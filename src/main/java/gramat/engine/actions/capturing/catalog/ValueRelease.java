package gramat.engine.actions.capturing.catalog;

public class ValueRelease extends AbstractValueRelease {

    public ValueRelease(ValuePress origin) {
        super(origin);
    }

    @Override
    public String getDescription() {
        return "RELEASE VALUE";
    }

    @Override
    public final int getOrder() {
        return 3;
    }
}
