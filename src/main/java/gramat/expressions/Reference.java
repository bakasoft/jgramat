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
        var badge = builder.root.badges.newBadge();
        var machine = content.buildOnce(builder, name);
        var accepted = builder.root.newState();

        builder.root.newEmptyTransition(initial, machine.initial, badge);
        builder.root.newEmptyTransition(machine.accepted, accepted, badge);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return getExpression().getChildren();
    }

}
