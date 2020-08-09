package gramat.expressions.capturing;

import gramat.engine.actions.capturing.catalog.ObjectPress;
import gramat.engine.actions.capturing.catalog.ObjectRelease;
import gramat.engine.actions.capturing.catalog.ObjectSustain;
import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CObject extends CData {

    private final Expression content;
    private final Object typeHint;

    public CObject(Expression content, Object typeHint) {
        this.content = content;
        this.typeHint = typeHint;
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        var accepted = content.build(compiler, initial);
        var press = new ObjectPress();
        var release = new ObjectRelease(press);
        var sustain = new ObjectSustain(press);

        // setup overrides
        press.overrides(sustain);
        release.overrides(sustain);
        release.overrides(press);  // TODO Confirm if this is true (double check recursive expressions)

        TRX2.applyActions(compiler, initial, accepted, press, release, sustain);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(content);
    }

}
