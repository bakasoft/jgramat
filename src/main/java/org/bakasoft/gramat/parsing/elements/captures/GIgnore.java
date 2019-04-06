package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.Map;

public class GIgnore extends GCapture {

    public GElement expression;

    public GIgnore(GLiteral[] options, GElement[] arguments) {
        if (options.length != 0) {
            throw new RuntimeException("expected no option");
        }

        this.expression = getSingleExpression(arguments);
    }

    @Override
    public GElement simplify() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        throw new UnsupportedOperationException();
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
