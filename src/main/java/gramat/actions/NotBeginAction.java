package gramat.actions;

import gramat.eval.Context;

import java.util.Objects;

public class NotBeginAction implements Action {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotBeginAction that = (NotBeginAction) o;
        return trxID == that.trxID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trxID);
    }
}
