package com.lanx.app.jndicreator.util;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-29
 * Time: 22:14:34
 * To change this template use File | Settings | File Templates.
 */
public class JNDIUtils {
    /**
     * ��һ���ַ����е��������,����ɾ��
     */
    public static String getNumber(String str) {
        str = str.replaceAll("[^\\d]", "");
        return str;
    }

    /**
     * ��һ���ַ����е�����ɾ��
     */
    public static String getCharacter(String str) {

        str = str.replaceAll("[\\d]", "");
        return str;
    }

    /**
     * ��һ���ַ����е��������,����ɾ��
     * jsdk 1.4���°���
     * @param str ������ַ���
     * @return String ��������ַ����е���������
     */
    public static String getNum (String str){
        if(str == null)
            return null;

        char c[] = str.toCharArray();

        StringBuffer temp = new StringBuffer();
        for(int i = 0;c != null && i<c.length;i++) {
            if(c[i] >= '0' && c[i] <= '9') {
                temp.append(c[i]);
            }
        }

        return temp.toString();
    }

    /**
     * ��һ���ַ����е����ֺ�.����ɾ��
     * jsdk 1.4���°���
     * @param str ������ַ���
     * @return String ��������ַ����еĳ����ֺ�.����������ַ�
     */
    public static String getChar (String str){
        if(str == null)
            return null;

        char c[] = str.toCharArray();

        StringBuffer temp = new StringBuffer();
        for(int i = 0;c != null && i<c.length;i++) {
            if(c[i] < '0' || c[i] > '9') {
                if(c[i] != '.')
                    temp.append(c[i]);
            }
        }
        return temp.toString();
    }
}
