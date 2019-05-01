package org.bakasoft.gramat.schema;

import org.bakasoft.framboyan.inspect.Inspectable;
import org.bakasoft.framboyan.inspect.Inspector;

import java.util.ArrayList;
import java.util.Objects;

public class Schema implements Inspectable {

  private final ArrayList<SchemaType> types;

  public Schema() {
    types = new ArrayList<>();
  }

  public SchemaType mergeType(String name) {
    SchemaType type = getType(name);

    if (type != null) {
      return type;
    }

    return createType(name);
  }

  public SchemaType createType(String name) {
    if (getType(name) != null) {
      throw new RuntimeException("type already exists: " + name);
    }

    SchemaType type = new SchemaType(name);

    types.add(type);

    return type;
  }

  public SchemaType getType(String name) {
    for (SchemaType type : types) {
      if (Objects.equals(name, type.getName())) {
        return type;
      }
    }

    return null;
  }

  @Override
  public void inspectWith(Inspector output) {
    for (SchemaType type : types) {
      type.inspectWith(output);
      output.breakLine();
      output.breakLine();
    }
  }

  public SchemaType[] getTypes() {
    return types.toArray(new SchemaType[0]);
  }

  @Override
  public String toString() {
    return inspect();
  }
}
