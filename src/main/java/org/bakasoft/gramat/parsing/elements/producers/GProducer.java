package org.bakasoft.gramat.parsing.elements.producers;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;

abstract public class GProducer extends GExpression1C {

  public GProducer(LocationRange location, GExpression expression) {
    super(location, expression);
  }

  @Override
  public final boolean isOptional_r(GControl control) {
    return expression.isOptional_r(control);
  }

  @Override
  public final boolean hasWildProducers_r(GControl control) {
    // this is a producer
    return true;
  }

  @Override
  public final boolean hasWildMutations_r(GControl control) {
    // producers absorb mutations
    return false;
  }

  @Override
  public void validate_r(GControl control) {
    if (expression.hasWildProducers()) {
      throw new GrammarException("Producers cannot have other producers inside, consider wrapping them with mutations.", expression.location);
    }
  }
}
