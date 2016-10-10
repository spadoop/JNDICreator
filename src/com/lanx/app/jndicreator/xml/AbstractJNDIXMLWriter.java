package com.lanx.app.jndicreator.xml;

import org.dom4j.DocumentException;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-24
 * Time: 10:17:48
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractJNDIXMLWriter extends AbstractXMLWriter {
    public AbstractJNDIXMLWriter(File file) throws DocumentException {
        super(file);
    }

    public AbstractJNDIXMLWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
    }
}
