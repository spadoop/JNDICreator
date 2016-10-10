package com.lanx.app.jndicreator;

import org.dom4j.DocumentException;

import com.lanx.app.jndicreator.impl.ConfigCreatorImpl;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-23
 * Time: 22:28:26
 * To change this template use File | Settings | File Templates.
 */
public class ConfigManager {
    /**
     * @param confFilename 传入的config.lanx.xml文件
     * @return ConfigCreator
     * @throws org.dom4j.DocumentException
     * */
    public static ConfigCreator genConfigCreatorInstance(String confFilename) throws DocumentException {
        return ConfigCreatorImpl.newInstance(confFilename);
    }
}
