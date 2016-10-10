package com.lanx.app.jndicreator.impl;

import com.lanx.app.jndicreator.AbstractJNDICreator;
import com.lanx.app.jndicreator.JNDICreator;
import com.lanx.app.jndicreator.WLSXMLWriterFactory;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-21
 * Time: 15:08:21
 * To change this template use File | Settings | File Templates.
 */
public class WLSJNDICreatorImpl extends AbstractJNDICreator {
    private String release = "";
    protected WLSJNDICreatorImpl(String confFilename,String release) throws DocumentException {
        super(confFilename);
        this.release = release;
    }

    public static JNDICreator newInstance(String confFilename,String release) throws DocumentException {
        return new WLSJNDICreatorImpl(confFilename,release);
    }

    protected String getAppServerConfFile(String appServerHome) {
        StringBuffer wls = new StringBuffer(appServerHome);
        if(release.indexOf("9") != -1) {
            wls.append("/user_projects/domains/base_domain/pending/config.xml");
        }else if(release.indexOf("8") != -1) {
            wls.append("/user_projects/domains/mydomain/config.xml");
        }else{
            wls.append("/user_projects/domains/mydomain/config.xml");
        }
        return wls.toString();
    }

    public boolean genJNDI(String appServerHome) throws DocumentException, IOException {
        List entitys = loadParser.loadJNDIMetaData();
        writer = WLSXMLWriterFactory.getWLSXMLWriter(release,getAppServerConfFile(appServerHome));
        return writer.write(entitys);
    }
}
