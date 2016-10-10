package com.lanx.app.jndicreator.xml.impl.config;

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.Element;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import com.lanx.app.jndicreator.util.JNDIConstant;
import com.lanx.app.jndicreator.vo.BizEntity;
import com.lanx.app.jndicreator.vo.JNDIEntity;
import com.lanx.app.jndicreator.xml.AbstractCFGLoadParser;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-17
 * Time: 9:32:23
 * To change this template use File | Settings | File Templates.
 */
public class CFGLoadParser extends AbstractCFGLoadParser {
    public CFGLoadParser(String xmlfile) throws DocumentException {
        super(xmlfile);
    }

    public CFGLoadParser(File file) throws DocumentException {
        super(file);
    }

    public List loadJNDIMetaData() {
        List alist = new ArrayList(JNDIConstant.Common.INIT_SIZE);
        List list = doc.selectNodes("/Configuration/database");
        for (int i = 0; i < list.size(); i++) {
            Node node = (Node) list.get(i);
            if (node != null) {
                String driverclass = node.valueOf("@driverClassName");
                if(driverclass != null) {
                    //在获取entity的时候先判断一下数据库类型
                    String localDBType = this.getDBType(driverclass);

                    JNDIEntity jndiEntity = this.setEntity(node, null,localDBType);
                    alist.add(jndiEntity);
                }
            }
        }

        return alist;
    }

    /**
     * 只将即时通讯服务器和文档服务器的connection节点的值保存到了list中
     */
    public BizEntity[] loadConnectionMetaData() {
        BizEntity bizEntity[] = new BizEntity[3];

        List list = doc.selectNodes("/Configuration/connection");
        for (int i = 0; i < list.size(); i++) {
            Node node = (Node) list.get(i);

            String bizurl = node.valueOf("@bizurl");
            String databasename = node.valueOf("@database");

            BizEntity entity = new BizEntity();
            if (bizurl.toLowerCase().indexOf("im.database") != -1) {
                entity.setBizurl(bizurl);
                entity.setDatabaseName(databasename);
                entity.setServerInfo(JNDIConstant.LanxServerInfo.IM_SERVER);

                bizEntity[0] = entity;
            } else if (bizurl.toLowerCase().indexOf("doc.database") != -1) {
                entity.setBizurl(bizurl);
                entity.setDatabaseName(databasename);
                entity.setServerInfo(JNDIConstant.LanxServerInfo.DOC_SERVER);

                bizEntity[1] = entity;
            }else if (bizurl.toLowerCase().indexOf("system.database") != -1) {
                entity.setBizurl(bizurl);
                entity.setDatabaseName(databasename);
                entity.setServerInfo(JNDIConstant.LanxServerInfo.SYSTEM_SERVER);

                bizEntity[2] = entity;
            }
        }
        return bizEntity;
    }

    public List loadConnections() {
        List returnlist = new ArrayList(JNDIConstant.Common.INIT_SIZE);
        List list = doc.selectNodes("/Configuration/connection");
        for (int i = 0; i < list.size(); i++) {
            Node node = (Node) list.get(i);
            String bizurl = node.valueOf("@bizurl");
            String databasename = node.valueOf("@database");

            BizEntity entity = new BizEntity();
            entity.setBizurl(bizurl);
            entity.setDatabaseName(databasename);

            returnlist.add(entity);
        }
        return returnlist;
    }

    public JNDIEntity loadSystemMetaData() {
        List connections = doc.getRootElement().elements("connection");
        for(int i = 0;connections != null && i<connections.size() ;i++) {
            Element ele = (Element)connections.get(i);
            String bizurl = ele.valueOf("@bizurl");

            if(bizurl.toLowerCase().indexOf("system.database") != -1) {
                String jndiname = ele.valueOf("@database");
                return loadMetaDataByName(jndiname,null);
            }
        }
        return null;
    }

