package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.parsers.ValueParser;
import gramat.engine.actions.capturing.CapturingContext;

public class ValueRelease extends CapturingAction {

    private final ValueParser parser;
    private final ValuePress press;

    public ValueRelease(ValueParser parser, ValuePress press) {
        this.parser = parser;
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        var accept = context.tryPostpone(ValueAccept.class);

        if (accept == null) {
            var reject  = context.present.removeLast(ValueReject.class);

            if (reject == null) {
                throw new RuntimeException("expected reject");
            }

            context.future.enqueue(new ValueAccept(press, parser, reject.beginPosition));
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE VALUE";
    }
}
