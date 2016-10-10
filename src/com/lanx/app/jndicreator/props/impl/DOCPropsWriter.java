package com.lanx.app.jndicreator.props.impl;

import com.lanx.app.jndicreator.props.AbstractDOCWriter;
import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.vo.Entity;
import com.lanx.app.jndicreator.vo.JNDIEntity;

import java.io.*;
import java.util.Properties;

public class DOCPropsWriter extends AbstractDOCWriter {
    public DOCPropsWriter(String afile){
        super(afile);
    }

    public boolean write(Entity entity) throws IOException {
        JNDIEntity jndiEntity = (JNDIEntity)entity;
        String driverClass = jndiEntity.getDriverClassName();
        String databaseName = jndiEntity.getDatabaseName();
        String dbType = this.getDBType(driverClass);
        jndiEntity.setDbType(dbType);
        Properties props = this.parseConfigFile();

        props.setProperty("db.driver",driverClass);
        props.setProperty("db.name",databaseName);
        props.setProperty("db.url",this.getURL(jndiEntity));
        props.setProperty("db.username",jndiEntity.getUsername());
        props.setProperty("db.password",jndiEntity.getPassword());

        File outFile = new File(filename);
        OutputStream fos = new FileOutputStream(outFile);
        props.store(fos, "connection pool.properties");
        fos.close();

        int index = filename.indexOf("alfresco");
        if(index != -1) {
            StringBuffer hibernateProperties = new StringBuffer(filename.substring(0,index + 8));
            hibernateProperties.append("/domain/hibernate-cfg.properties");

            return write(hibernateProperties.toString(),dbType);
        }else
            return false;
    }

    private boolean write(String hibernateProperties,String dbType) throws IOException {
        File file = new File(hibernateProperties);
        if(!file.exists()) {
            System.out.println("文档服务器的配置文件hibernate-cfg.properties不存在，请检查！");
            return false;
        }

        DOCPropsWriter docWriter = new DOCPropsWriter(hibernateProperties);
        Properties props = docWriter.parseConfigFile();
        StringBuffer dialect = new StringBuffer("org.hibernate.dialect.");
        if(JNDIConstant.Database.MSSQLSERVER.equals(dbType)) {
            props.setProperty("hibernate.dialect",dialect.append("SQLServerDialect").toString());
        }else if(JNDIConstant.Database.ORACLE.equals(dbType)) {
            props.setProperty("hibernate.dialect",dialect.append("OracleDialect").toString());
        }else if(JNDIConstant.Database.MYSQL.equals(dbType)) {
            props.setProperty("hibernate.dialect",dialect.append("MySQLDialect").toString());
        }else if(JNDIConstant.Database.DB2.equals(dbType)) {
            props.setProperty("hibernate.dialect",dialect.append("DB2Dialect").toString());
        }

        File outFile = new File(hibernateProperties);
        OutputStream fos = new FileOutputStream(outFile);
        props.store(fos, "hibernate cfg properties");
        fos.close();

        return true;
    }
}
