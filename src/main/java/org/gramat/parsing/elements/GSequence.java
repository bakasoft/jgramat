package org.gramat.parsing.elements;

import org.gramat.GrammarException;
import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.Sequence;
import org.gramat.Gramat;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.GExpressionNC;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GSequence extends GExpressionNC {

    public GSequence(LocationRange location, Gramat gramat, GExpression[] expressions) {
        super(location, gramat, expressions);
    }

    @Override
    public GExpression simplify(GExpression[] simpleExpressions) {
        if (simpleExpressions.length == 1) {
            return simpleExpressions[0];
        }

        // flatten sequences
        ArrayList<GExpression> flattened = new ArrayList<>();
        for (GExpression e : simpleExpressions) {
            if (e instanceof GSequence) {
                GSequence seq = (GSequence)e;

                Collections.addAll(flattened, seq.expressions);
            }
            else {
                flattened.add(e);
            }
        }

        return new GSequence(location, gramat, flattened.toArray(new GExpression[0]));
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new Sequence(GExpression.compileAll(expressions, compiled));
    }

    @Override
    public void validate_r(GControl control) {
//        boolean hasProducer = false;
//
//        for (GExpression expression : expressions) {
//            if (expression.countWildProducers() > 0) {
//                if (hasProducer) {
//                    throw new GrammarException("There cannot be more than one producer in the same sequence.", expression.location);
//                }
//
//                hasProducer = true;
//            }
//        }
//
//        validate_r(control, expressions);
    }

    @Override
    public void countWildProducers_r(AtomicInteger count, GControl control) {
        Arrays.stream(expressions).forEach(e -> e.countWildProducers_r(count, control));
    }

    @Override
    public void countWildMutations_r(AtomicInteger count, GControl control) {
        Arrays.stream(expressions).forEach(e -> e.countWildMutations_r(count, control));
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return expressions.length == 0
            || Arrays.stream(expressions).allMatch(e -> e.isOptional_r(control));
    }

    @Override
    public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
        SchemaType result = null;

        for (GExpression expression : expressions) {
            SchemaType type = expression.generateSchemaType(control, parentType, parentField);

            if (type != null && result != null) {
                throw new GrammarException("There cannot be more than one producer in the same sequence.", expression.location);
            }

            result = type;
        }

        return result;
    }
}
