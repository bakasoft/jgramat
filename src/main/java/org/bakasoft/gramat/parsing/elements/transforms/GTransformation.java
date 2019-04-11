package org.bakasoft.gramat.parsing.elements.transforms;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;

abstract public class GTransformation extends GExpression1C {

  public GTransformation(LocationRange location, GExpression expression) {
    super(location, expression);
  }

  @Override
  public void validate_r(GControl control) {
    if (expression.hasWildProducers()) {
      throw new GrammarException("Transformations cannot have wild producers.", expression.location);
    }
    else if (expression.hasWildMutations()) {
      throw new GrammarException("Transformations cannot have wild mutations.", expression.location);
    }
  }

  @Override
  public final boolean isOptional_r(GControl control) {
    return expression.isOptional_r(control);
  }

  @Override
  public final boolean hasWildProducers_r(GControl control) {
    return expression.hasWildProducers_r(control);
  }

  @Override
  public final boolean hasWildMutations_r(GControl control) {
    return expression.hasWildMutations_r(control);
  }
}
