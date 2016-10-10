package com.lanx.app.jndicreator;

import org.dom4j.DocumentException;
import com.lanx.app.jndicreator.xml.AbstractXMLWriter;
import com.lanx.app.jndicreator.xml.impl.config.CFGLoadParser;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-20
 * Time: 14:32:44
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractJNDICreator implements JNDICreator {
    protected CFGLoadParser loadParser = null;
    protected AbstractXMLWriter writer = null;

    protected AbstractJNDICreator(String confFilename) throws DocumentException {
        File confFile = new File(confFilename);
        if(!confFile.exists()) {
            throw new java.lang.NullPointerException("Cannot find the file indicated ! ");
        }

        loadParser = new CFGLoadParser(confFile);
    }

    protected abstract String getAppServerConfFile(String appServerHome);
}
