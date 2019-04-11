package org.bakasoft.gramat.parsing.util;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.GExpression;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract public class GExpression0C extends GExpression {

  public GExpression0C(LocationRange location, Gramat gramat) {
    super(location, gramat);
  }

  @Override
  public final List<GExpression> getChildren() {
    return Collections.emptyList();
  }

}
