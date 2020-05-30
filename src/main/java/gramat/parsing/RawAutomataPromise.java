package gramat.parsing;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;

import java.util.List;

public class RawAutomataPromise extends RawAutomaton {

    private final Parser parser;
    private final String name;

    private RawAutomaton cache;

    public RawAutomataPromise(Parser parser, String name) {
        this.parser = parser;
        this.name = name;
    }

    public RawAutomaton getExpression() {
        if (cache == null) {
            cache = parser.findExpression(name).collapse();
        }
        return cache;
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public List<RawAutomaton> getChildren() {
        return getExpression().getChildren();
    }

    @Override
    public NSegment build(NContext context) {
        var expression = getExpression();

        if (isRecursive()) {
            var initial = context.language.state();
            var accepted = context.language.state();
            var lang = context.language;
            var machine = context.getMachine(name);

            if (machine == null) {
                machine = context.createMachine(name, expression);
            }

            var automaton = machine;

            context.postBuildHook(() -> {
                for (var trn : automaton.initial.getTransitions()) {
                    lang.transition(initial, NStateSet.of(trn.target), trn.symbol); // TODO copy actions
                }

                for (var trn : lang.findTransitionsByTarget(automaton.accepted)) {
                    lang.transition(NStateSet.of(trn.source), initial, trn.symbol);
                }

                // TODO
                System.out.println("POLLO CONNECT");
            });

            return context.segment(initial, accepted);
        }

        return expression.build(context);
    }
}
