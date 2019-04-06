package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Transformation;
import org.bakasoft.gramat.parsing.elements.GElement;

import java.util.Map;

public class GIgnore extends GCapture {

    public final GElement expression;

    public GIgnore(GElement expression) {
        this.expression = expression;
    }

    @Override
    public GElement simplify() {
        return new GIgnore(expression.simplify());
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new Transformation(expression.compile(gramat, compiled), input -> "");
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
