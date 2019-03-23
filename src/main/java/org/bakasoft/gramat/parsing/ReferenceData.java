package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Reference;

public class ReferenceData extends ExpressionData {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Element _settle(Grammar grammar) {
        return new Reference(grammar, this);
    }
}
