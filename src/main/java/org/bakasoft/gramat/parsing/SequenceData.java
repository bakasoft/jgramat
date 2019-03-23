package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Sequence;

import java.util.ArrayList;
import java.util.List;

public class SequenceData extends ExpressionData {
    private final ArrayList<ExpressionData> items = new ArrayList<>();

    public List<ExpressionData> getItems() {
        return items;
    }

    @Override
    public Element _settle(Grammar grammar) {
        return new Sequence(grammar, this);
    }
}
