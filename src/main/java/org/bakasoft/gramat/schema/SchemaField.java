package org.bakasoft.gramat.schema;

import org.bakasoft.gramat.inspect.Inspectable;
import org.bakasoft.gramat.inspect.Inspector;

public class SchemaField implements Inspectable {

  private final SchemaEntity entity;
  private final String name;

  private SchemaType type;

  private boolean isList;

  public SchemaField(SchemaEntity entity, String name) {
    this.entity = entity;
    this.name = name;
  }

  @Override
  public void inspectWith(Inspector output) {
    if (name != null) {
      output.write(name);
    }
    else {
      output.write('*');
    }
    output.write(": ");
    if (type != null) {
      boolean listMode = isList && !type.isList();

      if (listMode) {
        output.write('[');
      }

      type.inspectWith(output);

      if (listMode) {
        output.write(']');
      }
    }
    else {
      output.write('?');
    }
  }

  public String getName() {
    return name;
  }

  public void setList(boolean isList) {
    this.isList = isList;
  }

  public boolean isList() {
    return isList || (type != null && type.isList());
  }

  public void setType(SchemaType type) {
    this.type = type;
  }

  public SchemaType getType() {
    return type;
  }

  public SchemaEntity getEntity() {
    return entity;
  }

}
