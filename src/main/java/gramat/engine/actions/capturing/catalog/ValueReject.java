package gramat.engine.actions.capturing.catalog;

public class ValueReject extends AbstractValueReject {

    public ValueReject(ValuePress origin, int beginPosition) {
        super(origin, beginPosition);
    }

    @Override
    public String getDescription() {
        return "VALUE REJECT";
    }

    @Override
    public final int getOrder() {
        return 4;
    }
}
