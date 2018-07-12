package com.jsl.ktv.karaok;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class PropertyManager {

    private static final String propsconfig = "config.properties";

    private static PropertyManager manager = null;
    private static Properties config = null;
    private static Object managerLock = new Object();

    private PropertyManager(Context context) {
        if (config == null)
            config = loadProps(context, propsconfig);
    }

    public static PropertyManager getInstance(Context context) {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new PropertyManager(context);
                }
            }
        }
        return manager;
    }

    private Properties loadProps(Context context, String name) {
        Properties props = new Properties();
        InputStream is = null;
        try {
            File file = new File(context.getFilesDir(), "config.properties");
            is = new FileInputStream(file);
            props.load(is);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
        }
        return props;
    }

    public String getConfigProperty(String name) {
        String value = config.getProperty(name);
        return value == null ? "" : value;
    }

}