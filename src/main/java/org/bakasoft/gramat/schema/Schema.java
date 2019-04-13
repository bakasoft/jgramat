package org.bakasoft.gramat.schema;

import org.bakasoft.gramat.inspect.Inspectable;
import org.bakasoft.gramat.inspect.Inspector;

import java.util.ArrayList;
import java.util.Objects;

public class Schema implements Inspectable {

  private final ArrayList<SchemaEntity> entities;

  public Schema() {
    entities = new ArrayList<>();
  }

  public SchemaEntity mergeEntity(String name) {
    SchemaEntity entity = getEntity(name);

    if (entity != null) {
      return entity;
    }

    return createEntity(name);
  }

  public SchemaEntity createEntity(String name) {
    if (getEntity(name) != null) {
      throw new RuntimeException("type already exists: " + name);
    }

    SchemaEntity entity = new SchemaEntity(this, name);

    entities.add(entity);

    return entity;
  }

  public SchemaEntity getEntity(String name) {
    for (SchemaEntity entity : entities) {
      if (Objects.equals(name, entity.getName())) {
        return entity;
      }
    }

    return null;
  }

  @Override
  public void inspectWith(Inspector output) {
    for (SchemaEntity entity : entities) {
      entity.inspectWith(output);
      output.breakLine();
      output.breakLine();
    }
  }

  public SchemaEntity[] getEntities() {
    return entities.toArray(new SchemaEntity[0]);
  }

  @Override
  public String toString() {
    return inspect();
  }
}
