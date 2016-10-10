package com.lanx.app.jndicreator.util;

public final class JNDIConstant {
    public static final class Common {
        public static final int INIT_SIZE = 1;
        public static final String NEW = "new";
        public static final String UPDATE = "update";
        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

    public static final class Database {
		public static final String ORACLE = "oracle";
        public static final String MSSQLSERVER = "sqlServer";
        public static final String MYSQL = "mysql";
		public static final String DB2 = "db2";
        public static final String HSQLDB = "hsqldb";
    }

    public static final class Server {
        public static final String APACHE_TOMCAT = "Tomcat";
        public static final String WEBLOGIC = "WebLogic";
        public static final String WEBSPHERE = "WebSphere";
        //public static final String WEBSPHERE = "IBM WebSphere Application Server";
        public static final String JBOSS = "JBoss";
    }

    public static final class LanxServerInfo {
        public static final String SYSTEM_SERVER = "systemserver";
        public static final String IM_SERVER = "imserver";
        public static final String DOC_SERVER = "docserver";
    }

    public static final class Encoding {
        public static final String UTF_8 = "UTF-8";
        public static final String UTF_16 = "UTF-16";
        public static final String GB2312 = "GB2312";
        public static final String GBK = "GBK";
        public static final String ISO8859_1 = "ISO8859_1";
    }
}
