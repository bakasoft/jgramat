package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Termination;
import org.bakasoft.gramat.Gramat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class GTerminator extends GElement {

    @Override
    public GElement simplify() {
        return this;
    }

    @Override
    public List<GElement> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new Termination();
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        return true;
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        return false;
    }
}
