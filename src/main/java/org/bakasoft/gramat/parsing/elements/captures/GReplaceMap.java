package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Transformation;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.Map;

public class GReplaceMap extends GCapture {

    public final Map<String, String> replacements;
    public final GElement expression;

    public GReplaceMap(Map<String, String> replacements, GElement expression) {
        this.replacements = replacements;
        this.expression = expression;
    }

    @Override
    public GElement simplify() {
        GElement simpleExpression = expression.simplify();

        if (simpleExpression == null) {
            return null;
        }

        return new GReplaceMap(replacements, expression);
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new Transformation(expression.compile(gramat, compiled), input -> {
            String output = replacements.get(input);

            if (output != null) {
                return output;
            }

            return replacements.get(null);
        });
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
