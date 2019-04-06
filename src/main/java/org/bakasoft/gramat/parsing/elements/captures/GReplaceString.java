package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Transformation;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.Map;

public class GReplaceString extends GCapture {

    public final String replacement;
    public final GElement expression;

    public GReplaceString(String replacement, GElement expression) {
        this.replacement = replacement;
        this.expression = expression;
    }

    @Override
    public GElement simplify() {
        GElement simpleExpression = expression.simplify();

        if (simpleExpression == null) {
            return null;
        }

        return new GReplaceString(replacement, expression);
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new Transformation(expression.compile(gramat, compiled), input -> replacement);
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        throw new UnsupportedOperationException();
    }
}
