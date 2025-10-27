package com.nemo.suite.util;

import java.lang.reflect.Field;

public class ReflectionUtils {
  /**
   * Tries to find a declared field in the given class using multiple possible names.
   * @return the Field if found, or throws NoSuchFieldException if none match.
   */
  public static Field findField(Class<?> clazz, String... possibleNames) throws NoSuchFieldException {
    for (String name : possibleNames) {
      try {
        Field f = clazz.getDeclaredField(name);
        f.setAccessible(true);
        return f;
      } catch (NoSuchFieldException ignored) {
        // try next name
      }
    }
    throw new NoSuchFieldException("None of the field names found: " + String.join(", ", possibleNames));
  }
}
