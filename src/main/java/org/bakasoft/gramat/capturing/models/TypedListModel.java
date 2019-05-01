package org.bakasoft.gramat.capturing.models;

import org.bakasoft.gramat.capturing.ObjectModel;
import org.bakasoft.polysynth.schemas.ArraySchema;

public class TypedListModel implements ObjectModel {

  private final ArraySchema schema;
  private final Object instance;

  public TypedListModel(ArraySchema schema) {
    this.schema = schema;
    this.instance = schema.createEmpty();
  }

  @Override
  public Object getInstance() {
    return instance;
  }

  @Override
  public void setValue(String name, Object value) {
    throw new RuntimeException("Can't set properties to a list");
  }

  @Override
  public void addValue(String name, Object value) {
    throw new RuntimeException("Can't set properties to a list");
  }

  @Override
  public void addValue(Object value) {
    schema.add(instance, value);
  }
}
