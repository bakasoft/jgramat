package gramat.engine.actions.capturing.catalog;

public class AttributeValueRelease extends AbstractContainerRelease {

    public AttributeValueRelease(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    public String getDescription() {
        return "RELEASE ATTRIBUTE VALUE";
    }

    @Override
    public final int getOrder() {
        return 9;
    }
}
