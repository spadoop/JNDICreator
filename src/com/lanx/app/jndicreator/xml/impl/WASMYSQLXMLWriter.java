package com.lanx.app.jndicreator.xml.impl;

import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.AbstractWASXMLWriter;

import org.dom4j.DocumentException;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-26
 * Time: 11:02:21
 * To change this template use File | Settings | File Templates.
 */
public class WASMYSQLXMLWriter extends AbstractWASXMLWriter {
    public WASMYSQLXMLWriter(File file) throws DocumentException {
        super(file);
    }

    public WASMYSQLXMLWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
    }

    protected void createServerPatchDocument(JNDIEntity jndiEntity, String jdbcPath) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
