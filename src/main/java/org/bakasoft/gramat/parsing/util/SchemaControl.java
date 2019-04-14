package org.bakasoft.gramat.parsing.util;

import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.elements.GReference;
import org.bakasoft.gramat.parsing.elements.producers.GProducer;
import org.bakasoft.gramat.parsing.elements.producers.GUnion;
import org.bakasoft.gramat.schema.*;

import java.util.HashMap;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SchemaControl {

  private final Schema schema;
  private final HashMap<GExpression, SchemaType> typeMap;
  private final Stack<GExpression> typeStack;

  public SchemaControl(Schema schema) {
    this.schema = schema;
    this.typeMap = new HashMap<>();
    this.typeStack = new Stack<>();
  }

  public SchemaType union(GUnion expression, Consumer<SchemaType> initializer) {
    return type(expression, initializer, () -> {
      String name = expression.typeName != null ? expression.typeName
          : "Union" + Integer.toHexString(expression.hashCode());

      return schema.mergeType(name);
    });
  }

  public SchemaType producer(GProducer expression, Consumer<SchemaType> initializer) {
    return type(expression, initializer, () -> {
      String name = (expression.typeName != null ? expression.typeName
          : "Type" + Integer.toHexString(expression.hashCode()));
      SchemaType type = schema.mergeType(name);

      if (expression.appendMode) {
        type.setList(true);
      }

      return type;
    });
  }

  public SchemaType reference(GReference reference, SchemaType parentType, SchemaField parentField) {
    GExpression expression = reference.gramat.findExpression(reference.ruleName);

    if (typeStack.contains(expression)) {
      if (!typeMap.containsKey(expression)) {
        throw new RuntimeException("it must be initialized!");
      }

      return typeMap.get(expression);
    }

    return expression.generateSchemaType(this, parentType, parentField);
  }

  private SchemaType type(GExpression expression, Consumer<SchemaType> initializer, Supplier<SchemaType> generator) {
    SchemaType type;

    if (typeMap.containsKey(expression)) {
      type = typeMap.get(expression);
    }
    else {
      type = generator.get();

      typeMap.put(expression, type);
    }

    if (typeStack.contains(expression)) {
      return type;
    }

    typeStack.push(expression);
    initializer.accept(type);
    typeStack.pop();

    return type;
  }

}
