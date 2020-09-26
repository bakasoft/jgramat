package gramat.pivot;

import java.util.ArrayList;
import java.util.List;

public class PAlternation extends PExpression {

    private List<PExpression> options;

    private void move_actions_down() {
        if (options == null || options.isEmpty()) {
            throw new RuntimeException("cannot move actions down");
        }

        var newOptions = new ArrayList<PExpression>();

        for (var option : options) {
            var newOption = option.shallowCopy();

            newOption.addActionsFrom(this);

            newOptions.add(newOption);
        }

        options = newOptions;

        clearActions();
    }

    private void merge_inner_alternations() {
        var newOptions = new ArrayList<PExpression>();

        if (options != null) {
            for (var option : options) {
                if (option instanceof PAlternation) {
                    var alt = (PAlternation) option;

                    alt.move_actions_down();

                    newOptions.addAll(alt.options);
                } else {
                    newOptions.add(option);
                }
            }
        }

        options = newOptions;
    }

    private void make_deterministic() {

    }

    @Override
    public PExpression compile(PContext context) {
        merge_inner_alternations();

        var newOptions = new ArrayList<PExpression>();

        if (options != null) {
            for (var option : options) {
                var newOption = option.compile(context);

                if (newOption != null) {
                    newOptions.add(newOption);
                }
            }
        }

        if (newOptions.isEmpty()) {
            return null;
        }

        // since items changed, they may contain new inner alternations
        merge_inner_alternations();

        make_deterministic();

        options = newOptions;
        return this;
    }

    @Override
    public PAlternation shallowCopy() {
        var copy = new PAlternation();
        copy.options = new ArrayList<>(this.options);
        copy.addActionsFrom(this);
        return copy;
    }

}
