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
     * 将一个字符串中的数字输出,其他删除
     */
    public static String getNumber(String str) {
        str = str.replaceAll("[^\\d]", "");
        return str;
    }

    /**
     * 将一个字符串中的数字删除
     */
    public static String getCharacter(String str) {

        str = str.replaceAll("[\\d]", "");
        return str;
    }

    /**
     * 将一个字符串中的数字输出,其他删除
     * jsdk 1.4以下版用
     * @param str 输入的字符串
     * @return String 返回这个字符串中的所有数字
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
     * 将一个字符串中的数字和.符号删除
     * jsdk 1.4以下版用
     * @param str 输入的字符串
     * @return String 返回这个字符串中的除数字和.符号以外的字符
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
