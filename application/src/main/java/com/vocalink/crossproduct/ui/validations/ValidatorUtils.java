package com.vocalink.crossproduct.ui.validations;

import java.lang.reflect.Field;

public class ValidatorUtils {

  static String tryGetFieldValue(Object obj, String name) {
    return tryGetFieldValue(obj, name, String.class);
  }

  static <T> T tryGetFieldValue(Object obj, String name, Class<T> type) {
    try {
      final Field field = obj.getClass().getDeclaredField(name);
      field.setAccessible(true);

      return type.cast(field.get(obj));
    } catch (NoSuchFieldException | IllegalAccessException| ClassCastException e) {
      return null;
    }
  }

}
