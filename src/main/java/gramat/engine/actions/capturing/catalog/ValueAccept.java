package gramat.engine.actions.capturing.catalog;

public class ValueAccept extends AbstractValueAccept {

    public ValueAccept(ValuePress origin, int beginPosition) {
        super(origin, beginPosition);
    }

    @Override
    public String getDescription() {
        return "VALUE ACCEPT";
    }
}
