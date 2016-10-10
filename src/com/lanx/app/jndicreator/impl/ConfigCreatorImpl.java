package com.lanx.app.jndicreator.impl;

import com.lanx.app.jndicreator.AbstractConfigCreator;
import com.lanx.app.jndicreator.ConfigCreator;
import com.lanx.app.jndicreator.props.impl.DOCPropsWriter;
import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.vo.Entity;
import com.lanx.app.jndicreator.xml.impl.IMXMLWriter;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-24
 * Time: 9:03:28
 * To change this template use File | Settings | File Templates.
 */
public class ConfigCreatorImpl extends AbstractConfigCreator {
    protected ConfigCreatorImpl(String confFilename) throws DocumentException {
        super(confFilename);
    }

    private String getAppServerConfFile(String serverInfo,String appServerHome) {
        if(JNDIConstant.LanxServerInfo.IM_SERVER.equals(serverInfo)) {
            return appServerHome + "/conf/wildfire.xml";
        }else if(JNDIConstant.LanxServerInfo.DOC_SERVER.equals(serverInfo)) {
            if(appServerHome.toLowerCase().indexOf("jboss") != -1) {
                return appServerHome + "/server/default/deploy/TransFile.war/WEB-INF/classes/alfresco/repository.properties";
            }else if(appServerHome.toLowerCase().indexOf("tomcat") != -1) {
                return appServerHome + "/webapps/TransFile/WEB-INF/classes/alfresco/repository.properties";
            }else
                return null;
        }else
            return null;
    }

    public static ConfigCreator newInstance(String confFilename) throws DocumentException {
        return new ConfigCreatorImpl(confFilename);
    }

    public String genIMServerConfig(String appServerHome) throws DocumentException, IOException {
        if(bizEntity == null) {
            return "��config.lanx.xml�ļ���δ���ּ�ʱͨѶ��������������ýڵ㣡";
        }

        String serverInfo = "";
        String databaseName = "";
        if(bizEntity[0] != null) {
            serverInfo = bizEntity[0].getServerInfo();
            databaseName = bizEntity[0].getDatabaseName();
        }else if(bizEntity[2] != null) {
            serverInfo = bizEntity[2].getServerInfo();
            databaseName = bizEntity[2].getDatabaseName();
        }

        if("".equals(serverInfo) || "".equals(databaseName)){
            throw new java.lang.UnsupportedOperationException("system.database��im.databaseͬʱ�������ڣ����������ļ���");
        }

        if(serverInfo.equals(JNDIConstant.LanxServerInfo.IM_SERVER)) {
            File file = new File(getAppServerConfFile(serverInfo,appServerHome));
            if(!file.exists()) {
                return "��ʱͨѶ�������������ļ�wildfire.xml�����ڣ����飡";
            }

            writer = new IMXMLWriter(file);
            Entity entity = loadParser.loadMetaDataByName(databaseName,serverInfo);

            writer.write(entity);
            return JNDIConstant.Common.SUCCESS;
        }else
            return JNDIConstant.Common.SUCCESS;
    }

    public String genDocServerConfig(String appServerHome) throws DocumentException, IOException {
        if(bizEntity == null) {
            return "��config.lanx.xml�ļ���δ�����ĵ���������������ýڵ㣡";
        }

        String serverInfo = "";
        String databaseName = "";
        if(bizEntity[1] != null) {
            serverInfo = bizEntity[1].getServerInfo();
            databaseName = bizEntity[1].getDatabaseName();
        }else if(bizEntity[2] != null) {
            serverInfo = bizEntity[2].getServerInfo();
            databaseName = bizEntity[2].getDatabaseName();
        }

        if("".equals(serverInfo) || "".equals(databaseName)){
            throw new java.lang.UnsupportedOperationException("system.database��doc.databaseͬʱ�������ڣ����������ļ���");
        }

        if(serverInfo.equals(JNDIConstant.LanxServerInfo.DOC_SERVER)) {
            String repository = getAppServerConfFile(serverInfo,appServerHome);
            if(repository == null) {
                return "�ĵ��������������ļ�repository.properties�����ڣ����飡";
            }

            File file = new File(repository);
            if(!file.exists()) {
                return "�ĵ��������������ļ�repository.properties�����ڣ����飡";
            }

            jndiWriter = new DOCPropsWriter(repository);
            Entity entity = loadParser.loadMetaDataByName(databaseName,serverInfo);

            jndiWriter.write(entity);
            return JNDIConstant.Common.SUCCESS;
        }else
            return JNDIConstant.Common.SUCCESS;
    }
}
