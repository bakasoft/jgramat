package gramat.engine.actions.capturing.catalog;

import gramat.engine.parsers.ValueParser;

public class ValuePress extends AbstractValuePress {

    private final ValueParser parser;

    public ValuePress(ValueParser parser) {
        this.parser = parser;
    }

    @Override
    protected Class<? extends AbstractValueAccept> getAcceptType() {
        return ValueAccept.class;
    }

    @Override
    protected Class<? extends AbstractValueReject> getRejectType() {
        return ValueReject.class;
    }

    @Override
    protected ValueReject createReject(int beginPosition) {
        return new ValueReject(this, beginPosition);
    }

    @Override
    protected AbstractValueAccept createAccept(int beginPosition) {
        return new ValueAccept(this, beginPosition);
    }

    @Override
    protected ValueParser getParser() {
        return parser;
    }

    @Override
    public String getDescription() {
        return "PRESS VALUE";
    }
}
