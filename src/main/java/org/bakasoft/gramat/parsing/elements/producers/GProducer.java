package org.bakasoft.gramat.parsing.elements.producers;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;

import java.util.concurrent.atomic.AtomicInteger;

abstract public class GProducer extends GExpression1C {

  public GProducer(LocationRange location, GExpression expression) {
    super(location, expression);
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
