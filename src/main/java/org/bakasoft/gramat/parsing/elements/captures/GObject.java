package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.TypeElement;
import org.bakasoft.gramat.elements.ValueElement;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class GObject extends GCapture {

    public final String typeName;
    public final GElement expression;

    public GObject(String typeName, GElement expression) {
        this.typeName = typeName;
        this.expression = Objects.requireNonNull(expression);
    }

    public GObject(GLiteral[] options, GElement[] arguments) {
        this.typeName = getOptionalString(options);
        this.expression = getSingleExpression(arguments);
    }

    @Override
    public GElement simplify() {
        GElement simplified = expression.simplify();

        if (simplified == null) {
            return null;
        }

        return new GObject(typeName, simplified);
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        // object conditions are not plain by definition
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
