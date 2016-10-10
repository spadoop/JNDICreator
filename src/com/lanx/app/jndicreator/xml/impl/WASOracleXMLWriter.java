package com.lanx.app.jndicreator.xml.impl;

import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.AbstractWASXMLWriter;

import java.io.File;

import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 * 该类是为较旧的WebSphere的v4版Oracle数据源配置而定制的，配置WebSphere5.x及以上版本的数据源会有差别
 */
public class WASOracleXMLWriter extends AbstractWASXMLWriter {
    public WASOracleXMLWriter(File file) throws DocumentException {
        super(file);
    }

    public WASOracleXMLWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
    }

    protected void createServerPatchDocument(JNDIEntity jndiEntity, String jdbcPath) {
        String jndiName = jndiEntity.getJndiname();

        Element xmi = doc.getRootElement();
        
        Element jdbc = xmi.addElement("resources.jdbc:JDBCProvider")
                .addAttribute("xmi:id", "JDBCProvider_" + System.currentTimeMillis())
                .addAttribute("name", jndiName)
                .addAttribute("description", "Oracle JDBC Driver.")
                .addAttribute("implementationClassName", "oracle.jdbc.pool.OracleConnectionPoolDataSource")
                .addAttribute("xa", "false");

        getJDBCElementPatch(jdbc, jdbcPath, jndiEntity.getDbType(),jndiEntity.getDriverClassName());

        Element factories = jdbc.addElement("factories").addAttribute("xmi:type","resources.jdbc:WAS40DataSource")//.addAttribute("xmi:type", "resources.jdbc:DataSource")
                .addAttribute("xmi:id", "WAS40DataSource_" + System.currentTimeMillis())
                .addAttribute("name", jndiName)
                .addAttribute("jndiName", jndiName)
                .addAttribute("description", "an Oracle Datasource")
                .addAttribute("databaseName", jndiEntity.getDatabaseName())
                .addAttribute("defaultUser", jndiEntity.getUsername())
                .addAttribute("defaultPassword", jndiEntity.getPassword());
        Element propertySet = factories.addElement("propertySet").addAttribute("xmi:id", "J2EEResourcePropertySet_" + System.currentTimeMillis());
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "driverType")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", jndiEntity.getDriverClassName())
                .addAttribute("description", "The type of the driver.")
                .addAttribute("required", "false");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "TNSEntryName")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", "")
                .addAttribute("description", "The entry name which is used for the Oracle OCI driver.");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "networkProtocol")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", "")
                .addAttribute("description", "Whether to use TCP/IP or IPC or any other protocol");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "databaseName")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value",jndiEntity.getDatabaseName() )
                .addAttribute("description", "The database name.");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "serverName")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", jndiEntity.getServerName())
                .addAttribute("description", "The name of the server.")
                .addAttribute("required", "false");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "portNumber")
                .addAttribute("type", "java.lang.Integer")
                .addAttribute("value", jndiEntity.getPort())
                .addAttribute("description", "The TCP/IP port number where the jdbc driver resides.")
                .addAttribute("required", "false");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "dataSourceName")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", jndiName)
                .addAttribute("description", "The name of the Data Source.");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "URL")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value",this.getURL(jndiEntity) )
                .addAttribute("description", "This is a required property.")
                .addAttribute("required", "true");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "loginTimeout")
                .addAttribute("type", "java.lang.Integer")
                .addAttribute("value", "")
                .addAttribute("description", "The maximum time to attempt to connect a database.");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "description")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", "")
                .addAttribute("description", "The description of this datasource.");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "preTestSQLString")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", "SELECT * FROM dual")
                .addAttribute("description", "This SQL statement is used for pre-test connection function.")
                .addAttribute("required", "false");

        factories.addElement("connectionPool").addAttribute("xmi:id", "WAS40ConnectionPool_" + System.currentTimeMillis())
                .addAttribute("minimumPoolSize", "1")
                .addAttribute("maximumPoolSize", "30")
                .addAttribute("connectionTimeout", "1000")
                .addAttribute("idleTimeout", "2000")
                .addAttribute("orphanTimeout", "3000");
    }
}
