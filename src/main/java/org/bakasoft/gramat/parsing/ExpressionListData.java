package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.elements.Element;

import java.util.ArrayList;
import java.util.List;

public final class ExpressionListData extends ExpressionData {

    public static class EList extends ArrayList<ExpressionData> {
        @Override
        public boolean add(ExpressionData expressionData) {
            if (expressionData == null) {
                throw new NullPointerException();
            }
            return super.add(expressionData);
        }
    }

    private final EList items = new EList();

    public List<ExpressionData> getItems() {
        return items;
    }

    @Override
    public Element _settle(Grammar grammar) {
        if (items.contains(null)) {
            throw new RuntimeException("cannot contain null");
        }

        if (items.isEmpty()) {
            throw new RuntimeException("items can't be empty");
        } else if (items.size() == 1) {
            ExpressionData expressionData = items.get(0);

            return grammar.settle(expressionData);
        }

        ArrayList<SequenceData> result = new ArrayList<>();
        ArrayList<ExpressionData> buffer = new ArrayList<>();

        for (ExpressionData expressionData : items) {
            if (expressionData instanceof AlternationMarkData) {
                if (buffer.isEmpty()) {
                    throw new RuntimeException("must be elements before and after |");
                }

                SequenceData sequenceData = new SequenceData();

                sequenceData.getItems().addAll(buffer);

                buffer.clear();

                result.add(sequenceData);
            } else {
                buffer.add(expressionData);
            }
        }

        if (buffer.isEmpty()) {
            throw new RuntimeException("must be elements after |");
        }

        // TODO optimize alternation

        // flush remaining items
        SequenceData sequenceData = new SequenceData();

        sequenceData.getItems().addAll(buffer);

        result.add(sequenceData);

        AlternationData alternationData = new AlternationData();

        alternationData.getItems().addAll(result);

        return grammar.settle(alternationData);
    }
}
