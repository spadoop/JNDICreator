package com.lanx.app.jndicreator;

import com.lanx.app.jndicreator.impl.JBossJNDICreatorImpl;
import com.lanx.app.jndicreator.impl.Tomcat55XJNDICreatorImpl;
import com.lanx.app.jndicreator.impl.WASJNDICreatorImpl;
import com.lanx.app.jndicreator.impl.WLSJNDICreatorImpl;
import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.util.JNDIUtils;

import org.dom4j.DocumentException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-20
 * Time: 10:26:59
 * To change this template use File | Settings | File Templates.
 */
public class JNDIManager {
    /**
     * @param confFilename 传入的config.lanx.xml文件
     * @param serverType App server的类型，可能是tomcat,jboss等
     * @return JNDICreator
     * @throws DocumentException
     * */
    public static JNDICreator genJNDICreatorInstance(String confFilename,String serverType,String classpath) throws DocumentException {
        String serverInfo = JNDIUtils.getChar(serverType).trim();
        if(serverInfo == null)
            throw new java.lang.IllegalArgumentException("输入的实参不符合要求,请检查实参并修改!");
        String release = JNDIUtils.getNum(serverType);

        if(JNDIConstant.Server.APACHE_TOMCAT.equalsIgnoreCase(serverInfo)) {
            //return Tomcat50XJNDICreatorImpl.newInstance(confFilename);
            return Tomcat55XJNDICreatorImpl.newInstance(confFilename);
        }else if(JNDIConstant.Server.JBOSS.equalsIgnoreCase(serverInfo)) {
            return JBossJNDICreatorImpl.newInstance(confFilename);
        }else if(JNDIConstant.Server.WEBLOGIC.equalsIgnoreCase(serverInfo)) {
            return WLSJNDICreatorImpl.newInstance(confFilename,release);
        }else if(JNDIConstant.Server.WEBSPHERE.equalsIgnoreCase(serverInfo)) {
            return WASJNDICreatorImpl.newInstance(confFilename,classpath);
        }else
            return null;
    }
    
    public static JNDICreator genJNDICreatorInstance(String confFilename,String serverType) throws DocumentException {
        return genJNDICreatorInstance(confFilename,serverType,null);
    }
}
