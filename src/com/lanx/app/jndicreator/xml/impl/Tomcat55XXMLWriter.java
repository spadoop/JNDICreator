package com.lanx.app.jndicreator.xml.impl;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.AbstractXMLWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-3-31
 * Time: 14:16:27
 * To change this template use File | Settings | File Templates.
 */
public class Tomcat55XXMLWriter extends AbstractXMLWriter {
    public Tomcat55XXMLWriter(String appServerConfFile) throws DocumentException {
        super(appServerConfFile);
    }

    public Tomcat55XXMLWriter(File file) throws DocumentException {
        super(file);
    }

    public boolean write(List entitys) throws IOException {
        for (int i = 0; entitys != null && i < entitys.size(); i++) {
            JNDIEntity jndiEntity = (JNDIEntity) entitys.get(i);
            //根据driverclass判断数据库的类型
            //String localDBType = this.getDBType(jndiEntity.getDriverClassName());
            //jndiEntity.setDbType(localDBType);
            Element ele = getExistJNDI(jndiEntity.getJndiname());
            if (ele != null) {
                updateServerPatchDocument(jndiEntity, ele);
            } else {
                createServerPatchDocument(jndiEntity);
            }
        }
        writeXMLFile(null);
        return true;
    }

    private Element getExistJNDI(String jndiname) {
        Node node = doc.selectSingleNode("//*[@docBase='BusinessWeb']");

        if (node != null) {
            List resources = ((Element) node).elements("Resource");
            for (int i = 0; resources != null && i < resources.size(); i++) {
                Element resource = (Element) resources.get(i);
                if (resource.valueOf("@name").equals(jndiname)) {
                    return resource;
                }
            }
        }
        return null;
    }

    private void updateServerPatchDocument(JNDIEntity jndiEntity, Element ele) {
        String url = this.getURL(jndiEntity);

        ele.addAttribute("name", jndiEntity.getJndiname())
                .addAttribute("url", url)
                .addAttribute("username", jndiEntity.getUsername())
                .addAttribute("password", jndiEntity.getPassword())
                .addAttribute("driverClassName", jndiEntity.getDriverClassName())
                .addAttribute("auth", "Container")
                .addAttribute("type", "javax.sql.DataSource");
    }

    private void createServerPatchDocument(JNDIEntity jndiEntity) {
        Element context = (Element) doc.selectSingleNode("//*[@docBase='BusinessWeb']");
        Element resource = null;
        if (context == null) {
            Element host = (Element) doc.selectSingleNode("/Server/Service/Engine/Host");

            context = host.addElement("Context").addAttribute("path", "")
                    .addAttribute("docBase", "BusinessWeb")
                    .addAttribute("debug", "5")
                    .addAttribute("reloadable", "true")
                    .addAttribute("crossContext", "true");
            context.addElement("Logger").addAttribute("className", "org.apache.catalina.logger.FileLogger")
                    .addAttribute("directory", "logs")
                    .addAttribute("prefix", "localhost_lanx_log.")
                    .addAttribute("suffix", ".txt")
                    .addAttribute("timestamp", "true");


        }
        resource = context.addElement("Resource");
        updateServerPatchDocument(jndiEntity, resource);
    }
}
