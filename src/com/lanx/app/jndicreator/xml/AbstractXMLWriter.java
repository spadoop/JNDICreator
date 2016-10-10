package com.lanx.app.jndicreator.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

import com.lanx.app.jndicreator.AbstractJNDIWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-10
 * Time: 15:18:54
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractXMLWriter extends AbstractJNDIWriter {
    //可能得到的是server.xml形成的doc
    protected Document doc = null;
    protected File file = null;
    protected String dbType = "";

    protected SAXReader reader = new SAXReader();

    public AbstractXMLWriter(File afile, String dbType) throws DocumentException {
        this.file = afile;
        this.dbType = dbType;

        //jboss一开始传入的是app sever的home目录
        if (!file.isDirectory()) {
            if (!file.exists()) {
                doc = DocumentHelper.createDocument();
            } else
                doc = reader.read(file);
        }
    }

    public AbstractXMLWriter(String xmlfile, String dbType) throws DocumentException {
        this(new File(xmlfile), dbType);
    }

    public AbstractXMLWriter(String xmlfile) throws DocumentException {
        this(new File(xmlfile));
    }

    public AbstractXMLWriter(File file) throws DocumentException {
        this(file, null);
    }

    public Document getResourceDoc() {
        return doc;
    }

    protected String getChar(int level) {
        switch (level) {
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            default :
                return "error";
        }
    }

    protected void addParameterElement(Element root, String name, String value) {
        Element parameter = root.addElement("property");
        parameter.addElement("name").setText(name);
        parameter.addElement("value").setText(value);
    }

    protected void writeXMLFile(String encoding) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        if (encoding != null)
            format.setEncoding(encoding);

        XMLWriter writer = new XMLWriter(new FileWriter(file), format);

        writer.write(doc);
        writer.flush();
        writer.close();
    }
}
