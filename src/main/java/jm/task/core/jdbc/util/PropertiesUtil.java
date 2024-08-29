package jm.task.core.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {
    
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value != null && value.startsWith("${") && value.endsWith("}")) {
            String envKey = value.substring(2, value.length() - 1);
            return System.getenv(envKey);
        }
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream is = PropertiesUtil.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            PROPERTIES.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
