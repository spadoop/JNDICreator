package com.lanx.app.jndicreator.xml.impl;

import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.AbstractWLSXMLWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-29
 * Time: 22:35:42
 * To change this template use File | Settings | File Templates.
 */
public class WLS9XMLWriter extends AbstractWLSXMLWriter {
    public WLS9XMLWriter(File file) throws DocumentException {
        super(file);
    }

    public WLS9XMLWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
    }

    public boolean write(List entitys) throws IOException, DocumentException {
        String configPath = config.substring(0, config.indexOf("config"));
        for (int i = 0; entitys != null && i < entitys.size(); i++) {
            JNDIEntity jndiEntity = (JNDIEntity) entitys.get(i);
            String jndiName = jndiEntity.getJndiname();

            //���config.xml�����jndi���ã�����Ҫ�޸�,ֻ�޸�-jdbc.xml�ļ�
            Element datasourceEle = getExistJNDI(jndiName);
            if (datasourceEle != null) {
                String descriptor = datasourceEle.element("descriptor-file-name").getText();
                String descriptorFilename = descriptor.split("/")[1];
                if (!descriptorFilename.endsWith(".xml"))
                    throw new java.lang.UnsupportedOperationException("Ŀǰ��֧��config.xml���jndi���ã�");

                //ʵ����update
                writeJDBCFile(jndiEntity,configPath,descriptorFilename,JNDIConstant.Common.UPDATE);
            }else{
                String jdbcFileName = getCurrentJDBCFile(jndiName);
                jndiEntity.setWlsJDBCFile(jdbcFileName);

                createServerPatchDocument(jndiEntity);
                //ʵ����create
                writeJDBCFile(jndiEntity,configPath,jdbcFileName,JNDIConstant.Common.NEW);
            }
        }
        writeXMLFile(null);
        return true;
    }

    private void writeJDBCFile(JNDIEntity jndiEntity,String configPath,
                                String descriptorFilename,String operationType) throws DocumentException, IOException {
        StringBuffer filename = new StringBuffer(configPath);
        filename.append("jdbc")
                .append(File.separator)
                .append(descriptorFilename);
        File jndiConfigFile = new File(filename.toString());
        
        //�޸�jdbcĿ¼�µ������ļ�
        WLSJDBCDriverWriter jwriter = new WLSJDBCDriverWriter(jndiConfigFile);
        jwriter.write(jndiEntity,operationType);
    }

    private String getCurrentJDBCFile(String jndiName) {
        StringBuffer sb = new StringBuffer(jndiName);
                sb.append("-")
                .append(System.currentTimeMillis())
                .append("-jdbc.xml");

        return sb.toString();
    }

    private Element getExistJNDI(String jndiName) {
        List datasourceEles = doc.getRootElement().elements("jdbc-system-resource");
        //List datasourceNodes = doc.selectNodes("//jdbc-system-resource/name");
        for (int i = 0; datasourceEles != null && i < datasourceEles.size(); i++) {
            Element datasourceEle = (Element) datasourceEles.get(i);
            String name = datasourceEle.element("name").getText();
            if (jndiName.equals(name)) {
                return datasourceEle;
            }
        }
        return null;
    }

    private void createServerPatchDocument(JNDIEntity jndiEntity) {
        Element domain = doc.getRootElement();//(Element) doc.selectSingleNode("/Domain");
        if (domain != null) {
            Element resource = domain.addElement("jdbc-system-resource");
            resource.addElement("name").setText(jndiEntity.getJndiname());
            resource.addElement("target").setText(getTarget());
            resource.addElement("descriptor-file-name").setText(getFilename(jndiEntity.getWlsJDBCFile()));

        }
    }

    private String getTarget() {
        return doc.getRootElement().element("admin-server-name").getText();
    }

    private String getFilename(String jdbcFileName) {
        StringBuffer filename = new StringBuffer("jdbc/");
        filename.append(jdbcFileName);

        return filename.toString();
    }
}
