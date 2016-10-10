package com.lanx.app.jndicreator.vo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-14
 * Time: 15:15:44
 * To change this template use File | Settings | File Templates.
 */
public class JNDIEntity implements Serializable,Entity {
    private String jndiname;
    private String serverName;
    private String databaseName;
    private String port;
    private String username;
    private String password;
    private String driverClassName;
    private String properties;
    private String url;
    private String dbType;
    private String wlsJDBCFile;

    public String getJndiname() {
        return this.jndiname ;
    }

    public void setJndiname(String jndiname) {
        this.jndiname = jndiname;
    }

    public String getServerName() {
        return this.serverName ;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDatabaseName() {
        return this.databaseName ;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getPort() {
        return this.port ;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return this.username ;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password ;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getDriverClassName() {
        return this.driverClassName ;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getProperties() {
        return this.properties ;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getUrl() {
        return this.url ;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDbType() {
        return this.dbType ;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getWlsJDBCFile() {
        return this.wlsJDBCFile ;
    }

    public void setWlsJDBCFile(String wlsJDBCFile) {
        this.wlsJDBCFile = wlsJDBCFile;
    }

}
