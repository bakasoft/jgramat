package gramat.expressions.capturing;

import gramat.engine.actions.capturing.catalog.ValuePress;
import gramat.engine.actions.capturing.catalog.ValueRelease;
import gramat.engine.actions.capturing.catalog.ValueSustain;
import gramat.engine.parsers.ValueParser;
import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CValue extends Expression {

    private final Expression content;
    private final ValueParser parser;

    public CValue(Expression content, ValueParser parser) {
        this.content = content;
        this.parser = parser;
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        var accepted = content.build(compiler, initial);
        var press = new ValuePress(parser);
        var release = new ValueRelease(press);
        var sustain = new ValueSustain(press);

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
