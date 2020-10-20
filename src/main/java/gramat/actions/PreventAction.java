package gramat.actions;

import gramat.eval.Context;

public class PreventAction extends Action {

    private final int trxID;

    public PreventAction(int trxID) {
        this.trxID = trxID;
    }

    @Override
    public void run(Context context) {
        var badge = context.heap.peek();

        context.manager.prevent(trxID, badge);
    }

    @Override
    public String toString() {
        return String.format("prevent(%s)", trxID);
    }

}
