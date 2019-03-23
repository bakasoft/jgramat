package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.ValueElement;

public class ValueData extends ElementData {

    @Override
    public Element _settle(Grammar grammar) {
        return new ValueElement(grammar, this);
    }
}
