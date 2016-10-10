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
public class Tomcat50XXMLWriter extends AbstractXMLWriter {
    public Tomcat50XXMLWriter(String appServerConfFile) throws DocumentException {
        super(appServerConfFile);
    }

    public Tomcat50XXMLWriter(File file) throws DocumentException {
        super(file);
    }

    public boolean write(List entitys) throws IOException {
        for (int i = 0; entitys != null && i < entitys.size(); i++) {
            JNDIEntity jndiEntity = (JNDIEntity) entitys.get(i);
            //根据driverclass判断数据库的类型
            String localDBType = this.getDBType(jndiEntity.getDriverClassName());
            jndiEntity.setDbType(localDBType);
            Element ele[] = getExistJNDI(jndiEntity.getJndiname());
            if (ele != null) {
                updateServerPatchDocument(jndiEntity, ele);
            } else {
                createServerPatchDocument(jndiEntity);
            }
        }
        writeXMLFile(null);
        return true;
    }

    private Element[] getExistJNDI(String jndiname) {
        Element ele[] = new Element[2];
        Node node = doc.selectSingleNode("//*[@path='/BusinessWeb']");

        if (node != null) {
            List resources = ((Element) node).elements("ResourceParams");
            for (int i = 0; resources != null && i < resources.size(); i++) {
                Element resourceParams = (Element) resources.get(i);
                String realpath = resourceParams.getUniquePath();
                realpath = realpath.replaceAll("ResourceParams", "Resource");

                if (resourceParams.valueOf("@name").equals(jndiname)) {
                    Node resourceNode = doc.selectSingleNode(realpath);
                    if (resourceNode == null)
                        throw new java.lang.IllegalStateException("Something Wrong! Check the config file server.xml!");

                    ele[0] = (Element) resourceNode;
                    ele[1] = resourceParams;
                    return ele;
                }
            }
        }
        return null;
    }

    private void updateServerPatchDocument(JNDIEntity jndiEntity, Element[] ele) {
        String jndiname = jndiEntity.getJndiname();

        ele[0].addAttribute("name", jndiname);
        ele[1].addAttribute("name", jndiname);
        updateParameterElement(ele[1], jndiEntity);
    }

    private void updateParameterElement(Element element, JNDIEntity jndiEntity) {
        List eleList = element.elements("parameter");
        for (int i = 0; i < eleList.size(); i++) {
            Element parameter = (Element) eleList.get(i);
            if ("url".equals(parameter.element("name").getText())) {
                parameter.element("value").setText(this.getURL(jndiEntity));
            } else if ("username".equals(parameter.element("name").getText())) {
                parameter.element("value").setText(jndiEntity.getUsername());
            } else if ("password".equals(parameter.element("name").getText())) {
                parameter.element("value").setText(jndiEntity.getPassword());
            } else if ("driverClassName".equals(parameter.element("name").getText())) {
                parameter.element("value").setText(jndiEntity.getDriverClassName());
            }
        }
    }

    private void createServerPatchDocument(JNDIEntity jndiEntity) {
        Element context = (Element)doc.selectSingleNode("//*[@path='/BusinessWeb']");

        if(context == null) {
            Element host = (Element) doc.selectSingleNode("//Server/Service/Engine/Host");

            context = host.addElement("Context").addAttribute("path", "/BusinessWeb")
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

        createServerPatchDocument(context, jndiEntity);
    }

    private void createServerPatchDocument(Element context, JNDIEntity jndiEntity) {
        String jndiname = jndiEntity.getJndiname();

        context.addElement("Resource").addAttribute("name", jndiname)
                .addAttribute("auth", "Container")
                .addAttribute("type", "javax.sql.DataSource");

        Element resourceParams = context.addElement("ResourceParams").addAttribute("name", jndiname);
        addParameterElement(resourceParams, "factory", "org.apache.commons.dbcp.BasicDataSourceFactory");
        addParameterElement(resourceParams, "url", this.getURL(jndiEntity));
        addParameterElement(resourceParams, "maxActive", "100");
        addParameterElement(resourceParams, "maxIdle", "30");
        addParameterElement(resourceParams, "maxWait", "10000");
        addParameterElement(resourceParams, "username", jndiEntity.getUsername());
        addParameterElement(resourceParams, "password", jndiEntity.getPassword());
        addParameterElement(resourceParams, "driverClassName", jndiEntity.getDriverClassName());

    }
}
