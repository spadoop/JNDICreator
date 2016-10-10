package com.lanx.app.jndicreator;

import org.dom4j.DocumentException;

import java.util.List;
import java.io.IOException;

import com.lanx.app.jndicreator.vo.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-24
 * Time: 10:41:40
 * To change this template use File | Settings | File Templates.
 */
public interface JNDIWriter {  
    public boolean write(Entity entity) throws IOException;
    public boolean write(List entitys) throws IOException, DocumentException;
}
