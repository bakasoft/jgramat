package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Transformation;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.Map;
import java.util.function.Function;

public class GTransform extends GCapture {

    public final String name;
    public final GElement expression;

    public GTransform(String name, GElement expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public GElement simplify() {
        GElement simpleExpression = expression.simplify();

        if (simpleExpression == null) {
            return null;
        }

        return new GTransform(name, simpleExpression);
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        if (name == null) {
            throw new RuntimeException("missing transformation name");
        }

        Function<String, String> transformation = gramat.getTransformation(name);

        if (transformation == null) {
            throw new RuntimeException("not implemented transformation: " + name);
        }

        return new Transformation(expression.compile(gramat, compiled), transformation);
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
