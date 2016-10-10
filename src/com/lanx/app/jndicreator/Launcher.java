package com.lanx.app.jndicreator;

import org.dom4j.DocumentException;

import java.io.IOException;

import com.lanx.app.jndicreator.util.JNDIConstant;

/**
 * 
 */
public class Launcher {
    public static String[] createJNDI(String[] args) throws DocumentException, IOException {
        if (args.length < 3) {
            System.out.println("至少输入3个参数来运行该程序，最后3个参数为可选！");
            System.out.println("使用:args[0] --> 配置文件的路径，例如 ./config.lanx.xml");
            System.out.println("     args[1] --> 应用服务器类型，例如 Tomcat 或 JBoss3 或 WebLogic8等");
            System.out.println("     args[2] --> 应用服务器的根目录，例如 c:/jakarta-tomcat-5.5.16");
            System.out.println(" --> args[3] --> JDBC驱动程序所在目录(该参数WebSphere时为必填项)，例如 c:/lib");
            System.out.println("     args[4] --> 即时通讯服务器的根目录，例如 c:/jakarta-tomcat-5.5.16");
            System.out.println("     args[5] --> 文档服务器的根目录，例如 c:/jakarta-tomcat-5.5.16");

            System.exit(1);
        }

        String returns[] = new String[3];
        System.setProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");

        if (args.length >= 3 && args.length <= 6) {
            JNDICreator jndiCreator = null;
            if(args[1].toLowerCase().indexOf("websphere") != -1) {
                if(args.length < 4 ) {
                    System.out.print("应用服务器为WebSphere时至少需要4个参数！");
                    System.exit(-1);
                }
                jndiCreator = JNDIManager.genJNDICreatorInstance(args[0], args[1], args[3]);
            }else {
                jndiCreator = JNDIManager.genJNDICreatorInstance(args[0], args[1]);
            }

            System.out.print("开始生成"+args[1]+"的JNDI配置...");
            if (jndiCreator.genJNDI(args[2])) {
                returns[0] = JNDIConstant.Common.SUCCESS;
                System.out.println(" ... 成功完成");
            }else {
                returns[0] = JNDIConstant.Common.FAILURE;
                System.out.println(" ... 失败，检查相关输入文件！");
            }

            if (args.length > 4) {
                ConfigCreator configCreator = ConfigManager.genConfigCreatorInstance(args[0]);
                if (args[4] != null && !"".equals(args[4])) {
                    System.out.print("开始生成即时通讯服务器的JNDI配置...");
                    returns[1] = configCreator.genIMServerConfig(args[4]);

                    if(JNDIConstant.Common.SUCCESS.equals(returns[1])) {
                        System.out.println(" ... 成功完成");
                    }else {
                        System.out.println(" ... 失败！");
                        System.out.println(returns[1]);
                    }
                }

                if (args[5] != null && !"".equals(args[5])) {
                    System.out.print("开始生成文档服务器的JNDI配置...");
                    returns[2] = configCreator.genDocServerConfig(args[5]);

                    if(JNDIConstant.Common.SUCCESS.equals(returns[2])) {
                        System.out.println(" ... 成功完成");
                    }else {
                        System.out.println(" ... 失败！");
                        System.out.println(returns[2]);
                    }
                }
            }
            return returns;
        } else {
            System.out.println("输入参数出现错误，请检查！");
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            //Launcher launcher = new Launcher();
            createJNDI(args);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
