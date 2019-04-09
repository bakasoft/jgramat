package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.ListElement;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GList extends GCapture {

    public final String typeName;
    public final GElement expression;

    public GList(String typeName, GElement expression) {
        this.typeName = typeName;
        this.expression = expression;
    }

    @Override
    public List<GElement> getChildren() {
        return Arrays.asList(expression);
    }

    @Override
    public GElement simplify() {
        GElement simpleExpression = expression.simplify();

        if (simpleExpression == null) {
            return null;
        }

        return new GList(typeName, simpleExpression);
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        Class<?> type = gramat.getType(typeName);

        return new ListElement(type, expression.compile(gramat, compiled));
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
