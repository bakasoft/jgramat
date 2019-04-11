package org.bakasoft.gramat.parsing.elements.mutations;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.ValueElement;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;

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
    if (valueExpression.countWildMutations() > 0) {
      throw new GrammarException("Property values cannot have other mutations inside.", valueExpression.location);
    }
    else if (valueExpression.countWildProducers() >= 2) {
      throw new GrammarException("Property values must have only one producer inside.", valueExpression.location);
    }
  }
}
