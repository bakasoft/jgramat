package org.bakasoft.gramat.parsing.elements.producers;

import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.ObjectElement;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;

import java.util.*;

public class GObject extends GProducer {

    public final String typeName;

    public GObject(LocationRange location, String typeName, GExpression expression) {
        super(location, expression);
        this.typeName = typeName;
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
