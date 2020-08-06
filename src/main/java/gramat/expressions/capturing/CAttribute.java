package gramat.expressions.capturing;

import gramat.engine.actions.capturing.AttributePress;
import gramat.engine.actions.capturing.AttributeRelease;
import gramat.engine.actions.capturing.AttributeSustain;
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
        var accepted = content.build(compiler, initial);
        var press = new AttributePress(name);
        var release = new AttributeRelease(name, press);
        var sustain = new AttributeSustain(name, press);

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
