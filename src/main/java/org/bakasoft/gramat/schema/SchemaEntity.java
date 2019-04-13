package org.bakasoft.gramat.schema;

import org.bakasoft.gramat.inspect.Inspectable;
import org.bakasoft.gramat.inspect.Inspector;

import java.util.ArrayList;
import java.util.Objects;

public class SchemaEntity implements Inspectable {

  private final Schema schema;
  private final String name;
  private final ArrayList<SchemaField> properties;

  public SchemaEntity(Schema schema, String name) {
    this.schema = schema;
    this.name = name;
    this.properties = new ArrayList<>();
  }

  public SchemaField mergeProperty(String name) {
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

    SchemaField type = new SchemaField(this, name);

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

  @Override
  public void inspectWith(Inspector output) {
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

  public String getName() {
    return name;
  }

  public void inspectReferenceWith(Inspector output) {
    if (name != null) {
      output.write(name);
    }
    else {
      output.write('*');
    }
  }

  public Schema getSchema() {
    return schema;
  }

  @Override
  public String toString() {
    return inspect();
  }
}
