package gramat.engine.actions.capturing.catalog;

public class AttributeNameRelease extends AbstractContainerRelease {

    public AttributeNameRelease(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    public String getDescription() {
        return "RELEASE ATTRIBUTE NAME";
    }
}
