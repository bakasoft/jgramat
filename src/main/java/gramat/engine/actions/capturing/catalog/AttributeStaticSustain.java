package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeStaticSustain extends CapturingAction {

    private final String name;
    private final AttributeStaticPress begin;

    public AttributeStaticSustain(String name, AttributeStaticPress begin) {
        this.name = name;
        this.begin = begin;
    }

    @Override
    public void run(CapturingContext context) {
        // TODO
    }

    @Override
    public String getDescription() {
        return "SUSTAIN ATTRIBUTE: " + name;
    }

    @Override
    public final int getOrder() {
        return 2;
    }
}
