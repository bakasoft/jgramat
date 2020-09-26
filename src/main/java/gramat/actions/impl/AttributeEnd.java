package gramat.actions.impl;

import gramat.Context;
import gramat.actions.Action;

import java.util.Map;
import java.util.Objects;

public class AttributeEnd extends Action {

    private final String defaultName;

    public AttributeEnd(String defaultName) {
        this.defaultName = defaultName;
    }

    @Override
    protected void run_impl(Context context) {
        var assembler = context.popAssembler();

        var name = defaultName;

        if (name == null) {
            name = assembler.popName();
        }

        var value = assembler.popValue();

        assembler.expectEmptyValues();

        context.peekAssembler().setAttribute(name, value);
    }

    @Override
    public boolean stack(Action other) {
        if (other instanceof AttributeEnd) {
            var o = (AttributeEnd)other;

            return Objects.equals(this.defaultName, o.defaultName);
        }
        return false;
    }

    @Override
    protected void fillAttributes(Map<String, String> attributes) {

    }

    @Override
    protected void dispose_impl() {

    }
}
