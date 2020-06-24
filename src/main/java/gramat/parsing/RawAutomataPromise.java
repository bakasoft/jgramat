package gramat.parsing;

import gramat.automata.ndfa.*;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
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
    public NSegment build(NContext context) {
        var expression = getExpression();

        if (isRecursive()) {
            var initial = context.language.state();
            var accepted = context.language.state();
//            var machine = context.getMachine(name);
//
//            if (machine == null) {
//                machine = context.createMachine(name, expression);
//            }
//
//            context.recursiveHook(machine, initial, accepted, RawAutomataPromise::hook);

            return context.segment(initial, accepted);
        }

        return expression.build(context);
    }

    @Override
    public State build(Builder builder, State initial) {
        var expression = getExpression();

        if (isRecursive()) {
            var machine = builder.assembler.reuseMachine(name, this, builder, initial);

            // TODO fix recursion
            // context.recursiveHook(machine, initial, accepted, RawAutomataPromise::hook);

            return machine.accepted;
        }
        else {
            return expression.build(builder, initial);
        }
    }

    private static void hook(NMachine machine, NState initial, NState accepted) {
        var lang = machine.language;

        for (var trn : machine.initial.getTransitions()) {
            lang.transition(initial, NStateSet.of(trn.target), trn.symbol); // TODO copy actions
        }

        for (var trn : lang.findTransitionsByTarget(machine.accepted)) {
            lang.transition(NStateSet.of(trn.source), initial, trn.symbol);
        }

        // TODO
        System.out.println("POLLO CONNECT");
    }
}
