package gramat.assemblers;

import gramat.machine.Effect;
import gramat.machine.State;
import gramat.models.automata.*;
import gramat.parsers.ParserSource;

public class MachineAssembler {

    private final StateAssembler states;
    private final BadgeAssembler badges;
    private final SymbolAssembler symbols;
    private final TransactionAssembler transactions;
    private final ActionAssembler actions;

    public MachineAssembler(ParserSource parsers) {
        this.states = new StateAssembler();
        this.badges = new BadgeAssembler();
        this.symbols = new SymbolAssembler();
        this.transactions = new TransactionAssembler(parsers);
        this.actions = new ActionAssembler(transactions, badges);
    }

    public State build(ModelMachine machine) {
        for (var transition : machine.transitions) {
            var source = states.map(transition.source);
            var badge = badges.build(transition.badge);
            var symbol = symbols.build(transition.symbol);
            var target = states.map(transition.target);
            var effect = new Effect(
                    target,
                    actions.build(transition.preActions),
                    actions.build(transition.postActions));

            source.transition.add(badge, symbol, effect);
        }

        return states.find(machine.initial);
    }

}
