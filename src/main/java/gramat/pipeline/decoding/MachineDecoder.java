package gramat.pipeline.decoding;

import gramat.scheme.Effect;
import gramat.scheme.State;
import gramat.scheme.common.parsers.ParserSource;
import gramat.scheme.data.automata.MachineData;

public class MachineDecoder {

    private final StateDecoder states;
    private final BadgeDecoder badges;
    private final SymbolDecoder symbols;
    private final TransactionDecoder transactions;
    private final ActionDecoder actions;

    public MachineDecoder(ParserSource parsers) {
        this.states = new StateDecoder();
        this.badges = new BadgeDecoder();
        this.symbols = new SymbolDecoder();
        this.transactions = new TransactionDecoder(parsers);
        this.actions = new ActionDecoder(transactions, badges);
    }

    public State build(MachineData machine) {
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