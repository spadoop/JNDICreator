package com.lanx.app.jndicreator.xml.impl;

import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.AbstractWASXMLWriter;

import java.io.File;

import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 * 该类是为较旧的WebSphere的v4版MSSQLServer数据源配置而定制的，配置WebSphere5.x及以上版本的数据源会有差别
 */
public class WASMSSQLServerXMLWriter extends AbstractWASXMLWriter {
    public WASMSSQLServerXMLWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
    }

    public WASMSSQLServerXMLWriter(File file) throws DocumentException {
        super(file);
    }

    protected void createServerPatchDocument(JNDIEntity jndiEntity, String jdbcPath) {
        String jndiName = jndiEntity.getJndiname();

        Element xmi = doc.getRootElement();//(Element) doc.selectSingleNode("/xmi:XMI"));
        Element jdbc = xmi.addElement("resources.jdbc:JDBCProvider")
                .addAttribute("xmi:id", "JDBCProvider_" + System.currentTimeMillis())
                .addAttribute("name", jndiName)
                .addAttribute("description", "Microsoft JDBC driver for MSSQLServer 2000.")
                .addAttribute("implementationClassName", "com.microsoft.jdbcx.sqlserver.SQLServerDataSource")
                .addAttribute("xa", "false");

        getJDBCElementPatch(jdbc, jdbcPath, jndiEntity.getDbType(),jndiEntity.getDriverClassName());

        Element factories = jdbc.addElement("factories").addAttribute("xmi:type", "resources.jdbc:WAS40DataSource")
                .addAttribute("xmi:id", "WAS40DataSource_" + System.currentTimeMillis())
                .addAttribute("name", jndiName)
                .addAttribute("jndiName", jndiName)
                .addAttribute("description", "a New DataDirect ConnectJDBC Datasource")
                .addAttribute("databaseName", jndiEntity.getDatabaseName())
                .addAttribute("defaultUser", jndiEntity.getUsername())
                .addAttribute("defaultPassword", jndiEntity.getPassword());
        Element propertySet = factories.addElement("propertySet").addAttribute("xmi:id", "J2EEResourcePropertySet_" + System.currentTimeMillis());
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "databaseName")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", jndiEntity.getDatabaseName())
                .addAttribute("description", "This is a required property. The database name.")
                .addAttribute("required", "false");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "serverName")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", jndiEntity.getServerName())
                .addAttribute("description", "This is a required property. The name of the server where MS SQL Server resides.")
                .addAttribute("required", "true");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "portNumber")
                .addAttribute("type", "java.lang.Integer")
                .addAttribute("value", jndiEntity.getPort())
                .addAttribute("description", "This is a required property. The TCP/IP port number where MS SQL Server resides.")
                .addAttribute("required", "true");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "selectMethod")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", "cursor")
                .addAttribute("description", "Determine whether or not Microsoft SQL Server 'server cursors' are used for SQL queries.");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "dataSourceName")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", jndiName)
                .addAttribute("description", "The name of the Data Source.");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "spyAttributes")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", "")
                .addAttribute("description", "The spy attributes.");
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
                .addAttribute("name", "disable2Phase")
                .addAttribute("type", "java.lang.Boolean")
                .addAttribute("value", "true")
                .addAttribute("description", "When true, two phase connections are used.");
        propertySet.addElement("resourceProperties").addAttribute("xmi:id", "J2EEResourceProperty_" + System.currentTimeMillis())
                .addAttribute("name", "preTestSQLString")
                .addAttribute("type", "java.lang.String")
                .addAttribute("value", "SQL SELECT COUNT(*) FROM sysobjects")
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
