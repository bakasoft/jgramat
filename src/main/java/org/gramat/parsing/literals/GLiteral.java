package org.gramat.parsing.literals;

import java.util.*;

public interface GLiteral {

  GToken forceToken();
  GArray forceArray();
  GMap forceMap();

  String forceString();
  List<String> forceStringList();
  Map<String, String> forceStringMap();

  static GLiteral forceLiteral(Object value) {
    if (value == null) {
      throw new RuntimeException();
    }
    else if (value instanceof Map) {
      return forceMap(value);
    }
    else if (value instanceof Collection) {
      return forceArray(value);
    }

    return forceToken(value);
  }

  static GToken forceToken(Object value) {
    if (value == null) {
      throw new RuntimeException();
    }
    else if (value instanceof GToken) {
      return (GToken)value;
    }
    else if (value instanceof GArray) {
      return ((GArray)value).forceToken();
    }
    else if (value instanceof GMap) {
      return ((GMap)value).forceToken();
    }

    return new GToken(String.valueOf(value));
  }

  static GArray forceArray(Object value) {
    if (value == null) {
      throw new RuntimeException();
    }
    else if (value instanceof GToken) {
      return ((GToken)value).forceArray();
    }
    else if (value instanceof GArray) {
      return (GArray)value;
    }
    else if (value instanceof GMap) {
      return ((GMap)value).forceArray();
    }

    GArray array = new GArray();

    for (Object item : forceCollection(value)) {
      array.add(forceLiteral(item));
    }

    return array;
  }

  static GMap forceMap(Object value) {
    if (value == null) {
      throw new RuntimeException();
    }
    else if (value instanceof GToken) {
      return ((GToken)value).forceMap();
    }
    else if (value instanceof GArray) {
      return ((GArray)value).forceMap();
    }
    else if (value instanceof GMap) {
      return (GMap)value;
    }

    GMap map = new GMap();

    if (value instanceof Map) {
      for (Map.Entry<?, ?> entry : ((Map<?,?>)value).entrySet()) {
        map.put(
            forceToken(entry.getKey()).forceString(),
            forceLiteral(entry.getValue())
        );
      }
    }
    else {
      throw new RuntimeException();
    }

    return map;
  }

  static Collection<?> forceCollection(Object value) {
    if (value instanceof Collection) {
      return (Collection<?>)value;
    }

    throw new RuntimeException();
  }
}
