package org.gramat.parsing.elements.transforms;

import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.Transformation;
import org.gramat.parsing.GExpression;

import java.util.Map;

public class GIgnore extends GTransformation {

    public GIgnore(LocationRange location, GExpression expression) {
        super(location, expression);
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        return new GIgnore(location, simpleExpression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new Transformation(expression.compile(compiled), input -> "");
    }
}
