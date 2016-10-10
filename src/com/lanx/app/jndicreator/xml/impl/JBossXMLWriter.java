package com.lanx.app.jndicreator.xml.impl;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.DocumentHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.util.XMLFilter;
import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.AbstractXMLWriter;

public class JBossXMLWriter extends AbstractXMLWriter {
    private String appServerHome = "";

    public JBossXMLWriter(String xmlfile) throws DocumentException {
        super(xmlfile);
        this.appServerHome = xmlfile;
    }

    public boolean write(List entitys) throws IOException, DocumentException {
        //������config.lanx.xml���ÿ��jndi�����ݿ����Ͳ�һ��
        for (int i = 0; entitys != null && i < entitys.size(); i++) {
            JNDIEntity jndiEntity = (JNDIEntity) entitys.get(i);
            dbType = jndiEntity.getDbType();//this.getDBType(jndiEntity.getDriverClassName());

            if(dbType == null)
                dbType = this.getDBType(jndiEntity.getDriverClassName());

            //���ж����jndi�Ƿ��Ѿ����ڣ��������½������ڵĻ��޸ģ��޸�ʱ�ļ�������ҲҪ�޸�
            //����Ѿ��еĸ����ݿ��jndi�����ļ���������oracle-ds.xml,oracle1-ds.xml,mssql-ds.xml��
            String[] appServerConfFiles = getPreviousAppServerConfFile();
            String appServerConfFile = "";
            //ѭ�������ж��Ƿ��Ѿ��д��ڵ�Jndi
            for (int x = 0; appServerConfFiles != null && x < appServerConfFiles.length; x++) {
                StringBuffer readStr = new StringBuffer(appServerHome);
                readStr.append("/server/default/deploy/")
                        .append(appServerConfFiles[x]);

                doc = reader.read(readStr.toString());

                if (existJNDI(jndiEntity.getJndiname())) {
                    appServerConfFile = readStr.toString();
                    break;
                }
            }

            //������jndi�ļ����ڣ���ô�޸�
            if (!"".equals(appServerConfFile)) {
                file = new File(appServerConfFile);
                updateServerPatchDocument(jndiEntity);
                writeXMLFile(null);

                //����ļ�����Ҫ�޸�,��ʼ�޸��ļ���
                if(needRenameTo(appServerConfFile)) {
                    int current = -1;
                    if(appServerConfFiles != null)
                        current = appServerConfFiles.length;
                    appServerConfFile = getCurrentAppServerConfFile(current);

                    File dest = new File(appServerConfFile);
                    file.renameTo(dest);
                }
            }//���������
            else {
                //�����½�һ���ļ�
                int current = -1;
                if(appServerConfFiles != null)
                    current = appServerConfFiles.length;
                appServerConfFile = getCurrentAppServerConfFile(current);

                file = new File(appServerConfFile);
                doc = DocumentHelper.createDocument();
                createNewDocument(jndiEntity);

                writeXMLFile(null);
            }
        }
        return true;
    }

    private void updateServerPatchDocument(JNDIEntity jndiEntity) {
        String localDbType = this.getDBType(jndiEntity.getDriverClassName());

        doc.selectSingleNode("/datasources/local-tx-datasource/jndi-name").setText(jndiEntity.getJndiname());
        doc.selectSingleNode("/datasources/local-tx-datasource/connection-url").setText(this.getURL(jndiEntity));
        doc.selectSingleNode("/datasources/local-tx-datasource/driver-class").setText(jndiEntity.getDriverClassName());
        doc.selectSingleNode("/datasources/local-tx-datasource/user-name").setText(jndiEntity.getUsername());
        doc.selectSingleNode("/datasources/local-tx-datasource/password").setText(jndiEntity.getPassword());

        Node ext = doc.selectSingleNode("/datasources/local-tx-datasource/exception-sorter-class-name");
        if (ext == null && JNDIConstant.Database.ORACLE.equals(localDbType)) {
            Element datasource = (Element) doc.selectSingleNode("/datasources/local-tx-datasource");
            datasource.addElement("exception-sorter-class-name").setText("org.jboss.resource.adapter.jdbc.vendor.OracleExceptionSorter");
        } else if(ext != null && !JNDIConstant.Database.ORACLE.equals(localDbType))
            doc.remove(ext);
    }

