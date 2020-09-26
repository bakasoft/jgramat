package gramat.pivot.data;

import gramat.actions.ActionStore;
import gramat.pivot.XTransitionData;

abstract public class TDActionWrapper extends XTransitionData {

    public final ActionStore preActions;

    public final ActionStore postActions;

    public TDActionWrapper() {
        this.preActions = new ActionStore();
        this.postActions = new ActionStore();
    }

}
