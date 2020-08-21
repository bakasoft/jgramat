package gramat.expressions.capturing;

import gramat.engine.actions.capturing.catalog.ValuePress;
import gramat.engine.actions.capturing.catalog.ValueRelease;
import gramat.engine.parsers.ValueParser;
import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CValue extends CData {

    private final Expression content;
    private final ValueParser parser;

    public CValue(Expression content, ValueParser parser) {
        this.content = content;
        this.parser = parser;
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        // the order of action instances matter
        var press = new ValuePress(parser);
        var accepted = content.build(compiler, initial);
        var release = new ValueRelease(press);

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
