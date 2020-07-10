package gramat.expressions.capturing;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CObject extends Expression {

    private final Expression content;
    private final Object typeHint;

    public CObject(Expression content, Object typeHint) {
        this.content = content;
        this.typeHint = typeHint;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        var accepted = content.build(builder, initial);
        var begin = new ObjectBegin();
        var commit = new ObjectCommit(begin);
        var rollback = new ObjectRollback(begin);

        TRX.applyActions(builder, initial, accepted, begin, commit, rollback);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(content);
    }

    public class ObjectBegin extends ValueAction {

        // TODO add comment why this is active by default
        public boolean active = true;

        @Override
        public void run(ValueRuntime runtime) {
            if (!active) {
                runtime.pushAssembler();
                active = true;
            }
        }

        @Override
        public String getDescription() {
            return "BEGIN OBJECT";
        }

    }

    public class ObjectCommit extends ValueAction {

        private final ObjectBegin begin;

        public ObjectCommit(ObjectBegin begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (begin.active) {
                var assembler = runtime.popAssembler();

                var object = assembler.getAttributes();  // TODO add types

                runtime.peekAssembler().pushValue(object);

                begin.active = false;
            }
        }

        @Override
        public String getDescription() {
            return "COMMIT OBJECT";
        }
    }

    public class ObjectRollback extends ValueAction {

        private final ObjectBegin begin;

        public ObjectRollback(ObjectBegin begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (begin.active) {
                runtime.popAssembler();
                begin.active = false;
            }
        }

        @Override
        public String getDescription() {
            return "ROLLBACK OBJECT";
        }
    }
}
