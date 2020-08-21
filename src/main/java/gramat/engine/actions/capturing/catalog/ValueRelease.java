package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;
import gramat.engine.actions.capturing.marks.PositionMark;

public class ValueRelease extends CapturingSubAction<ValuePress> {

    public ValueRelease(ValuePress origin) {
        super(origin);
    }

    @Override
    public void run(CapturingContext context) {
        var accept = context.tryPostpone(ValueAccept.class);

        if (accept == null) {
            var mark = context.tryPopMark(origin, PositionMark.class);

            if (mark != null) {
                context.enqueue(new ValueAccept(origin, mark.beginPosition));
            }
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE VALUE";
    }
}
