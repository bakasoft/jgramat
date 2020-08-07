package gramat.engine.actions.capturing.catalog;

public class AttributeNameReject extends AbstractContainerReject {

    public AttributeNameReject(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    public String getDescription() {
        return "REJECT ATTRIBUTE NAME";
    }

    @Override
    public final int getOrder() {
        return 4;
    }
}
