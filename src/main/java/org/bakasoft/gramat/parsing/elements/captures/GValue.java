package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.ObjectElement;
import org.bakasoft.gramat.elements.ValueElement;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.Map;
import java.util.function.Function;

public class GValue extends GCapture {

    public final String typeName;
    public final GElement expression;

    public GValue(String typeName, GElement expression) {
        this.typeName = typeName;
        this.expression = expression;
    }

    @Override
    public GElement simplify() {
        GElement simpleExpression = expression.simplify();

        if (simpleExpression == null) {
            return null;
        }

        return new GValue(typeName, simpleExpression);
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        Element element = expression.compile(gramat, compiled);

        Function<String, ?> converter = gramat.getParser(typeName);

        return new ValueElement(converter, element);
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
