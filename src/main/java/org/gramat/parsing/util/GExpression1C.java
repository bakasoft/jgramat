package org.gramat.parsing.util;

import org.gramat.LocationRange;
import org.gramat.parsing.GExpression;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

abstract public class GExpression1C extends GExpression {

  abstract public GExpression simplify(GExpression simpleExpression);

  public final GExpression expression;

  public GExpression1C(LocationRange location, GExpression expression) {
    super(location, expression.gramat);
    this.expression = Objects.requireNonNull(expression);
  }

  @Override
  public final GExpression simplify() {
    GExpression simpleExpression = expression.simplify();

    if (simpleExpression == null) {
      return null;
    }

    return simplify(simpleExpression);
  }

  @Override
  public final List<GExpression> getChildren() {
    return Collections.singletonList(expression);
  }

}
