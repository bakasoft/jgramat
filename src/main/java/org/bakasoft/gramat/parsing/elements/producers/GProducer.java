package org.bakasoft.gramat.parsing.elements.producers;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;
import org.bakasoft.gramat.parsing.util.SchemaControl;
import org.bakasoft.gramat.schema.SchemaField;
import org.bakasoft.gramat.schema.SchemaType;

import java.util.concurrent.atomic.AtomicInteger;

abstract public class GProducer extends GExpression1C {

  public final String typeName;
  public final boolean appendMode;

  public GProducer(LocationRange location, String typeName, boolean appendMode, GExpression expression) {
    super(location, expression);
    this.typeName = typeName;
    this.appendMode = appendMode;
  }

  public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
   return control.producer(this, entity -> {
      // parent field is null because producers absorb mutations
      if (expression.generateSchemaType(control, entity, null) != null) {
        throw new GrammarException("Nested types are not supported.", expression.location);
      }
    });
  }

  @Override
  public final boolean isOptional_r(GControl control) {
    return expression.isOptional_r(control);
  }

  @Override
  public final void countWildProducers_r(AtomicInteger count, GControl control) {
    count.incrementAndGet(); // this is a wild producer!
  }

  @Override
  public final void countWildMutations_r(AtomicInteger count, GControl control) {
    // nothing to count: producers absorb mutations
  }

  @Override
  public void validate_r(GControl control) {
    if (expression.countWildProducers() > 0) {
      throw new GrammarException("Producers cannot have other producers inside, consider wrapping them with mutations.", expression.location);
    }
  }
}
