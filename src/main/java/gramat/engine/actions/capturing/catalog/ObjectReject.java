package gramat.engine.actions.capturing.catalog;

public class ObjectReject extends AbstractContainerReject {

    public ObjectReject(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    public String getDescription() {
        return "REJECT OBJECT";
    }
}
