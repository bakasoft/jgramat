package gramat.actions;

import gramat.eval.Context;

public class CancelAction extends Action {

    private final int trxID;

    public CancelAction(int trxID) {
        this.trxID = trxID;
    }

    @Override
    public void run(Context context) {
        var badge = context.heap.peek();

        context.manager.cancel(trxID, badge);
    }

    @Override
    public String toString() {
        return String.format("cancel(%s)", trxID);
    }

}
