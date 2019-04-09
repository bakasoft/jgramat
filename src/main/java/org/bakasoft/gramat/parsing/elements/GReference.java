package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Reference;
import org.bakasoft.gramat.Gramat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GReference extends GElement {

    public final String ruleName;

    public GReference(String ruleName) {
        this.ruleName = Objects.requireNonNull(ruleName);
    }

    @Override
    public List<GElement> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public GElement simplify() {
        return this;
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new Reference(ruleName);
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        return gramat.findRule(ruleName).expression.isPlain(gramat);
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        return gramat.findRule(ruleName).expression.isOptional(gramat);
    }
}
