package org.bakasoft.gramat.schema;

import org.bakasoft.gramat.inspect.Inspectable;
import org.bakasoft.gramat.inspect.Inspector;

import java.util.*;

public class SchemaType implements Inspectable {

  private final String name;
  private final ArrayList<SchemaType> types;
  private final ArrayList<SchemaField> properties;

  private boolean isList;

  public SchemaType(String name) {
    this.name = Objects.requireNonNull(name);
    this.isList = false;
    this.types = new ArrayList<>();
    this.properties = new ArrayList<>();
  }


  public SchemaField mergeProperty(String name) {
    // TODO should not be union
    SchemaField field = getProperty(name);

    if (field != null) {
      return field;
    }

    return createField(name);
  }

  public SchemaField createField(String name) {
    if (getProperty(name) != null) {
      throw new RuntimeException("property already exists: " + name);
    }

    SchemaField type = new SchemaField(name);

    properties.add(type);

    return type;
  }

  public SchemaField getProperty(String name) {
    for (SchemaField property : properties) {
      if (Objects.equals(name, property.getName())) {
        return property;
      }
    }

    return null;
  }

  public boolean add(SchemaType value) {
    // TODO should not have properties
    if (types.contains(value)) {
      return false;
    }

    return types.add(value);
  }

  @Override
  public void inspectWith(Inspector output) {
    if (types.size() > 0) {
      inspectWithUnion(output);
    }
    else {
      inspectWithEntity(output);
    }
  }

  public void inspectWithEntity(Inspector output) {
    output.write("type ");
    inspectReferenceWith(output);
    output.write(" {");
    output.breakLine();

    output.indent(+1);

    for (SchemaField property : properties) {
      property.inspectWith(output);
      output.breakLine();
    }

    output.indent(-1);

    output.write('}');
  }

  public void inspectWithUnion(Inspector output) {
    output.write("union ");
    inspectReferenceWith(output);
    output.write(" = ");

    if (types.isEmpty()) {
      output.write('*');
    }
    else {
      for (int i = 0; i < types.size(); i++) {
        if (i > 0) {
          output.write(" | ");
        }
        types.get(i).inspectReferenceWith(output);
      }
    }
  }

  public void inspectReferenceWith(Inspector output) {
    output.write(name);

    if (isList) {
      output.write("[]");
    }
  }

  public boolean isList() {
    return isList;
  }

  public void setList(boolean list) {
    isList = list;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return inspect();
  }
}
