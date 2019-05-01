package org.gramat.parsing.util;

import org.gramat.Gramat;
import org.gramat.LocationRange;
import org.gramat.parsing.GExpression;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

abstract public class GExpressionNC extends GExpression {

  abstract public GExpression simplify(GExpression[] simpleExpressions);

  public final GExpression[] expressions;

  public GExpressionNC(LocationRange location, Gramat gramat, GExpression[] expressions) {
    super(location, gramat);
    this.expressions = Objects.requireNonNull(expressions);
  }

  @Override
  public final GExpression simplify() {
    GExpression[] simpleExpressions = simplifyAll(expressions);

    if (simpleExpressions.length == 0) {
      return null;
    }

    return simplify(simpleExpressions);
  }

  @Override
  public final List<GExpression> getChildren() {
    return Arrays.asList(expressions);
  }

}
