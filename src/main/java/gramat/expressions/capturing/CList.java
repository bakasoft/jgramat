package gramat.expressions.capturing;

import gramat.engine.Action;
import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CList extends Expression {

    private final Expression content;
    private final Object typeHint;

    public CList(Expression content, Object typeHint) {
        this.content = content;
        this.typeHint = typeHint;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        var machine = content.machine(builder, initial);
        var begin = new ListBegin();
        var commit = new ListCommit(begin);
        var rollback = new ListRollback(begin);
        builder.maker.addActionHook(TRX.setupActions(machine, begin, commit, rollback));
        return machine.accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(content);
    }

    public class ListBegin extends ValueAction {

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "BEGIN LIST";
        }

    }

    public class ListCommit extends ValueAction {

        private final ListBegin begin;

        public ListCommit(ListBegin begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "COMMIT LIST";
        }
    }

    public class ListRollback extends ValueAction {

        private final ListBegin begin;

        public ListRollback(ListBegin begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "ROLLBACK LIST";
        }
    }
}
