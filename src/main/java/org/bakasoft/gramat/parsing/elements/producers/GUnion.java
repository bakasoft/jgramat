package org.bakasoft.gramat.parsing.elements.producers;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Alternation;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.elements.GAlternation;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.SchemaControl;
import org.bakasoft.gramat.schema.SchemaEntity;
import org.bakasoft.gramat.schema.SchemaField;
import org.bakasoft.gramat.schema.SchemaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GUnion extends GExpression {

  private final GExpression[] expressions;

  public GUnion(LocationRange range, Gramat gramat, GExpression[] expressions) {
    super(range, gramat);
    this.expressions = expressions;
  }

  @Override
  public GExpression simplify() {
    GExpression[] simpleExpressions = simplifyAll(expressions);

    if (simpleExpressions.length == 1) {
      return simpleExpressions[0];
    }

    return new GUnion(location, gramat, simpleExpressions);
  }

  @Override
  public List<GExpression> getChildren() {
    return Arrays.asList(expressions);
  }

  @Override
  public Element compile(Map<String, Element> rules) {
    Element[] elements = compileAll(expressions, rules);

    return new Alternation(elements);
  }

  @Override
  public boolean isOptional_r(GControl control) {
    return expressions.length == 0
        || Arrays.stream(expressions).allMatch(e -> e.isOptional_r(control));
  }

  @Override
  public void validate_r(GControl control) {

  }

  @Override
  public void countWildProducers_r(AtomicInteger count, GControl control) {
    Arrays.stream(expressions).forEach(e -> e.countWildProducers_r(count, control));
  }

  @Override
  public void countWildMutations_r(AtomicInteger count, GControl control) {
    Arrays.stream(expressions).forEach(e -> e.countWildProducers_r(count, control));
  }

  @Override
  public SchemaType generateSchemaType(SchemaControl control, SchemaEntity parentEntity, SchemaField parentField) {
    return control.type(this, result -> {
      for (GExpression currentExpr : expressions) {
        SchemaType currentType = currentExpr.generateSchemaType(control, parentEntity, parentField);

        if(currentType.isEmpty()) {
          throw new GrammarException("All options in the union must be producers.", currentExpr.location);
        }

        result.addType(currentType);
      }
    });
  }
}
