package com.lanx.app.jndicreator.impl;

import com.lanx.app.jndicreator.AbstractJNDICreator;
import com.lanx.app.jndicreator.JNDICreator;
import com.lanx.app.jndicreator.xml.impl.Tomcat55XXMLWriter;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-24
 * Time: 13:40:50
 * To change this template use File | Settings | File Templates.
 */
public class Tomcat55XJNDICreatorImpl extends AbstractJNDICreator {
    protected Tomcat55XJNDICreatorImpl(String confFilename) throws DocumentException {
        super(confFilename);
    }

    public static JNDICreator newInstance(String confFilename) throws DocumentException {
        return new Tomcat55XJNDICreatorImpl(confFilename);
    }

    protected String getAppServerConfFile(String appServerHome) {
        return new StringBuffer(appServerHome).append("/conf/server.xml").toString();
    }

    public boolean genJNDI(String appServerHome) throws DocumentException, IOException {
        List entitys = loadParser.loadJNDIMetaData();

        writer = new Tomcat55XXMLWriter(getAppServerConfFile(appServerHome));
        return writer.write(entitys);
    }
}
