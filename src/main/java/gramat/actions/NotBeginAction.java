package gramat.actions;

import gramat.eval.Context;

public class NotBeginAction extends Action {

    private final int trxID;

    public NotBeginAction(int trxID) {
        this.trxID = trxID;
    }

    @Override
    public void run(Context context) {
        var badge = context.heap.peek();

        context.manager.notBegin(trxID, badge);
    }

    @Override
    public String toString() {
        return String.format("not-begin(%s)", trxID);
    }

}
