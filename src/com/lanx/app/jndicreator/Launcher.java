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
            System.out.println("��������3�����������иó������3������Ϊ��ѡ��");
            System.out.println("ʹ��:args[0] --> �����ļ���·�������� ./config.lanx.xml");
            System.out.println("     args[1] --> Ӧ�÷��������ͣ����� Tomcat �� JBoss3 �� WebLogic8��");
            System.out.println("     args[2] --> Ӧ�÷������ĸ�Ŀ¼������ c:/jakarta-tomcat-5.5.16");
            System.out.println(" --> args[3] --> JDBC������������Ŀ¼(�ò���WebSphereʱΪ������)������ c:/lib");
            System.out.println("     args[4] --> ��ʱͨѶ�������ĸ�Ŀ¼������ c:/jakarta-tomcat-5.5.16");
            System.out.println("     args[5] --> �ĵ��������ĸ�Ŀ¼������ c:/jakarta-tomcat-5.5.16");

            System.exit(1);
        }

        String returns[] = new String[3];
        System.setProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");

        if (args.length >= 3 && args.length <= 6) {
            JNDICreator jndiCreator = null;
            if(args[1].toLowerCase().indexOf("websphere") != -1) {
                if(args.length < 4 ) {
                    System.out.print("Ӧ�÷�����ΪWebSphereʱ������Ҫ4��������");
                    System.exit(-1);
                }
                jndiCreator = JNDIManager.genJNDICreatorInstance(args[0], args[1], args[3]);
            }else {
                jndiCreator = JNDIManager.genJNDICreatorInstance(args[0], args[1]);
            }

            System.out.print("��ʼ����"+args[1]+"��JNDI����...");
            if (jndiCreator.genJNDI(args[2])) {
                returns[0] = JNDIConstant.Common.SUCCESS;
                System.out.println(" ... �ɹ����");
            }else {
                returns[0] = JNDIConstant.Common.FAILURE;
                System.out.println(" ... ʧ�ܣ������������ļ���");
            }

            if (args.length > 4) {
                ConfigCreator configCreator = ConfigManager.genConfigCreatorInstance(args[0]);
                if (args[4] != null && !"".equals(args[4])) {
                    System.out.print("��ʼ���ɼ�ʱͨѶ��������JNDI����...");
                    returns[1] = configCreator.genIMServerConfig(args[4]);

                    if(JNDIConstant.Common.SUCCESS.equals(returns[1])) {
                        System.out.println(" ... �ɹ����");
                    }else {
                        System.out.println(" ... ʧ�ܣ�");
                        System.out.println(returns[1]);
                    }
                }

                if (args[5] != null && !"".equals(args[5])) {
                    System.out.print("��ʼ�����ĵ���������JNDI����...");
                    returns[2] = configCreator.genDocServerConfig(args[5]);

                    if(JNDIConstant.Common.SUCCESS.equals(returns[2])) {
                        System.out.println(" ... �ɹ����");
                    }else {
                        System.out.println(" ... ʧ�ܣ�");
                        System.out.println(returns[2]);
                    }
                }
            }
            return returns;
        } else {
            System.out.println("����������ִ������飡");
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
