package com.vocalink.crossproduct.ui.dto;

import java.io.InputStream;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultDtoConfiguration {

  private static final String APPLICATION_PROPERTIES = "application.properties";
  private static final Properties properties = new Properties();

  static {
    try (final InputStream resource = DefaultDtoConfiguration.class.getClassLoader()
        .getResourceAsStream(APPLICATION_PROPERTIES)) {
      properties.load(resource);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to load default dto properties.");
    }
  }

  public static String getDefault(DtoProperties key) {
    return properties.getProperty(key.getProperty());
  }

}
