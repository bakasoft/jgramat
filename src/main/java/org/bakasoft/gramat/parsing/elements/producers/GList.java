package org.bakasoft.gramat.parsing.elements.producers;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.ListElement;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;

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
