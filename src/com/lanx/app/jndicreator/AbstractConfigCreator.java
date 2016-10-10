package com.lanx.app.jndicreator;

import org.dom4j.DocumentException;

import java.io.IOException;

import com.lanx.app.jndicreator.vo.BizEntity;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-24
 * Time: 9:06:06
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractConfigCreator extends AbstractJNDICreator implements ConfigCreator{
    protected JNDIWriter jndiWriter = null;
    protected BizEntity[] bizEntity = null;

    protected AbstractConfigCreator(String confFilename) throws DocumentException {
        super(confFilename);
        bizEntity = loadParser.loadConnectionMetaData();
    }

    protected String getAppServerConfFile(String appServerHome) {
        return null;
    }

    public boolean genJNDI(String appServerHome) throws DocumentException, IOException {
        return false;
    }
}
