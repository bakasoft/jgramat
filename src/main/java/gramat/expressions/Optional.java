package gramat.expressions;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;

import java.util.List;

public class Optional extends Expression {

    private final Expression content;

    public Optional(Expression content) {
        this.content = content;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        var accepted = content.build(builder, initial);

        builder.newEmptyTransition(initial, accepted);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(content);
    }
}
