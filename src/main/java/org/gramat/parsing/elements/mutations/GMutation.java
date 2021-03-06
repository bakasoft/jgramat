package org.gramat.parsing.elements.mutations;

import org.gramat.Gramat;
import org.gramat.GrammarException;
import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.ValueElement;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

abstract public class GMutation extends GExpression {

  public GMutation(LocationRange location, Gramat gramat) {
    super(location, gramat);
  }

  @Override
  public final void countWildProducers_r(AtomicInteger count, GControl control) {
    // nothing to count: properties absorb producers
  }

  @Override
  public final void countWildMutations_r(AtomicInteger count, GControl control) {
    count.incrementAndGet(); // this is a wild mutation!
  }

  static Element compileValue(GExpression valueExpression, Map<String, Element> compiled) {
    Element cValue = valueExpression.compile(compiled);

    if (valueExpression.countWildProducers() == 0) {

      return new ValueElement(null, cValue);
    }

    return cValue;
  }

  static void validateValueExpression(GExpression valueExpression) {
//    if (valueExpression.countWildMutations() > 0) {
//      throw new GrammarException("Property values cannot have other mutations inside.", valueExpression.location);
//    }
//    else if (valueExpression.countWildProducers() >= 2) {
//      throw new GrammarException("Property values must have only one producer inside.", valueExpression.location);
//    }
  }

  static SchemaType generateSchemaType(String propertyName, LocationRange location, GExpression expression, boolean appendMode, SchemaType parentType, SchemaField parentField, SchemaControl control) {
    if (parentType == null) {
      throw new GrammarException("Properties must be inside a type.", location);
    }
    else if (parentField != null) {
      throw new GrammarException("Nested properties are not supported.", location);
    }

    SchemaField field = parentType.mergeProperty(propertyName);
    SchemaType type = expression.generateSchemaType(control, parentType, field);

    if (appendMode) {
      field.setList(true);
    }

    field.setType(type);

    return null; // empty type
  }
}
