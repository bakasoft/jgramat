package org.gramat.parsing.elements.producers;

import org.gramat.Gramat;
import org.gramat.GrammarException;
import org.gramat.LocationRange;
import org.gramat.elements.Alternation;
import org.gramat.elements.Element;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GUnion extends GExpression {

  public final String typeName;
  public final GExpression[] expressions;

  public GUnion(LocationRange range, Gramat gramat, String typeName, GExpression[] expressions) {
    super(range, gramat);
    this.typeName = typeName;
    this.expressions = expressions;
  }

  @Override
  public GExpression simplify() {
    GExpression[] simpleExpressions = simplifyAll(expressions);

    if (simpleExpressions.length == 1) {
      return simpleExpressions[0];
    }

    return new GUnion(location, gramat, typeName, simpleExpressions);
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
  public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
    return control.union(this, union -> {
      for (GExpression currentExpr : expressions) {
        SchemaType type = currentExpr.generateSchemaType(control, parentType, parentField);

        if(type == null) {
          throw new GrammarException("All options in the union must be producers.", currentExpr.location);
        }

        union.add(type);
      }
    });
  }
}
