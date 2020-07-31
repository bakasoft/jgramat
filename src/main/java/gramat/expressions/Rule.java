package gramat.expressions;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;
import gramat.engine.nodet.NTool;
import gramat.engine.symbols.Symbol;

import java.util.HashSet;
import java.util.List;

public class Rule extends Expression {

    private static int idCount = 0;

    public final String name;
    public final Expression expression;

    public Rule(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        if (!expression.isRecursive()) {
            return expression.build(builder, initial);
        }

        var machine = expression.buildOnce(builder, name);

//        if (machine.used) {
//            builder.root.newEmptyTransition(initial, machine.initial);
//
//            return machine.accepted;
//        }
//
//        machine.used = true;

        var accepted = builder.root.newState();

        builder.addRecursiveHook(() -> {
            var id = (++idCount);
            var push = builder.root.checks.push(name + id);
            var pop = builder.root.checks.pop(name + id);
            var control = new HashSet<Symbol>();

            for (var trn : NTool.findOutgoingSymbolTransitions(machine.initial)) {
                if (control.add(trn.getSymbol())) {
                    builder.root.newTransition(initial, trn.target, trn.getSymbol(), push);
                }
            }

            control.clear();

            for (var trn : NTool.findIncomingSymbolTransitions(machine.accepted)) {
                if (control.add(trn.getSymbol())) {
                    builder.root.newTransition(trn.source, accepted, trn.getSymbol(), pop);
                }
            }
        });

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(expression);
    }
}
