package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Reference;
import org.bakasoft.gramat.parsing.GElement;
import org.bakasoft.gramat.Gramat;

import java.util.Map;
import java.util.Objects;

public class GReference extends GElement {

    public final String ruleName;

    public GReference(String ruleName) {
        this.ruleName = Objects.requireNonNull(ruleName);
    }

    @Override
    public GElement simplify() {
        return this;
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new Reference(ruleName, () -> {
            Element element = compiled.get(ruleName);

            if (element == null) {
                throw new RuntimeException("rule not found: " + ruleName);
            }

            return element;
        });
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