package gramat.expressions.capturing;

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
        var press = new JoinPress();
        var release = new JoinRelease(press);
        var sustain = new JoinSustain(press);

        // setup overrides
        press.overrides(sustain);
        release.overrides(sustain);

        TRX2.applyActions(builder, initial, accepted, press, release, sustain);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(content);
    }

    public class JoinPress extends ValueAction {

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "PRESS JOIN";
        }
    }

    public class JoinRelease extends ValueAction {

        private final JoinPress press;

        public JoinRelease(JoinPress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "RELEASE JOIN";
        }
    }

    public class JoinSustain extends ValueAction {

        private final JoinPress press;

        public JoinSustain(JoinPress press) {
            this.press = press;
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

    public class JoinCancel extends ValueAction {

        private final JoinPress press;

        public JoinCancel(JoinPress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "CANCEL JOIN";
        }
    }
}
