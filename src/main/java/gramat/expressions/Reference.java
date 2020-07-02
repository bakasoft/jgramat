package gramat.expressions;

import gramat.Grammar;
import gramat.engine.nodet.*;

import java.util.List;

public class Reference extends Expression {

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

        if (isRecursive()) {
            if (builder.maker.addRecursiveName(name)) {
                builder.maker.addRecursiveHook(hook(builder.maker));
            }

            NPlaceholder placeholder = builder.maker.makePlaceholder(builder, name, initial);

            return placeholder.accepted;
        }
        else {
            return content.build(builder, initial);
        }
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(getExpression());
    }

    private Runnable hook(NMaker maker) {
        return () -> {
            System.out.println("Recursive hook....");

            var root = maker.root;
            NMachine machine = maker.getMachine(name);

            if (machine == null) {
                var content = getExpression();

                machine = content.machine(new NBuilder(maker), root.newState());

                maker.setMachine(name, machine);
            }

            for (var ph : maker.getPlaceholders(name)) {
                machine.newEmptyTransition(ph.initial, machine.initial);
                machine.newEmptyTransition(machine.accepted, ph.accepted);
            }

            // TODO add badges
        };
    }
}
