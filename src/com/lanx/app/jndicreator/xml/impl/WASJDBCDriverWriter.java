package com.lanx.app.jndicreator.xml.impl;

import com.lanx.app.jndicreator.xml.AbstractXMLWriter;

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-26
 * Time: 10:52:35
 * To change this template use File | Settings | File Templates.
 */
public class WASJDBCDriverWriter extends AbstractXMLWriter {
    private int entrys;

    public WASJDBCDriverWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
    }

    public WASJDBCDriverWriter(File file) throws DocumentException {
        super(file);
    }

    private Element existJDBCDriver(String symbolicName) {
        List list = doc.selectNodes("//variables:VariableMap/entries");
        entrys = list.size();
        for (int i = 0; i < entrys; i++) {
            Node node = (Node) list.get(i);
            String jdbcName = node.valueOf("@symbolicName");
            //System.out.println("jdbcName =" + jdbcName);

            if (symbolicName.equals(jdbcName))
                return (Element)node;
        }
        return null;
    }

    private void createJDBCDriver(String jdbcpath,String jdbcpathValue) {
        Element root = (Element)doc.selectSingleNode("//entries/parent::*");//.selectSingleNode("/variables:VariableMap");
        if(root != null) {
            String description = "";
            if(jdbcpath.indexOf("ORACLE") != -1 ) {
                description = "The directory that contains the Oracle thin or oci8  JDBC Driver.";
            }else if(jdbcpath.indexOf("SQLSERVER") != -1 || jdbcpath.indexOf("JTDS") != -1) {
                description = "The directory that contains the Microsoft JDBC driver for MSSQLServer 2000.";
            }

            int sequence = entrys + 1;
            root.addElement("entries").addAttribute("xmi:id", "VariableSubstitutionEntry_" + sequence)
                    .addAttribute("symbolicName", jdbcpath)
                    .addAttribute("value", jdbcpathValue)
                    .addAttribute("description", description);
        }
    }

    private void updateJDBCDriver(Element ele,String jdbcpathValue) {
        ele.addAttribute("value", jdbcpathValue);
    }

    public boolean write(String jdbcpath,String jdbcpathValue) throws IOException {
        if(jdbcpathValue == null || "".equals(jdbcpathValue)) {
            throw new java.lang.IllegalArgumentException("WebSphere的JDBC驱动程序目录不能为空，请检查输入的实参！");    
        }

        Element ele = existJDBCDriver(jdbcpath);
        if (ele == null) {
            createJDBCDriver(jdbcpath,jdbcpathValue);
        }else {
            updateJDBCDriver(ele,jdbcpathValue);
        }

        this.writeXMLFile(null);
        return true;
    }
}
