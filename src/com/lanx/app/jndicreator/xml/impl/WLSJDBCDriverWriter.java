package com.lanx.app.jndicreator.xml.impl;

import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.AbstractXMLWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-30
 * Time: 10:15:15
 * To change this template use File | Settings | File Templates.
 */
public class WLSJDBCDriverWriter extends AbstractXMLWriter {
    public WLSJDBCDriverWriter(File file) throws DocumentException {
        super(file);
    }

    public WLSJDBCDriverWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
    }

    public boolean write(JNDIEntity jndiEntity,String operationType) throws IOException {
        if(operationType.equals(JNDIConstant.Common.NEW)) {
            createNewJDBCFile(jndiEntity);
        }else if(operationType.equals(JNDIConstant.Common.UPDATE)) {
            updateJDBCFile(jndiEntity);
        }

        writeXMLFile(null);
        return true;
    }

    private void updateJDBCFile(JNDIEntity jndiEntity) {
        String url = this.getURL(jndiEntity);
        String localDbType = this.getDBType(jndiEntity.getDriverClassName());

        Element jdbcDriver = doc.getRootElement().element("jdbc-driver-params");
        jdbcDriver.element("url").setText(url);
        jdbcDriver.element("driver-name").setText(jndiEntity.getDriverClassName());
        jdbcDriver.element("password-encrypted").setText(jndiEntity.getPassword());

        Element properties = jdbcDriver.element("properties");
        updateProperties(properties,jndiEntity);

        Node testTableNode = doc.selectSingleNode("//jdbc-connection-pool-params/test-table-name");
        testTableNode.setText(getTestTableName(localDbType));
    }

    private void updateProperties(Element properties,JNDIEntity jndiEntity) {
        List propList = properties.elements("property");
        for(int i = 0;propList != null && i<propList.size() ;i++) {
            Element ele = (Element)propList.get(i);
            Element childEle = ele.element("name");
            if("user".equals(childEle.getText())){
                ele.element("value").setText(jndiEntity.getUsername());
            }else if("url".equals(childEle.getText())) {
                ele.element("value").setText(this.getURL(jndiEntity));
            }else if("dataSourceName".equals(childEle.getText())) {
                ele.element("value").setText(jndiEntity.getDatabaseName());
            }else if("userName".equals(childEle.getText())) {
                ele.element("value").setText(jndiEntity.getUsername());
            }else if("databaseName".equals(childEle.getText())) {
                ele.element("value").setText(jndiEntity.getDatabaseName());
            }else if("serverName".equals(childEle.getText())) {
                ele.element("value").setText(jndiEntity.getServerName());
            }
        }
    }

    private void createNewJDBCFile(JNDIEntity jndiEntity) {
        String localDbType = this.getDBType(jndiEntity.getDriverClassName());
        String url = this.getURL(jndiEntity);

        Element datasource = doc.addElement("jdbc-data-source")
                .addAttribute("xmlns","http://www.bea.com/ns/weblogic/90")
                .addAttribute("xmlns:sec","http://www.bea.com/ns/weblogic/90/security")
                .addAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance")
                .addAttribute("xmlns:wls","http://www.bea.com/ns/weblogic/90/security/wls")
                .addAttribute("xsi:schemaLocation","http://www.bea.com/ns/weblogic/910/domain.xsd");
        datasource.addElement("name").setText(jndiEntity.getJndiname());

        Element jdbcDriver = datasource.addElement("jdbc-driver-params");
        jdbcDriver.addElement("url").setText(url);//jndiEntity.getUrl());
        jdbcDriver.addElement("driver-name").setText(jndiEntity.getDriverClassName());

        Element properties = jdbcDriver.addElement("properties");
        addParameterElement(properties,"user",jndiEntity.getUsername());
        addParameterElement(properties,"url",url);
        addParameterElement(properties,"selectMethod","cursor");
        addParameterElement(properties,"dataSourceName","SQL2000JDBC");
        addParameterElement(properties,"userName",jndiEntity.getUsername());
        addParameterElement(properties,"databaseName",jndiEntity.getDatabaseName());
        addParameterElement(properties,"serverName",jndiEntity.getServerName());

        jdbcDriver.addElement("password-encrypted").setText(jndiEntity.getPassword());
        datasource.addElement("jdbc-connection-pool-params")
                .addElement("test-table-name").setText(getTestTableName(localDbType));

        Element jdbcDatasource = datasource.addElement("jdbc-data-source-params");
        jdbcDatasource.addElement("jndi-name").setText(jndiEntity.getJndiname());
        jdbcDatasource.addElement("global-transactions-protocol").setText("OnePhaseCommit");
    }

    private String getTestTableName(String localDBType) {
        if (JNDIConstant.Database.ORACLE.equals(localDBType)) {
            return "SQL SELECT 1 FROM DUAL";
        } else if (JNDIConstant.Database.MSSQLSERVER.equals(localDBType)) {
            return "SQL SELECT COUNT(*) FROM sysobjects";
        } else if (JNDIConstant.Database.MYSQL.equals(localDBType)) {
            return "SQL SELECT 1";
        } else if (JNDIConstant.Database.DB2.equals(localDBType)) {
            return "";
        } else
            return null;
    }
}
