package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;
import gramat.engine.actions.capturing.ValueAssembler;

abstract public class AbstractContainerAccept extends CapturingSubAction<AbstractContainerPress> {

    abstract protected void processContent(CapturingContext context, ValueAssembler assembler);

    public AbstractContainerAccept(AbstractContainerPress origin) {
        super(origin);
    }

    protected void beforeProcessContent(CapturingContext context) {

    }

    @Override
    public final void run(CapturingContext context) {
        beforeProcessContent(context);

        var assembler = context.popAssembler();

        processContent(context, assembler);
    }

}