package gramat.expressions.capturing;

import gramat.engine.actions.capturing.catalog.ListPress;
import gramat.engine.actions.capturing.catalog.ListRelease;
import gramat.engine.actions.capturing.catalog.ListSustain;
import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CList extends Expression {

    private final Expression content;
    private final Object typeHint;

    public CList(Expression content, Object typeHint) {
        this.content = content;
        this.typeHint = typeHint;
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        var accepted = content.build(compiler, initial);
        var press = new ListPress();
        var release = new ListRelease(press);
        var sustain = new ListSustain(press);

        // setup overrides
        press.overrides(sustain);
        release.overrides(sustain);

        TRX2.applyActions(compiler, initial, accepted, press, release, sustain);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(content);
    }

}
