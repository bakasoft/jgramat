package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Alternation;
import org.bakasoft.gramat.elements.Element;

import java.util.ArrayList;
import java.util.List;

public class AlternationData extends ExpressionData {
    private final ArrayList<ExpressionData> items = new ArrayList<>();

    public List<ExpressionData> getItems() {
        return items;
    }

    @Override
    public Element _settle(Grammar grammar) {
        return new Alternation(grammar, this);
    }
}
