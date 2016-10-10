package com.lanx.app.jndicreator;

import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.vo.Entity;
import com.lanx.app.jndicreator.vo.JNDIEntity;

import java.io.IOException;
import java.util.List;

import org.dom4j.DocumentException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-24
 * Time: 11:21:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractJNDIWriter implements JNDIWriter {
    protected String getURL(JNDIEntity jndiEntity) {
        String localDBType = jndiEntity.getDbType();
        String serverName = jndiEntity.getServerName();
        //System.out.println("serverName = " + serverName);

        String port = jndiEntity.getPort();
        String properties = jndiEntity.getProperties();
        String databaseName = jndiEntity.getDatabaseName();

        if (JNDIConstant.Database.MSSQLSERVER.equals(localDBType)) {
            StringBuffer url = new StringBuffer();
            if (jndiEntity.getDriverClassName().indexOf("jtds") != -1) {
                String instance = "";
                int index = serverName.indexOf("\\");
                if (index != -1) {
                    instance = this.getInstanceStr(index, serverName);
                    serverName = serverName.substring(0, index);
                }

                url.append("jdbc:jtds:sqlserver://")
                        .append(serverName)
                        .append(":")
                        .append(port);

                if (!"".equals(databaseName)) {
                    url.append("/")
                            .append(databaseName);

                }
                if (!"".equals(instance)) {
                    url.append(";")
                            .append(instance);
                }
            } else {
                url.append("jdbc:microsoft:sqlserver://")
                        .append(serverName)
                        .append(":")
                        .append(port);

                if (!"".equals(properties)) {
                    url.append(";")
                            .append(properties);
                }
                if (!"".equals(databaseName)) {
                    url.append(";databaseName=")
                            .append(databaseName);
                }
            }
            //System.out.println("url.toString() = " + url.toString());
            return url.toString();
        } else if (JNDIConstant.Database.ORACLE.equals(localDBType)) {
            StringBuffer url = new StringBuffer("jdbc:oracle:oci:@");
            url.append(serverName);

            return url.toString();
        } else if (JNDIConstant.Database.MYSQL.equals(localDBType)) {
            StringBuffer url = new StringBuffer("jdbc:mysql://");
            url.append(serverName)
                    .append(":");

            if (port == null || "".equals(port))
                port = "3306";

            url.append(port)
                    .append("/")
                    .append(databaseName);

            return url.toString();
        } else if (JNDIConstant.Database.HSQLDB.equals(localDBType)) {
            StringBuffer url = new StringBuffer("jdbc:hsqldb:mem:");
            return url.toString();
        } else if (JNDIConstant.Database.DB2.equals(localDBType)) {
            //ToDo add db2 url
            //StringBuffer url = new StringBuffer("");
            return null;
        } else
            return null;
    }

    private String getInstanceStr(int index, String serverName) {
        StringBuffer sb = new StringBuffer("instance=");
        sb.append(serverName.substring(index + 1));
        return sb.toString();
    }

    protected String getDBType(String driverClassName) {
        if (driverClassName.indexOf("oracle") != -1) {
            return JNDIConstant.Database.ORACLE;
        } else if (driverClassName.indexOf("sqlserver") != -1
                || driverClassName.indexOf("jtds") != -1) {
            return JNDIConstant.Database.MSSQLSERVER;
        } else if (driverClassName.indexOf("mysql") != -1) {
            return JNDIConstant.Database.MYSQL;
        } else if (driverClassName.indexOf("db2") != -1) {
            return JNDIConstant.Database.DB2;
        } else
            return null;
    }

    public boolean write(Entity entity) throws IOException {
        return false;
    }

    public boolean write(List entitys) throws IOException, DocumentException {
        return false;
    }
}
