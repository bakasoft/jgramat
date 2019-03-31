package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.TypeElement;
import org.bakasoft.gramat.elements.ValueElement;
import org.bakasoft.gramat.parsing.GElement;
import org.bakasoft.gramat.Gramat;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class GObject extends GElement {

    public final String typeName;
    public final GElement expression;

    public GObject(String typeName, GElement expression) {
        this.typeName = Objects.requireNonNull(typeName);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public GElement simplify() {
        return new GObject(typeName, expression.simplify());
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        // object elements are not plain by definition
        return false;
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        return expression.isOptional(gramat);
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        Element element = expression.compile(gramat, compiled);

        if (expression.isPlain(gramat)) {
            Function<String, ?> converter = gramat.getParser(typeName);

            return new ValueElement(converter, element);
        }

        Class<?> type = gramat.getType(typeName);

        return new TypeElement(type, element);
    }
}
