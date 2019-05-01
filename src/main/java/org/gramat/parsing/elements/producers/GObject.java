package org.gramat.parsing.elements.producers;

import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.ObjectElement;
import org.gramat.parsing.GExpression;

import java.util.*;

public class GObject extends GProducer {

    public GObject(LocationRange location, String typeName, GExpression expression) {
        super(location, typeName, false, expression);
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        return new GObject(location, typeName, simpleExpression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        Element element = expression.compile(compiled);

        Class<?> type = gramat.getType(typeName);

        return new ObjectElement(type, element);
    }
}
