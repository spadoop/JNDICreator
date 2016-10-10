package com.lanx.app.jndicreator.util;

import java.io.FilenameFilter;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-21
 * Time: 11:34:42
 * To change this template use File | Settings | File Templates.
 */
public class XMLFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return name.endsWith("ds.xml");
    }
}
