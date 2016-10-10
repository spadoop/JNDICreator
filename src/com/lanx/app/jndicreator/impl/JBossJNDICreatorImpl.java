package com.lanx.app.jndicreator.impl;

import com.lanx.app.jndicreator.AbstractJNDICreator;
import com.lanx.app.jndicreator.JNDICreator;
import com.lanx.app.jndicreator.xml.impl.JBossXMLWriter;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-21
 * Time: 8:54:20
 * To change this template use File | Settings | File Templates.
 */
public class JBossJNDICreatorImpl extends AbstractJNDICreator {
    protected JBossJNDICreatorImpl(String confFilename) throws DocumentException {
        super(confFilename);
    }

    protected String getAppServerConfFile(String appServerHome) {
        //jboss用到了多个文件，因此放到JBossXMLWriter中处理
        return appServerHome;// + "/server/default/deploy/"+;
    }

    public boolean genJNDI(String appServerHome) throws DocumentException, IOException {
        List entitys = loadParser.loadJNDIMetaData();

        writer = new JBossXMLWriter(getAppServerConfFile(appServerHome));
        
        return writer.write(entitys);
    }

    public static JNDICreator newInstance(String confFilename) throws DocumentException {
        return new JBossJNDICreatorImpl(confFilename);
    }
}
