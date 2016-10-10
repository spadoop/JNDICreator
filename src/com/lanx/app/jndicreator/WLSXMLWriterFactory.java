package com.lanx.app.jndicreator;

import org.dom4j.DocumentException;
import com.lanx.app.jndicreator.xml.AbstractWLSXMLWriter;
import com.lanx.app.jndicreator.xml.impl.WLS8XMLWriter;
import com.lanx.app.jndicreator.xml.impl.WLS9XMLWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-29
 * Time: 22:42:02
 * To change this template use File | Settings | File Templates.
 */
public class WLSXMLWriterFactory {
    public static AbstractWLSXMLWriter getWLSXMLWriter(String release,String serverInfo) throws DocumentException {
        if(release.indexOf("8") != -1 ) {
            return new WLS8XMLWriter(serverInfo);
        }else if(release.indexOf("9") != -1 ) {
            return new WLS9XMLWriter(serverInfo);
        }
        return null;
    }
}
