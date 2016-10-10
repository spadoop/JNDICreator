package com.lanx.app.jndicreator.xml;

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.impl.WASJDBCDriverWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-26
 * Time: 10:47:01
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractWASXMLWriter extends AbstractXMLWriter {
    protected String xmlfile = "";

    public AbstractWASXMLWriter(File file) throws DocumentException {
        super(file);
        this.xmlfile = file.getAbsolutePath();
    }

    public AbstractWASXMLWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
        this.xmlfile = xmlfile;
    }

    public boolean write(JNDIEntity jndiEntity, String classpath) throws IOException {
        if (!doc.isReadOnly()) {
            //在写配置文件前需要先寻找/添加一下websphere变量
            String jdbcPath = getJDBCPath(jndiEntity.getDbType(), jndiEntity.getDriverClassName());
            createJDBCDriverPath(jdbcPath, classpath);

            Element xmi = doc.getRootElement();
            if(xmi.getNamespaceForURI("http://www.ibm.com/websphere/appserver/schemas/5.0/resources.jdbc.xmi") == null)
                xmi.addNamespace("resources.jdbc", "http://www.ibm.com/websphere/appserver/schemas/5.0/resources.jdbc.xmi");

            List resourceList = doc.selectNodes("/xmi:XMI/resources.jdbc:JDBCProvider");
            //List resourceList = doc.getRootElement().elements("resources.jdbc:JDBCProvider");
            if (existJNDI(resourceList, jndiEntity.getJndiname())) {
                updateServerPatchDocument(jndiEntity, jdbcPath);
            } else {
                createServerPatchDocument(jndiEntity, jdbcPath);
            }

            writeXMLFile(null);
            return true;
        }
        return false;
    }

    private boolean existJNDI(List resources, String jndiname) {
        for (int i = 0; resources != null && i < resources.size(); i++) {
            Element ele = (Element) resources.get(i);
            String resourceJndiName = ele.element("factories").valueOf("@jndiName");
            if (jndiname.equals(resourceJndiName))
                return true;
        }
        return false;
    }

    private String getJDBCPath(String localDbType, String driverClassName) {
        String jdbcPath = "";
        if (JNDIConstant.Database.MSSQLSERVER.equals(localDbType)) {
            if (driverClassName.indexOf("jtds") != -1) {
                jdbcPath = "JTDS_JDBC_DRIVER_PATH";
            } else {
                jdbcPath = "MSSQLSERVER_JDBC_DRIVER_PATH";
            }
        } else if (JNDIConstant.Database.ORACLE.equals(localDbType)) {
            jdbcPath = "ORACLE_JDBC_DRIVER_PATH";
        } else if (JNDIConstant.Database.MYSQL.equals(localDbType)) {
            jdbcPath = "MYSQL_JDBC_DRIVER_PATH";
        } else if (JNDIConstant.Database.DB2.equals(localDbType)) {
            jdbcPath = "DB2_JDBC_DRIVER_PATH";
        } else
            return null;
        return jdbcPath;
    }

    protected void getJDBCElementPatch(Element jdbc, String jdbcPath, String localDbType, String driverClassName) {
        if (JNDIConstant.Database.MSSQLSERVER.equals(localDbType)) {
            if (driverClassName.indexOf("jtds") != -1) {
                jdbc.addElement("classpath").setText("${" + jdbcPath + "}" + File.separator + "jtds-1.2.jar");
            } else {
                jdbc.addElement("classpath").setText("${" + jdbcPath + "}" + File.separator + "msbase.jar");
                jdbc.addElement("classpath").setText("${" + jdbcPath + "}" + File.separator + "mssqlserver.jar");
                jdbc.addElement("classpath").setText("${" + jdbcPath + "}" + File.separator + "msutil.jar");
            }
        } else if (JNDIConstant.Database.ORACLE.equals(localDbType)) {
            jdbc.addElement("classpath").setText("${" + jdbcPath + "}" + File.separator + "ojdbc14.jar");
        } else if (JNDIConstant.Database.MYSQL.equals(localDbType)) {
            jdbc.addElement("classpath").setText("${" + jdbcPath + "}" + File.separator + "mysql-connector-java-3.0.17-ga-bin.jar");
        }
    }

    private void createJDBCDriverPath(String jdbcPathName, String jdbcpathValue) {
        String path = "";
        int pos = xmlfile.indexOf("resources.xml");
        if (pos != -1)
            path = xmlfile.substring(0, pos) + "variables.xml";
        else
            throw new java.lang.IllegalStateException("找不到配置文件variables.xml，请检查输入参数！");

        try {
            WASJDBCDriverWriter jwriter = new WASJDBCDriverWriter(new File(path));
            jwriter.write(jdbcPathName, jdbcpathValue);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void createServerPatchDocument(JNDIEntity jndiEntity, String jdbcPath);

    protected void updateServerPatchDocument(JNDIEntity jndiEntity, String jdbcPath) {
        String jndiName = jndiEntity.getJndiname();
        Element factories = (Element) doc.selectSingleNode("/xmi:XMI/resources.jdbc:JDBCProvider/factories[@jndiName='"
                + jndiName + "']");
        if (factories != null) {
            Element propertySet = factories.element("propertySet");

            List resourceProperties = propertySet.elements();
            for (int i = 0; resourceProperties != null && i < resourceProperties.size(); i++) {
                Element resourceProperty = (Element) resourceProperties.get(i);
                if ("databaseName".equals(resourceProperty.valueOf("@name"))) {
                    resourceProperty.addAttribute("value", jndiEntity.getDatabaseName());
                } else if ("serverName".equals(resourceProperty.valueOf("@name"))) {
                    resourceProperty.addAttribute("value", jndiEntity.getServerName());
                } else if ("portNumber".equals(resourceProperty.valueOf("@name"))) {
                    resourceProperty.addAttribute("value", jndiEntity.getPort());
                } else if ("user".equals(resourceProperty.valueOf("@name"))) {
                    resourceProperty.addAttribute("value", jndiEntity.getUsername());
                } else if ("password".equals(resourceProperty.valueOf("@name"))) {
                    resourceProperty.addAttribute("value", jndiEntity.getPassword());
                } else if ("driverType".equals(resourceProperty.valueOf("@name"))) {
                    resourceProperty.addAttribute("value", jndiEntity.getDriverClassName());
                } else if ("URL".equals(resourceProperty.valueOf("@name"))) {
                    resourceProperty.addAttribute("value", this.getURL(jndiEntity));
                }
            }
        }
    }
}
