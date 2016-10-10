package com.lanx.app.jndicreator.xml.impl;

import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.vo.Entity;
import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.AbstractJNDIXMLWriter;

import java.io.IOException;
import java.io.File;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-24
 * Time: 9:46:33
 * To change this template use File | Settings | File Templates.
 */
public class IMXMLWriter extends AbstractJNDIXMLWriter {
    public IMXMLWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
    }

    public IMXMLWriter(File file) throws DocumentException {
        super(file);
    }

    public boolean write(Entity entity) throws IOException {
        JNDIEntity jndiEntity = (JNDIEntity) entity;
        //根据driverclass判断数据库的类型
        //String localDBType = this.getDBType(jndiEntity.getDriverClassName());
        //jndiEntity.setDbType(localDBType);
        Element ele = getExistJNDI();
        if (ele != null) {
            updateServerPatchDocument(jndiEntity, ele);
        } else {
            createServerPatchDocument(jndiEntity);
        }

        writeXMLFile(null);
        return true;
    }

    private Element getExistJNDI() {
        Node node = doc.selectSingleNode("/jive/database");

        if (node != null) {
            return (Element)node;
        }
        return null;
    }

    private void updateServerPatchDocument(JNDIEntity jndiEntity, Element databaseEle) {
        Element provider = databaseEle.element("defaultProvider");
        Element setup = databaseEle.getParent().element("setup");
        String driverClass = jndiEntity.getDriverClassName();
        String localDBType = jndiEntity.getDbType();
        StringBuffer url = new StringBuffer(this.getURL(jndiEntity));
        if (JNDIConstant.Database.MSSQLSERVER.equals(localDBType)) {
            url.append(";appName=jive");
        }
        String username = jndiEntity.getUsername();
        String password = jndiEntity.getPassword();

        //provider有的情况下也可能没有下面的element
        if(provider != null) {
            provider.element("driver").setText(driverClass);
            provider.element("serverURL").setText(url.toString());
            provider.element("username").setText(username);
            provider.element("password").setText(password);
        }else {
            Element defaultProvider = databaseEle.addElement("defaultProvider");
            defaultProvider.addElement("driver").setText(driverClass);
            defaultProvider.addElement("serverURL").setText(url.toString());
            defaultProvider.addElement("username").setText(username);
            defaultProvider.addElement("password").setText(password);
            defaultProvider.addElement("minConnections").setText("5");
            defaultProvider.addElement("maxConnections").setText("15");
            defaultProvider.addElement("connectionTimeout").setText("1.0");
        }

        if(setup != null) {
            setup.setText("true");
        }else
            databaseEle.getParent().addElement("setup").setText("true");
    }

    private void createServerPatchDocument(JNDIEntity jndiEntity) {
        Element jive = (Element)doc.selectSingleNode("/jive");
        if(jive != null) {
            Element database = jive.addElement("database");
            updateServerPatchDocument(jndiEntity,database);
        }
    }
}
