package org.gramat.parsing.elements.transforms;

import org.gramat.GrammarException;
import org.gramat.LocationRange;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.GExpression1C;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.concurrent.atomic.AtomicInteger;

abstract public class GTransformation extends GExpression1C {

  public GTransformation(LocationRange location, GExpression expression) {
    super(location, expression);
  }

  @Override
  public void validate_r(GControl control) {
//    if (expression.countWildProducers() > 0) {
//      throw new GrammarException("Transformations cannot have wild producers.", expression.location);
//    }
//    else if (expression.countWildMutations() > 0) {
//      throw new GrammarException("Transformations cannot have wild mutations.", expression.location);
//    }
  }

  @Override
  public final boolean isOptional_r(GControl control) {
    return expression.isOptional_r(control);
  }

  @Override
  public final void countWildProducers_r(AtomicInteger count, GControl control) {
    expression.countWildProducers_r(count, control);
  }

  @Override
  public final void countWildMutations_r(AtomicInteger count, GControl control) {
    expression.countWildMutations_r(count, control);
  }

  @Override
  public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
    if (expression.generateSchemaType(control, parentType, parentField) != null) {
      throw new GrammarException("Transformations cannot contain producers, they are designed only for text.", expression.location);
    }

    return null;
  }
}
