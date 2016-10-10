package com.lanx.app.jndicreator.props;

import com.lanx.app.jndicreator.AbstractJNDIWriter;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-4-24
 * Time: 11:03:32
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDOCWriter extends AbstractJNDIWriter {
    protected String filename = "";

    public AbstractDOCWriter(String afile) {
        this.filename = afile;
    }

    protected Properties parsePropertyFile() throws IOException {
        Properties props = new Properties();
        InputStream propFile = getClass().getResourceAsStream(filename);
        props.load(propFile);

        return props;
    }

    protected Properties parseConfigFile() throws IOException {
        Properties props = new Properties();
        File configFile = new File(filename);
        FileInputStream fin = new FileInputStream(configFile);
        props.load(fin);

        //System.out.println("db.url = "+props.getProperty("db.url"));
        return props;
    }
}
