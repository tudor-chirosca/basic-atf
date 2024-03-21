package org.basic.config.properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class PropertiesManager {
    private static final Logger LOG = LogManager.getLogger(PropertiesManager.class);
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = PropertiesManager.class.getClassLoader().getResourceAsStream("properties/config.properties")) {
            if (input != null) {
                PROPERTIES.load(input);
            } else {
                LOG.error("Unable to find properties file");
            }
        } catch (IOException e) {
            LOG.error("Error loading properties file", e);
        }
    }

    private PropertiesManager() {
    }

    public static String getUsername() {
        return PROPERTIES.getProperty("username");
    }

    public static String getPassword() {
        return PROPERTIES.getProperty("password");
    }

    public static String getDriverPath() {
        return PROPERTIES.getProperty("chromeDriverPath");
    }

    public static String getPageName(String pageName) {
        return PROPERTIES.getProperty(pageName);
    }

    public static Duration checkElementIsDisplayedTimeout() {
        return Duration.ofSeconds(Integer.parseInt(PROPERTIES.getProperty("elementIsDisplayed")));
    }
}
