package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;

public class ObjectPress extends AbstractContainerPress {

    @Override
    protected Object createInitializator(CapturingContext context) {
        return null;
    }

    @Override
    protected AbstractContainerAccept createAccept(AbstractContainerReject reject) {
        return new ObjectAccept(this);
    }

    @Override
    protected AbstractContainerReject createReject(Object initializator) {
        return new ObjectReject(this);
    }

    @Override
    protected Class<? extends AbstractContainerAccept> getAcceptClass() {
        return ObjectAccept.class;
    }

    @Override
    protected Class<? extends AbstractContainerReject> getRejectClass() {
        return ObjectReject.class;
    }

    @Override
    public String getDescription() {
        return "PRESS OBJECT";
    }

}
