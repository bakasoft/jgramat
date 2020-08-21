package gramat.expressions.capturing;

import gramat.engine.actions.capturing.catalog.AttributeStaticPress;
import gramat.engine.actions.capturing.catalog.AttributeStaticRelease;
import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CAttribute extends Expression {

    private final String name;
    private final Expression content;

    public CAttribute(String name, Expression content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        // the order of action instances matter
        var press = new AttributeStaticPress(name);

        var accepted = content.build(compiler, initial);

        var release = new AttributeStaticRelease(name, press);

        // setup overrides
        release.overrides(press);  // TODO Confirm if this is true (double check recursive expressions)

        TRX2.applyActions(compiler, initial, accepted, press, release);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(content);
    }

}
