package org.gramat.parsing.literals;

import java.util.*;
import java.util.stream.Collectors;

public class GMap extends HashMap<String, GLiteral> implements GLiteral {

  public GMap() {}

  @Override
  public GToken forceToken() {
    if (size() != 1) {
      throw new RuntimeException("can't convert non-singleton map to token");
    }

    GLiteral literal = get(null);

    if (literal == null) {
      throw new RuntimeException();
    }

    return literal.forceToken();
  }

  @Override
  public GArray forceArray() {
    return new GArray(values());
  }

  @Override
  public GMap forceMap() {
    return this;
  }

  @Override
  public String forceString() {
    return forceToken().forceString();
  }

  @Override
  public List<String> forceStringList() {
    return values().stream()
        .filter(Objects::nonNull)
        .map(GLiteral::forceString)
        .collect(Collectors.toList());
  }

  @Override
  public Map<String, String> forceStringMap() {
    Map<String, String> map = new HashMap<>();
    for (Map.Entry<String, GLiteral> entry : entrySet()) {
      if (entry.getValue() != null) {
        map.put(entry.getKey(), entry.getValue().forceString());
      }
    }
    return map;
  }
}
