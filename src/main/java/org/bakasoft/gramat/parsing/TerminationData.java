package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Termination;

public class TerminationData extends ExpressionData {
    @Override
    public Element _settle(Grammar grammar) {
        return new Termination(grammar, this);
    }
}
