package gramat.engine.indet;

import gramat.engine.nodet.NStateList;

public class IState {

    public final NStateList origin;

    public boolean accepted;

    public IState(NStateList origin) {
        this.origin = origin;
        this.accepted = false;
    }

    public String computeID() {
        return origin.computeID();
    }

}
