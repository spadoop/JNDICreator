package com.lanx.app.jndicreator;

import org.dom4j.DocumentException;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-20
 * Time: 10:13:49
 * To change this template use File | Settings | File Templates.
 */
public interface JNDICreator {
    public boolean genJNDI(String appServerHome) throws DocumentException, IOException;
}
