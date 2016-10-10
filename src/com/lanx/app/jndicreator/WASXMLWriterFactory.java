package com.lanx.app.jndicreator;

import org.dom4j.DocumentException;

import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.xml.AbstractWASXMLWriter;
import com.lanx.app.jndicreator.xml.impl.WASMSSQLServerXMLWriter;
import com.lanx.app.jndicreator.xml.impl.WASMYSQLXMLWriter;
import com.lanx.app.jndicreator.xml.impl.WASOracleXMLWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-26
 * Time: 11:27:08
 * To change this template use File | Settings | File Templates.
 */
public class WASXMLWriterFactory {
    public static AbstractWASXMLWriter getWASXMLWriter(String dbType,String serverInfo) throws DocumentException {
        if(JNDIConstant.Database.MSSQLSERVER.equals(dbType)) {
            return new WASMSSQLServerXMLWriter(serverInfo);
        }else if(JNDIConstant.Database.ORACLE.equals(dbType)) {
            return new WASOracleXMLWriter(serverInfo);
        }else if(JNDIConstant.Database.MYSQL.equals(dbType)) {
            return new WASMYSQLXMLWriter(serverInfo);
        }else if(JNDIConstant.Database.DB2.equals(dbType)) {
            //new WASMSSQLServerXMLWriter(file).write(jndiEntity);
        }
        return null;
    }
}
