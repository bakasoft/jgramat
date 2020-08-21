package gramat.expressions.capturing;

import gramat.engine.actions.capturing.catalog.JoinPress;
import gramat.engine.actions.capturing.catalog.JoinRelease;
import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CJoin extends CData {

    private final Expression content;

    public CJoin(Expression content) {
        this.content = content;
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        // the order of action instances matter
        var press = new JoinPress();
        var accepted = content.build(compiler, initial);
        var release = new JoinRelease(press);

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
