package gramat.parsing;

import gramat.automata.ndfa.NContext;
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
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        var expression = getExpression();

        if (isRecursive()) {
            var lang = context.language;
            var am = lang.getAutomaton(name);

            if (am == null) {
                am = NContext.compileAutomaton(lang, name, expression);
            }

            var automaton = am;

            context.postBuildHook(() -> {
                for (var trn : automaton.initial.getTransitions()) {
                    lang.transition(initial, NStateSet.of(trn.target), trn.symbol); // TODO copy actions
                }

                for (var automaton_accepted : automaton.accepted) {
                    for (var trn : lang.findTransitionsByTarget(automaton_accepted)) {
                        lang.transition(NStateSet.of(trn.source), initial, trn.symbol);
                    }
                }

                // TODO
                System.out.println("POLLO CONNECT");
            });
        }
        else {
            expression.build(context, initial, accepted);
        }
    }
}
