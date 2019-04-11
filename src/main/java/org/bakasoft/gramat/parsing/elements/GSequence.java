package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Sequence;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpressionNC;

import java.util.*;

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
        return new Sequence(compileAll(expressions, compiled));
    }

    @Override
    public void validate_r(GControl control) {
        boolean hasProducer = false;

        for (GExpression expression : expressions) {
            if (expression.hasWildProducers()) {
                if (hasProducer) {
                    throw new GrammarException("There cannot be more than one producer in the same sequence.", expression.location);
                }

                hasProducer = true;
            }
        }

        validate_r(control, expressions);
    }

    @Override
    public boolean hasWildProducers_r(GControl control) {
        return Arrays.stream(expressions).anyMatch(e -> e.hasWildProducers_r(control));
    }

    @Override
    public boolean hasWildMutations_r(GControl control) {
        return Arrays.stream(expressions).anyMatch(e -> e.hasWildMutations_r(control));
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return expressions.length == 0
            || Arrays.stream(expressions).allMatch(e -> e.isOptional_r(control));
    }
}
