package gramat.engine.nodet;

import gramat.engine.AmCode;

public class NMachine {

    public final String name;
    public final NState initial;
    public final NState accepted;

    public boolean used;

    public NMachine(String name, NState initial, NState accepted) {
        this.name = name;
        this.initial = initial;
        this.accepted = accepted;
    }

    public String getAmCode() {
        var output = new StringBuilder();

        AmCode.writeMachine(output, this);

        return output.toString();
    }
}
