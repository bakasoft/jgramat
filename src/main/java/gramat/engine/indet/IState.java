package gramat.engine.indet;

import gramat.engine.nodet.NStateList;

public class IState {

    public final String id;
    public final NStateList origin;

    public boolean accepted;

    public IState(String id, NStateList origin) {
        this.id = id;
        this.origin = origin;
        this.accepted = false;
    }

}
