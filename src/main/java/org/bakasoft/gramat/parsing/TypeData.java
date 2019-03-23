package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.TypeElement;

public class TypeData extends ElementData {

    @Override
    public Element _settle(Grammar grammar) {
        return new TypeElement(grammar, this);
    }

}
