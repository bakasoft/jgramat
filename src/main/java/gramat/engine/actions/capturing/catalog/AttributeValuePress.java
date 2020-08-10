package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.tools.Debug;

public class AttributeValuePress extends AbstractContainerPress {

    @Override
    protected Object createInitializator(CapturingContext context) {
        var accept = context.dequeue(AttributeNameAccept.class);

        if (accept != null) {
            Debug.log("Execute action: " + accept);

            accept.run(context);
        }

        var baton = context.dequeue(AttributeNameBaton.class);

        if (baton == null) {
            throw new RuntimeException("expected baton");
        }

        return baton;
    }

    @Override
    protected AbstractContainerReject createReject(Object initializator) {
        var baton = (AttributeNameBaton)initializator;

        return new AttributeValueReject(this, baton.name);
    }

    @Override
    protected AbstractContainerAccept createAccept(AbstractContainerReject reject) {
        return new AttributeValueAccept(this, ((AttributeValueReject)reject).name);
    }

    @Override
    protected Class<? extends AbstractContainerAccept> getAcceptClass() {
        return AttributeValueAccept.class;
    }

    @Override
    protected Class<? extends AbstractContainerReject> getRejectClass() {
        return AttributeValueReject.class;
    }

    @Override
    public String getDescription() {
        return "PRESS ATTRIBUTE VALUE";
    }
}
