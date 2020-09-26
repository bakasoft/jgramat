package gramat.pivot;

import java.util.ArrayList;
import java.util.List;

public class PSequence extends PExpression {

    private List<PExpression> items;

    public void append(PExpression expression) {
        items.add(expression);
    }

    private void move_actions_down() {
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("cannot move actions down");
        }
        else if (items.size() == 1) {
            var newItems = new ArrayList<PExpression>();
            var newItem = items.get(0).shallowCopy();

            newItems.add(newItem);

            newItem.addPreActionsFrom(this);
            newItem.addPostActionsFrom(this);

            clearActions();

            items = newItems;
        }
        else {
            var newFirst = items.get(0).shallowCopy();
            var newLast = items.get(items.size() - 1).shallowCopy();

            items.set(0, newFirst);
            items.set(items.size() - 1, newLast);

            newFirst.addPreActionsFrom(this);
            newLast.addPostActionsFrom(this);

            clearActions();
        }
    }

    private void merge_inner_sequences() {
        var newItems = new ArrayList<PExpression>();

        if (items != null) {
            for (var item : items) {
                if (item instanceof PSequence) {
                    var sub = (PSequence) item;

                    sub.move_actions_down();

                    newItems.addAll(sub.items);
                } else {
                    newItems.add(item);
                }
            }
        }

        items = newItems;
    }

    @Override
    public PExpression compile(PContext context) {
        merge_inner_sequences();

        var newItems = new ArrayList<PExpression>();

        if (items != null) {
            for (var item : items) {
                var newItem = item.compile(context);

                if (newItem != null) {
                    newItems.add(newItem);
                }
            }
        }

        if (newItems.isEmpty()) {
            return null;
        }

        // since items changed, they may contain new inner sequences
        merge_inner_sequences();

        items = newItems;
        return this;
    }

    @Override
    public PSequence shallowCopy() {
        var copy = new PSequence();
        copy.items = new ArrayList<>(this.items);
        copy.addActionsFrom(this);
        return copy;
    }

}
