package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;
import gramat.engine.actions.capturing.marks.MarkFactory;
import gramat.tools.Debug;

public class AttributeNameAccept extends CapturingSubAction<AttributeNamePress> {

    public AttributeNameAccept(AttributeNamePress origin) {
        super(origin);
    }

    @Override
    public final void run(CapturingContext context) {
        var accept = context.dequeue(ValueAccept.class);

        if (accept != null) {
            Debug.log("Execute action: " + accept);

            accept.run(context);
        }

        var assembler = context.popAssembler();
        var name = assembler.popString();

        assembler.expectEmpty();

        var mark = MarkFactory.createMark(name);

        context.pushMark(origin, mark);
    }

    @Override
    public String getDescription() {
        return "ACCEPT ATTRIBUTE NAME";
    }
}
