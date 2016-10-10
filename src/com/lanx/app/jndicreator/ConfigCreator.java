package com.lanx.app.jndicreator;

import org.dom4j.DocumentException;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-23
 * Time: 22:27:33
 * To change this template use File | Settings | File Templates.
 */
public interface ConfigCreator {
    public String genIMServerConfig(String appServerHome) throws DocumentException, IOException;
    public String genDocServerConfig(String appServerHome) throws DocumentException, IOException;
}
