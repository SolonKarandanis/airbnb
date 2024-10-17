package com.solon.airbnb.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ApplicationConfigProps {

    private static final Logger log = LoggerFactory.getLogger(ApplicationConfigProps.class);

    protected static Properties APPLICATION_PROPS = initProps("application.properties");

    public static final String CCM_MULTIPART_FILE_TEMP_PATH = APPLICATION_PROPS.getProperty("file.multipart.temp.path");

    protected static Properties initProps(String propertyName) {
        Properties retVal = null;
        try {
            URL propertyFileUrl = ApplicationConfig.class.getResource("/" + propertyName);
            InputStream in = propertyFileUrl.openStream();
            retVal = new Properties();
            retVal.load(in);
            in.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            retVal = null;
        }
        return retVal;
    }

    protected static int getIntegerProperty(String key) {
        String property = APPLICATION_PROPS.getProperty(key);
        if (!StringUtils.hasLength(property)) {
            property = "0";
        }
        return Integer.parseInt(property);
    }

    protected static int getIntegerProperty(String key, int def) {
        String property = APPLICATION_PROPS.getProperty(key);
        if (!StringUtils.hasLength(property)) {
            return def;
        }
        return Integer.parseInt(property);
    }

    protected static long getLongProperty(String key) {
        String property = APPLICATION_PROPS.getProperty(key);
        if (!StringUtils.hasLength(property)) {
            property = "0";
        }
        return Long.parseLong(property);
    }

    protected static Boolean getBooleanProperty(String key, boolean def) {
        String strProp = APPLICATION_PROPS.getProperty(key);
        Boolean prop = Boolean.parseBoolean(strProp);
        return prop;
    }
}
