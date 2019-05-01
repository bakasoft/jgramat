package org.gramat.parsing.elements.producers;

import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.ListElement;
import org.gramat.parsing.GExpression;

import java.util.Map;

public class GList extends GProducer {

    public GList(LocationRange location, String typeName, GExpression expression) {
        super(location, typeName, true, expression);
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        return new GList(location, typeName, simpleExpression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        Class<?> type = gramat.getType(typeName);

        return new ListElement(type, expression.compile(compiled));
    }

}