    public List loadSystemConnections() {
        List systems = new ArrayList(JNDIConstant.Common.INIT_SIZE);

        List connections = doc.getRootElement().elements("connection");
        for(int i = 0;connections != null && i<connections.size() ;i++) {
            Element ele = (Element)connections.get(i);
            String bizurl = ele.valueOf("@bizurl");

            if(bizurl.toLowerCase().indexOf("system.database") != -1) {
                BizEntity biz = new BizEntity();
                biz.setBizurl(bizurl);
                biz.setDatabaseName(ele.valueOf("@database"));

                systems.add(biz);
            }
        }
        return systems;
    }

    public JNDIEntity loadMetaDataByName(String databaseName, String serverInfo) {
        Node node = doc.selectSingleNode("/Configuration/database[@name='" + databaseName + "']");
        if (node != null) {
            //在获取entity的时候先判断一下数据库类型
            String driverClassName = node.valueOf("@driverClassName");
            return this.setEntity(node, serverInfo,this.getDBType(driverClassName));
        }
        return null;
    }

    private JNDIEntity setEntity(Node node, String serverInfo,String localDBType) {
        String jndiname = node.valueOf("@name");
        String servername = node.valueOf("@servername");
        String databasename = node.valueOf("@databasename");
        String port = node.valueOf("@port");
        String username = node.valueOf("@username");
        String password = node.valueOf("@password");
        String driverClassName = node.valueOf("@driverClassName");
        String properties = node.valueOf("@properties");

        JNDIEntity jndiEntity = new JNDIEntity();
        //dbType = this.getDBType(driverClassName);
        jndiEntity.setDbType(localDBType);

        jndiEntity.setJndiname(jndiname);
        jndiEntity.setServerName(servername);

        if (port == null || "".equals(port)) {
            port = this.getPort(localDBType);
        }
        jndiEntity.setPort(port);

        if(username == null || "".equals(username)) {
            username = this.getUsername(localDBType);
        }
        jndiEntity.setUsername(username);

        if(password == null || "".equals(password)) {
            password = this.getPassword(localDBType);
        }
        jndiEntity.setPassword(password);

        jndiEntity.setProperties(properties);
        jndiEntity.setDatabaseName(databasename);

        if (localDBType.equals(JNDIConstant.Database.MSSQLSERVER)
                && (JNDIConstant.LanxServerInfo.IM_SERVER.equals(serverInfo)
                || JNDIConstant.LanxServerInfo.DOC_SERVER.equals(serverInfo))) {
            jndiEntity.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
        } else
            jndiEntity.setDriverClassName(driverClassName);


        return jndiEntity;
    }

    private String getPort(String localDBType) {
        if (localDBType.equals(JNDIConstant.Database.MSSQLSERVER)) {
            return "1433";
        } else if (localDBType.equals(JNDIConstant.Database.ORACLE)) {
            return "1521";
        } else if (localDBType.equals(JNDIConstant.Database.MYSQL)) {
            return "3306";
        } else if (localDBType.equals(JNDIConstant.Database.DB2)) {
            return "1433";
        } else
            return null;
    }

    private String getUsername(String localDBType) {
        if (localDBType.equals(JNDIConstant.Database.MSSQLSERVER)) {
            return "sa";
        } else if (localDBType.equals(JNDIConstant.Database.ORACLE)) {
            return "system";
        } else if (localDBType.equals(JNDIConstant.Database.MYSQL)) {
            return "root";
        } else if (localDBType.equals(JNDIConstant.Database.DB2)) {
            return "sys";
        } else
            return null;
    }

    private String getPassword(String localDBType) {
        if (localDBType.equals(JNDIConstant.Database.MSSQLSERVER)) {
            return "sa";
        } else if (localDBType.equals(JNDIConstant.Database.ORACLE)) {
            return "system";
        } else if (localDBType.equals(JNDIConstant.Database.MYSQL)) {
            return "root";
        } else if (localDBType.equals(JNDIConstant.Database.DB2)) {
            return "sys";
        } else
            return null;
    }
}
