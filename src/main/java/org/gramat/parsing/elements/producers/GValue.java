package org.gramat.parsing.elements.producers;

import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.ValueElement;
import org.gramat.parsing.GExpression;

import java.util.Map;
import java.util.function.Function;

public class GValue extends GProducer {

    public GValue(LocationRange location, String typeName, GExpression expression) {
        super(location, typeName, false, expression);
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        return new GValue(location, typeName, simpleExpression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        Element element = expression.compile(compiled);

        Function<String, ?> converter = gramat.getParser(typeName);

        return new ValueElement(converter, element);
    }

}
