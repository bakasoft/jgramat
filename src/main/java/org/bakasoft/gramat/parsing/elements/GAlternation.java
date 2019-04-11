package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Alternation;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpressionNC;

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
        if (Arrays.stream(expressions).anyMatch(e -> e.countWildProducers() > 0)) {
            for (GExpression expression : expressions) {
                if (expression.countWildProducers() == 0) {
                    throw new GrammarException("If one option of an alternation has a producer, all other options must be producers too.", expression.location);
                }
            }
        }
    }

    @Override
    public void countWildProducers_r(AtomicInteger count, GControl control) {
        Arrays.stream(expressions).forEach(e -> e.countWildProducers_r(count, control));
    }

    @Override
    public void countWildMutations_r(AtomicInteger count, GControl control) {
        Arrays.stream(expressions).forEach(e -> e.countWildProducers_r(count, control));
    }
}
