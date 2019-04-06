package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.Map;

public class GFunction extends GCapture {

    public final String name;
    public final String[] arguments;
    public final GElement expression;

    public GFunction(String name, String[] arguments, GElement expression) {
        this.name = name;
        this.arguments = arguments;
        this.expression = expression;
    }

    @Override
    public GElement simplify() {
        return new GFunction(name, arguments, expression.simplify());
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        throw new UnsupportedOperationException("functions are not implemented yet");
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
