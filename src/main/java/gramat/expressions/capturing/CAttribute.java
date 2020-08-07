package gramat.expressions.capturing;

import gramat.engine.actions.capturing.catalog.AttributeStaticPress;
import gramat.engine.actions.capturing.catalog.AttributeStaticRelease;
import gramat.engine.actions.capturing.catalog.AttributeStaticSustain;
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
        var press = new AttributeStaticPress(name);
        var release = new AttributeStaticRelease(name, press);
        var sustain = new AttributeStaticSustain(name, press);

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
