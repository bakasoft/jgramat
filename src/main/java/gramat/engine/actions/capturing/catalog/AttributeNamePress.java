package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;

public class AttributeNamePress extends AbstractContainerPress {

    @Override
    protected Object createInitializator(CapturingContext context) {
        return null;
    }

    @Override
    protected AbstractContainerReject createReject(Object initializator) {
        return new AttributeNameReject(this);
    }

    @Override
    protected AbstractContainerAccept createAccept(AbstractContainerReject reject) {
        return new AttributeNameAccept(this);
    }

    @Override
    protected Class<? extends AbstractContainerAccept> getAcceptClass() {
        return AttributeNameAccept.class;
    }

    @Override
    protected Class<? extends AbstractContainerReject> getRejectClass() {
        return AttributeNameReject.class;
    }

    @Override
    public String getDescription() {
        return "PRESS ATTRIBUTE NAME";
    }
}
