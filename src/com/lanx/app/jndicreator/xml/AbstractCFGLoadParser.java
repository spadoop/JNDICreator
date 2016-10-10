package com.lanx.app.jndicreator.xml;

import org.dom4j.DocumentException;

import java.io.File;
import java.util.List;

import com.lanx.app.jndicreator.vo.BizEntity;
import com.lanx.app.jndicreator.vo.JNDIEntity;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-20
 * Time: 11:53:22
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCFGLoadParser extends AbstractXMLWriter{
    public AbstractCFGLoadParser(File file) throws DocumentException {
        super(file);
    }

    public AbstractCFGLoadParser(String xmlfile) throws DocumentException {
        super(xmlfile);
    }

    public abstract List loadJNDIMetaData();
    public abstract JNDIEntity loadMetaDataByName(String databaseName,String serverInfo);
    public abstract BizEntity[] loadConnectionMetaData();
    public abstract List loadConnections();
    public abstract JNDIEntity loadSystemMetaData();
    public abstract List loadSystemConnections();
}
