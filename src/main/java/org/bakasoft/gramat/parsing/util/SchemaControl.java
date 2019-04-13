package org.bakasoft.gramat.parsing.util;

import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.schema.Schema;
import org.bakasoft.gramat.schema.SchemaEntity;
import org.bakasoft.gramat.schema.SchemaType;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SchemaControl {

  public final Schema schema; // TODO change to private and pass schema as argument
  private final HashMap<GExpression, SchemaType> typeMap;
  private final HashMap<GExpression, SchemaEntity> entityMap;
  private final Stack<GExpression> typeStack;
  private final Stack<GExpression> entityStack;


  public SchemaControl(Schema schema) {
    this.schema = schema;
    this.typeMap = new HashMap<>();
    this.entityMap = new HashMap<>();
    this.typeStack = new Stack<>();
    this.entityStack = new Stack<>();
  }

  public SchemaType type(GExpression expression, Runnable initializer) {
    return type(expression, SchemaType::new, result -> initializer.run());
  }

  public SchemaType type(GExpression expression, Consumer<SchemaType> initializer) {
    return type(expression, SchemaType::new, initializer);
  }

  public SchemaType type(GExpression expression, Supplier<SchemaType> generator, Runnable initializer) {
    return type(expression, generator, result -> initializer.run());
  }

  public SchemaType type(GExpression expression, Supplier<SchemaType> generator, Consumer<SchemaType> initializer) {
    SchemaType value;

    if (typeMap.containsKey(expression)) {
      value = typeMap.get(expression);
    }
    else {
      value = generator.get();

      typeMap.put(expression, value);
    }

    if (typeStack.contains(expression)) {
      return value;
    }

    typeStack.push(expression);
    initializer.accept(value);
    typeStack.pop();

    return value;
  }

}
