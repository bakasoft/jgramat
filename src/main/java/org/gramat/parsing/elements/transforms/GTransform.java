package org.gramat.parsing.elements.transforms;

import org.gramat.GrammarException;
import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.Transformation;
import org.gramat.parsing.GExpression;

import java.util.Map;
import java.util.function.Function;

public class GTransform extends GTransformation {

    public final String name;

    public GTransform(LocationRange location, String name, GExpression expression) {
        super(location, expression);
        this.name = name;
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        return new GTransform(location, name, simpleExpression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        if (name == null) {
            throw new GrammarException("Missing transformation name.", location);
        }

        Function<String, String> transformation = gramat.getTransformation(name);

        if (transformation == null) {
            throw new GrammarException("Not implemented transformation: " + name, location);
        }

        return new Transformation(expression.compile(compiled), transformation);
    }

}
