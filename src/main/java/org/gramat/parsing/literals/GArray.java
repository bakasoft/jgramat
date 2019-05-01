package org.gramat.parsing.literals;

import java.util.*;
import java.util.stream.Collectors;

public class GArray extends ArrayList<GLiteral> implements GLiteral {

  public GArray() {}

  public GArray(Collection<? extends GLiteral> c) {
    super(c);
  }

  @Override
  public GToken forceToken() {
    if (size() != 1) {
      throw new RuntimeException();
    }

    return get(0).forceToken();
  }

  @Override
  public GArray forceArray() {
    return this;
  }

  @Override
  public GMap forceMap() {
    GMap map = new GMap();
    for (int i = 0; i < size(); i++) {
      map.put(String.valueOf(i), get(i));
    }
    return map;
  }

  @Override
  public String forceString() {
    return forceToken().forceString();
  }

  @Override
  public List<String> forceStringList() {
    return stream()
        .filter(Objects::nonNull)
        .map(GLiteral::forceString)
        .collect(Collectors.toList());
  }

  @Override
  public Map<String, String> forceStringMap() {
    HashMap<String, String> map = new HashMap<>();
    for (int i = 0; i < size(); i++) {
      if (get(i) != null) {
        map.put(String.valueOf(i), get(i).forceString());
      }
    }
    return map;
  }
}
