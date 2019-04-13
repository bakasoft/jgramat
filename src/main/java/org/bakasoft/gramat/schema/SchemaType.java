package org.bakasoft.gramat.schema;

import org.bakasoft.gramat.inspect.Inspectable;
import org.bakasoft.gramat.inspect.Inspector;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class SchemaType implements Inspectable {

  private final ArrayList<SchemaEntity> entitiesBuffer;
  private final ArrayList<SchemaType> typesBuffer;

  private boolean isList;

  public SchemaType() {
    this.entitiesBuffer = new ArrayList<>();
    this.typesBuffer = new ArrayList<>();
  }

  public boolean addType(SchemaType type) {
    if (typesBuffer.contains(type)) {
      return false;
    }

    return typesBuffer.add(type);
  }

  public boolean addEntity(SchemaEntity value) {
    if (entitiesBuffer.contains(value)) {
      return false;
    }

    return entitiesBuffer.add(value);
  }

  public boolean isList() {
    return isList;
  }

  public void setList(boolean list) {
    isList = list;
  }

  @Override
  public void inspectWith(Inspector output) {
    if (isList) {
      output.write('[');
    }

    List<SchemaEntity> entities = collectEntities();

    if (entities.isEmpty()) {
      output.write('*');
    }
    else {
      for (int i = 0; i < entities.size(); i++) {
        if (i > 0) {
          output.write(" | ");
        }
        entities.get(i).inspectReferenceWith(output);
      }
    }

    if (isList) {
      output.write(']');
    }
  }

  @Override
  public String toString() {
    return inspect();
  }

  // recursive actions //

  private boolean collect(Set<SchemaType> control, Predicate<SchemaEntity> action) {
    for (SchemaEntity entity : entitiesBuffer) {
      if (!action.test(entity)) {
        return false; // short circuit! ⚡
      }
    }

    for (SchemaType type : typesBuffer) {
      if (control.add(type)) {
        if (!type.collect(control, action)) {
          return false;
        }
      }
    }

    return true;
  }

  public List<SchemaEntity> collectEntities() {
    ArrayList<SchemaEntity> result = new ArrayList<>();

    collect(new HashSet<>(), entity -> {
      if (!result.contains(entity)) {
        result.add(entity);
      }
      return true; // don't break circuit
    });

    return result;
  }

  public boolean hasEntities() {
    AtomicBoolean result = new AtomicBoolean(false);

    collect(new HashSet<>(), entity -> {
      result.set(true);
      return false; // make short circuit!
    });

    return result.get();
  }

  public boolean isEmpty() {
    AtomicBoolean result = new AtomicBoolean(true);

    collect(new HashSet<>(), entity -> {
      result.set(false);
      return false; // make short circuit!
    });

    return result.get();
  }
}
