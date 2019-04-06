package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.Map;

public class GList extends GCapture {
    public GList(GLiteral[] options, GElement[] arguments) {
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