    private void createNewDocument(JNDIEntity jndiEntity) {
        String localDbType = this.getDBType(jndiEntity.getDriverClassName());

        Element datasource = doc.addElement("datasources").addElement("local-tx-datasource");
        datasource.addElement("jndi-name").setText(jndiEntity.getJndiname());
        datasource.addElement("connection-url").setText(this.getURL(jndiEntity));
        datasource.addElement("driver-class").setText(jndiEntity.getDriverClassName());
        datasource.addElement("user-name").setText(jndiEntity.getUsername());
        datasource.addElement("password").setText(jndiEntity.getPassword());

        if (JNDIConstant.Database.ORACLE.equals(localDbType)) {
            datasource.addElement("exception-sorter-class-name").setText("org.jboss.resource.adapter.jdbc.vendor.OracleExceptionSorter");
        }
    }

    private String getCurrentAppServerConfFile(int previous) {
        int count = previous + 1;

        StringBuffer sb = new StringBuffer(appServerHome);
        sb.append("/server/default/deploy/");
        if (JNDIConstant.Database.MSSQLSERVER.equals(dbType)) {
            sb.append("mssql");
            if(count > 0) {
                sb.append(count);
            }
            sb.append("-ds.xml");
        } else if (JNDIConstant.Database.ORACLE.equals(dbType)) {
            sb.append("oracle");
            if(count > 0) {
                sb.append(count);
            }
            sb.append("-ds.xml");
        } else if (JNDIConstant.Database.MYSQL.equals(dbType)) {
            sb.append("mysql");
            if(count > 0) {
                sb.append(count);
            }
            sb.append("-ds.xml");
        } else if (JNDIConstant.Database.DB2.equals(dbType)) {
            sb.append("db2");
            if(count > 0) {
                sb.append(count);
            }
            sb.append("-ds.xml");
        }

        return sb.toString();
    }

    private String[] getPreviousAppServerConfFile() {
        String appServerConfFilePath = appServerHome + "/server/default/deploy";
        File dir = new File(appServerConfFilePath);
        return dir.list(new XMLFilter());
    }
/*
    private int getConfFileCount(String[] dsxmls) {
        int count = 0;
        for (int i = 0; dsxmls != null && i < dsxmls.length; i++) {
            if (JNDIConstant.Database.MSSQLSERVER.equals(dbType)) {
                if (dsxmls[i].indexOf("mssql") != -1)
                    count ++;
            } else if (JNDIConstant.Database.ORACLE.equals(dbType)) {
                if (dsxmls[i].indexOf("oracle") != -1)
                    count ++;
            } else if (JNDIConstant.Database.MYSQL.equals(dbType)) {
                if (dsxmls[i].indexOf("mysql") != -1)
                    count ++;
            } else if (JNDIConstant.Database.DB2.equals(dbType)) {
                if (dsxmls[i].indexOf("db2") != -1)
                    count ++;
            }
        }
        return count;
    }
*/
    private boolean existJNDI(String jndiname) {
        Node node = doc.selectSingleNode("/datasources/local-tx-datasource/jndi-name");
        if (node != null) {
            if (node.getText().equals(jndiname)) {
                return true;
            }
        }
        return false;
    }

    private boolean needRenameTo(String appServerConfFile) {
        if (JNDIConstant.Database.MSSQLSERVER.equals(dbType)) {
            if(appServerConfFile.toLowerCase().indexOf("mssql") == -1)
                return true;
        }else if (JNDIConstant.Database.ORACLE.equals(dbType)) {
            if(appServerConfFile.toLowerCase().indexOf("oracle") == -1)
                return true;
        }else if (JNDIConstant.Database.MYSQL.equals(dbType)) {
            if(appServerConfFile.toLowerCase().indexOf("mysql") == -1)
                return true;
        }else if (JNDIConstant.Database.DB2.equals(dbType)) {
            if(appServerConfFile.toLowerCase().indexOf("db2") == -1)
                return true;
        }
        return false;
    }
}
