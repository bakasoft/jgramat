package gramat.engine.actions.capturing.catalog;

public class ValueSustain extends AbstractValueSustain {

    public ValueSustain(ValuePress press) {
        super(press);
    }

    @Override
    public String getDescription() {
        return "SUSTAIN VALUE";
    }

    @Override
    public final int getOrder() {
        return 2;
    }
}
