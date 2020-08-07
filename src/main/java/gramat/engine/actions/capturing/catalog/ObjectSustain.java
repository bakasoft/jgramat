package gramat.engine.actions.capturing.catalog;

public class ObjectSustain extends AbstractContainerSustain {

    public ObjectSustain(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    public String getDescription() {
        return "SUSTAIN OBJECT";
    }
}
