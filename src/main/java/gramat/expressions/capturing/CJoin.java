package gramat.expressions.capturing;

import gramat.engine.actions.Action;
import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CJoin extends Expression {

    private final Expression content;

    public CJoin(Expression content) {
        this.content = content;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        var accepted = content.build(builder, initial);
        var begin = new JoinBegin();
        var commit = new JoinCommit(begin);
        var sustain = new JoinSustain(begin);

        // setup overrides
        begin.overrides(sustain);
        commit.overrides(sustain);

        TRX2.applyActions(builder, initial, accepted, begin, commit, sustain);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(content);
    }

    public class JoinBegin extends ValueAction {

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "BEGIN JOIN";
        }
    }

    public class JoinCommit extends ValueAction {

        private final JoinBegin begin;

        public JoinCommit(JoinBegin begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "COMMIT JOIN";
        }
    }

    public class JoinSustain extends ValueAction {

        private final JoinBegin begin;

        public JoinSustain(JoinBegin begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "SUSTAIN JOIN";
        }
    }

    public class JoinRollback extends ValueAction {

        private final JoinBegin begin;

        public JoinRollback(JoinBegin begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "ROLLBACK JOIN";
        }
    }
}
