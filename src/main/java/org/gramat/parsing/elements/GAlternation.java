package org.gramat.parsing.elements;

import org.gramat.GrammarException;
import org.gramat.LocationRange;
import org.gramat.elements.Alternation;
import org.gramat.elements.Element;
import org.gramat.Gramat;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.GExpressionNC;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GAlternation extends GExpressionNC {

    public GAlternation(LocationRange location, Gramat gramat, GExpression[] expressions) {
        super(location, gramat, expressions);
    }

    @Override
    public GExpression simplify(GExpression[] simpleExpressions) {
        if (simpleExpressions.length == 1) {
            return simpleExpressions[0];
        }

        return new GAlternation(location, gramat, simpleExpressions);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        Element[] elements = compileAll(expressions, compiled);

        return new Alternation(elements);
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return expressions.length == 0
            || Arrays.stream(expressions).allMatch(e -> e.isOptional_r(control));
    }

    @Override
    public void validate_r(GControl control) {
//        if (Arrays.stream(expressions).anyMatch(e -> e.countWildProducers() > 0)) {
//            for (GExpression expression : expressions) {
//                if (expression.countWildProducers() == 0) {
//                    throw new GrammarException("If one option of an alternation has a producer, all other options must be producers too.", expression.location);
//                }
//            }
//        }
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
        for (GExpression currentExpr : expressions) {
            SchemaType type = currentExpr.generateSchemaType(control, parentType, parentField);

            if (type != null) {
                throw new GrammarException("Alternations can't have producers, use @union instead.", currentExpr.location);
            }
        }

        return null; // empty type
    }
}
