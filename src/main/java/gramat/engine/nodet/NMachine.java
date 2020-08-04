package gramat.engine.nodet;

import gramat.engine.AmCode;

import java.util.List;

public class NMachine {

    public final String name;
    public final NState initial;
    public final NState accepted;
    public final List<NGroup> groups;

    public NMachine(String name, NState initial, NState accepted, List<NGroup> groups) {
        this.name = name;
        this.initial = initial;
        this.accepted = accepted;
        this.groups = groups;
    }

    public String getAmCode() {
        var output = new StringBuilder();

        AmCode.writeMachine(output, this);

        return output.toString();
    }
}
