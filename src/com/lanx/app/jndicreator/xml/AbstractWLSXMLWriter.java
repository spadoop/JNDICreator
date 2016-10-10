package com.lanx.app.jndicreator.xml;

import org.dom4j.DocumentException;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-30
 * Time: 9:01:25
 * To change this template use File | Settings | File Templates.
 */
public class AbstractWLSXMLWriter extends AbstractXMLWriter {
    protected String config = "";
    public AbstractWLSXMLWriter(File file) throws DocumentException {
        super(file);
        this.config = file.getAbsolutePath();
    }

    public AbstractWLSXMLWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
        this.config = xmlfile;
    }
}
