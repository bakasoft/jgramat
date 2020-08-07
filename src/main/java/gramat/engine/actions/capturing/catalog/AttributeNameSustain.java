package gramat.engine.actions.capturing.catalog;

public class AttributeNameSustain extends AbstractContainerSustain {

    public AttributeNameSustain(AttributeNamePress origin) {
        super(origin);
    }

    @Override
    public String getDescription() {
        return "SUSTAIN ATTRIBUTE NAME";
    }

    @Override
    public final int getOrder() {
        return 2;
    }
}
