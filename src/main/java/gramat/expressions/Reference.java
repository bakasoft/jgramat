package gramat.expressions;

import gramat.Grammar;
import gramat.engine.symbols.Symbol;
import gramat.engine.nodet.*;

import java.util.HashSet;
import java.util.List;

public class Reference extends Expression {

    private static int idCount = 0;

    private final Grammar grammar;
    private final String name;

    private Expression cache;

    public Reference(Grammar grammar, String name) {
        this.grammar = grammar;
        this.name = name;
    }

    public Expression getExpression() {
        if (cache == null) {
            cache = grammar.findExpression(name);
        }
        return cache;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        var content = getExpression();

        if (!content.isRecursive()) {
            return content.build(builder, initial);
        }

        var machine = content.buildOnce(builder, name);

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
        return List.of(getExpression());
    }

}
