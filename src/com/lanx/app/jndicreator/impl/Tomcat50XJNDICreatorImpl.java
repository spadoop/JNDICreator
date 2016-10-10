package com.lanx.app.jndicreator.impl;

import com.lanx.app.jndicreator.AbstractJNDICreator;
import com.lanx.app.jndicreator.JNDICreator;
import com.lanx.app.jndicreator.xml.impl.Tomcat50XXMLWriter;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-20
 * Time: 10:27:36
 * To change this template use File | Settings | File Templates.
 */
public class Tomcat50XJNDICreatorImpl extends AbstractJNDICreator {
    protected Tomcat50XJNDICreatorImpl(String confFilename) throws DocumentException {
        super(confFilename);
    }

    protected String getAppServerConfFile(String appServerHome) {
        return new StringBuffer(appServerHome).append("/conf/server.xml").toString();
    }

    public static JNDICreator newInstance(String confFilename) throws DocumentException {
        return new Tomcat50XJNDICreatorImpl(confFilename);
    }

    /**
     * @param appServerHome 传入的App server的需要增加jndi的配置文件的根路径
     * @throws DocumentException IOException
     * */
    public boolean genJNDI(String appServerHome) throws DocumentException, IOException {
        List entitys = loadParser.loadJNDIMetaData();

        writer = new Tomcat50XXMLWriter(getAppServerConfFile(appServerHome));
        return writer.write(entitys);
    }
}
