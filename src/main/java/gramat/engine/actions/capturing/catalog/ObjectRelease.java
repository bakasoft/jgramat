package gramat.engine.actions.capturing.catalog;

public class ObjectRelease extends AbstractContainerRelease {

    public ObjectRelease(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    public String getDescription() {
        return "RELEASE OBJECT";
    }

    @Override
    public final int getOrder() {
        return 3;
    }
}
