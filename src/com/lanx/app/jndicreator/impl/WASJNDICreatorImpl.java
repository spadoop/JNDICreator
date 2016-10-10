package com.lanx.app.jndicreator.impl;

import com.lanx.app.jndicreator.AbstractJNDICreator;
import com.lanx.app.jndicreator.JNDICreator;
import com.lanx.app.jndicreator.WASXMLWriterFactory;
import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.AbstractWASXMLWriter;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.List;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-25
 * Time: 14:06:42
 * To change this template use File | Settings | File Templates.
 */
public class WASJNDICreatorImpl extends AbstractJNDICreator {
    protected String classpath = "";

    protected WASJNDICreatorImpl(String confFilename,String classpath) throws DocumentException {
        super(confFilename);
        this.classpath = classpath;
    }

    public static JNDICreator newInstance(String confFilename,String classpath) throws DocumentException {
        return new WASJNDICreatorImpl(confFilename,classpath);
    }

    protected String getAppServerConfFile(String appServerHome) {
        try {
            String localName = InetAddress.getLocalHost().getHostName();
            return new StringBuffer(appServerHome).append("/AppServer/config/cells/")
                    .append(localName)
                    .append("/nodes/")
                    .append(localName)
                    .append("/resources.xml").toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean genJNDI(String appServerHome) throws DocumentException, IOException {
        List entitys = loadParser.loadJNDIMetaData();

        for(int i = 0;entitys != null && i<entitys.size() ;i++) {
            JNDIEntity jndiEntity = (JNDIEntity)entitys.get(i);
            writer = WASXMLWriterFactory.getWASXMLWriter(jndiEntity.getDbType(),getAppServerConfFile(appServerHome));
            ((AbstractWASXMLWriter)writer).write(jndiEntity,classpath);
        }
        return true;
    }
}
