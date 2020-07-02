package gramat.engine.nodet;

import gramat.engine.AmCode;

public class NMachine extends NContainer {

    public final NState initial;
    public final NState accepted;

    public NMachine(NRoot root, NState initial, NState accepted, NStateList states, NTransitionList transitions) {
        super(root, states, transitions);
        this.initial = initial;
        this.accepted = accepted;
    }

    public String getAmCode() {
        var output = new StringBuilder();

        AmCode.writeMachine(output, this);

        return output.toString();
    }
}
