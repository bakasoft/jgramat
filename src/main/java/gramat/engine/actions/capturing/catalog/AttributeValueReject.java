package gramat.engine.actions.capturing.catalog;

public class AttributeValueReject extends AbstractContainerReject {

    public final String name;

    public AttributeValueReject(AbstractContainerPress origin, String name) {
        super(origin);
        this.name = name;
    }

    @Override
    public String getDescription() {
        return "REJECT ATTRIBUTE VALUE";
    }
}
