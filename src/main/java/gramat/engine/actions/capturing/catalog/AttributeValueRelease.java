package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;
import gramat.engine.actions.capturing.marks.NameMark;

public class AttributeValueRelease extends CapturingSubAction<AttributeValuePress> {

    public AttributeValueRelease(AttributeValuePress origin) {
        super(origin);
    }

    @Override
    public final void run(CapturingContext context) {
        var accept = context.tryPostpone(AttributeValueAccept.class);

        if (accept == null) {
            var mark = context.tryPopMark(origin, NameMark.class);

            // Mark may not be found when the Press action was not triggered
            if (mark != null) {
                context.enqueue(new AttributeValueAccept(origin, mark.name));
            }
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE ATTRIBUTE VALUE";
    }
}
