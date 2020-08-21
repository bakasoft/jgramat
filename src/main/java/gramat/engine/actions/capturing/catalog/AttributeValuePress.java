package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;
import gramat.engine.actions.capturing.marks.NameMark;
import gramat.tools.Debug;

public class AttributeValuePress extends CapturingSubAction<AttributeNamePress> {

    public AttributeValuePress(AttributeNamePress origin) {
        super(origin);
    }

    @Override
    public final void run(CapturingContext context) {
        var accept = context.dequeue(AttributeNameAccept.class);

        if (accept != null) {
            Debug.log("Execute action: " + accept);

            accept.run(context);
        }

        var mark = context.tryPopMark(origin, NameMark.class);

        if (mark == null) {
            throw new RuntimeException("expected name mark");
        }

        context.pushMark(this, mark);

        context.pushAssembler();
    }

    @Override
    public String getDescription() {
        return "PRESS ATTRIBUTE VALUE";
    }
}
