package org.gramat.parsing.util;

import org.gramat.Gramat;
import org.gramat.LocationRange;
import org.gramat.parsing.GExpression;

import java.util.Collections;
import java.util.List;

abstract public class GExpression0C extends GExpression {

  public GExpression0C(LocationRange location, Gramat gramat) {
    super(location, gramat);
  }

  @Override
  public final List<GExpression> getChildren() {
    return Collections.emptyList();
  }

}
