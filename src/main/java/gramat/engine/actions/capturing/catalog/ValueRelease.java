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
        var endPosition = context.input.getPosition();
        var accept = (ValueAccept)context.tryPostpone(p -> p instanceof ValueAccept);

        if (accept != null) {
            // update end position
            accept.endPosition = endPosition;
        }
        else {
            var reject  = (ValueReject)context.present.removeLast(p -> p instanceof ValueReject);

            if (reject == null) {
                throw new RuntimeException("expected reject");
            }

            context.future.enqueue(new ValueAccept(press, parser, reject.beginPosition, endPosition));
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE VALUE";
    }
}
