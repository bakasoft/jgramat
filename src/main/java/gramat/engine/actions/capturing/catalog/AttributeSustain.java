package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeSustain extends CapturingAction {

    private final String name;
    private final AttributePress begin;

    public AttributeSustain(String name, AttributePress begin) {
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
}
