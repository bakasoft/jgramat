package gramat.parsing;

import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.Machine;
import gramat.epsilon.State;

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
    public State build(Builder builder, State initial) {
        var expression = getExpression();

        if (isRecursive()) {
            var machine = builder.assembler.reuseMachine(name, this, builder, builder.newState());
            var accepted = builder.newState();

            builder.assembler.recursiveHook(machine, initial, accepted, RawAutomataPromise::hook);

            return machine.accepted;
        }
        else {
            return expression.build(builder, initial);
        }
    }

    private static void hook(Machine machine, State initial, State accepted) {
        var lang = machine.language;

        for (var trn : machine.initial.transitions) {
            lang.transition(initial, NStateSet.of(trn.target), trn.symbol); // TODO copy actions
        }

        for (var trn : lang.findTransitionsByTarget(machine.accepted)) {
            lang.transition(NStateSet.of(trn.source), initial, trn.symbol);
        }

        // TODO
        System.out.println("POLLO CONNECT");
    }
}
