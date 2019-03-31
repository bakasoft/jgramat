package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.elements.Alternation;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.GElement;
import org.bakasoft.gramat.Gramat;

import java.util.Map;
import java.util.Objects;

public class GAlternation extends GElement {

    public final GElement[] expressions;

    public GAlternation(GElement[] expressions) {
        this.expressions = Objects.requireNonNull(expressions);
    }

    @Override
    public GElement simplify() {
        if (expressions.length == 0) {
            return new GNop();
        }
        else if (expressions.length == 1) {
            return expressions[0].simplify();
        }

        return this;
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        return areAllPlain(expressions, gramat);
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        for (GElement expression : expressions) {
            if (!expression.isOptional(gramat)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        Element[] elements = compileAll(expressions, gramat, compiled);

        return new Alternation(elements);
    }
}
