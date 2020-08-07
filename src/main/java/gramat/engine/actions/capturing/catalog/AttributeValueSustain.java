package gramat.engine.actions.capturing.catalog;

public class AttributeValueSustain extends AbstractContainerSustain {

    public AttributeValueSustain(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    public String getDescription() {
        return "SUSTAIN ATTRIBUTE VALUE";
    }

    @Override
    public final int getOrder() {
        return 8;
    }
}
