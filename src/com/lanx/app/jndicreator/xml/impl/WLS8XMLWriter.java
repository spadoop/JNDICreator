package com.lanx.app.jndicreator.xml.impl;

import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.AbstractWLSXMLWriter;

import java.util.List;
import java.io.IOException;
import java.io.File;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-21
 * Time: 15:15:38
 * To change this template use File | Settings | File Templates.
 */
public class WLS8XMLWriter extends AbstractWLSXMLWriter {
    public WLS8XMLWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
    }

    public WLS8XMLWriter(File file) throws DocumentException {
        super(file);
    }

    public boolean write(List entitys) throws IOException {
        for (int i = 0; entitys != null && i < entitys.size(); i++) {
            JNDIEntity jndiEntity = (JNDIEntity) entitys.get(i);
            //根据driverclass判断数据库的类型
            //String localDBType = this.getDBType(jndiEntity.getDriverClassName());
            //jndiEntity.setDbType(localDBType);

            Element ele[] = getExistJNDI(jndiEntity.getJndiname());
            if (ele != null) {
                updateServerPatchDocument(jndiEntity, ele[1],JNDIConstant.Common.UPDATE);
            } else {
                createServerPatchDocument(jndiEntity);
            }
        }
        writeXMLFile(null);
        return true;
    }

    private Element[] getExistJNDI(String jndiname) {
        Element ele[] = new Element[2];
        Node datasourceNode = doc.selectSingleNode("//JDBCTxDataSource[@JNDIName='" + jndiname + "']");

        if (datasourceNode != null) {
            Node poolNode = doc.selectSingleNode("//JDBCConnectionPool[@Name='" + datasourceNode.valueOf("@PoolName") + "']");
            if (poolNode == null)
                throw new java.lang.IllegalArgumentException("Something Wrong! Check the config file config.xml!");

            ele[0] = (Element) datasourceNode;
            ele[1] = (Element) poolNode;

            return ele;
        }
        return null;
    }

    private void updateServerPatchDocument(JNDIEntity jndiEntity, Element poolEle,String operationType) {
        StringBuffer properties = new StringBuffer((jndiEntity.getProperties() == null) ? "" :jndiEntity.getProperties());
        if(properties.length() == 0) {
            properties.append("user=")
                    .append(jndiEntity.getUsername());
        }else {
            properties.append(";user=")
                    .append(jndiEntity.getUsername());
        }

        poolEle.addAttribute("DriverName", jndiEntity.getDriverClassName())
                .addAttribute("Password", jndiEntity.getPassword())
                .addAttribute("Properties", properties.toString())
                .addAttribute("Targets", "myserver")
                .addAttribute("TestTableName", this.getTestTableName(jndiEntity.getDbType()))
                .addAttribute("URL", this.getURL(jndiEntity));

        if(JNDIConstant.Common.NEW.equals(operationType)) {
            poolEle.addAttribute("Name",jndiEntity.getJndiname());
        }
    }

    private void createServerPatchDocument(JNDIEntity jndiEntity) {
        String jndiname = jndiEntity.getJndiname();

        Element domain = doc.getRootElement();//(Element) doc.selectSingleNode("/Domain");
        if (domain != null) {
            domain.addElement("JDBCTxDataSource").addAttribute("JNDIName", jndiname)
                    .addAttribute("Name", jndiname + System.currentTimeMillis())
                    .addAttribute("PoolName", jndiname)
                    .addAttribute("Targets", "myserver");

            Element connpool = domain.addElement("JDBCConnectionPool");

            this.updateServerPatchDocument(jndiEntity,connpool,JNDIConstant.Common.NEW);
            //System.out.println("final doc = " + doc.asXML());
        }
    }

    private String getTestTableName(String localDBType) {
        if(JNDIConstant.Database.ORACLE.equals(localDBType)) {
            return "SQL SELECT 1 FROM DUAL";
        }else if(JNDIConstant.Database.MSSQLSERVER.equals(localDBType)) {
            return "SQL SELECT COUNT(*) FROM sysobjects";
        }else if(JNDIConstant.Database.MYSQL.equals(localDBType)) {
            return "SQL SELECT 1";
        }else if(JNDIConstant.Database.DB2.equals(localDBType)) {
            return "";
        }else
            return null;
    }
}
