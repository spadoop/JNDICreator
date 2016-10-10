package com.lanx.app.jndicreator.vo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-23
 * Time: 23:26:59
 * To change this template use File | Settings | File Templates.
 */
public class BizEntity implements Serializable,Entity {
    private String bizurl;
    private String databaseName;
    private String serverInfo;

    public String getBizurl() {
        return this.bizurl;
    }

    public void setBizurl(String bizurl) {
        this.bizurl = bizurl;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getServerInfo() {
        return this.serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }
}
