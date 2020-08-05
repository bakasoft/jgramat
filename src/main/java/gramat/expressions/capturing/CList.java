package gramat.expressions.capturing;

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
        var accepted = content.build(builder, initial);
        var press = new ListPress();
        var release = new ListRelease(press);
        var sustain = new ListSustain(press);

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

    public class ListPress extends ValueAction {

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "PRESS LIST";
        }

    }

    public class ListRelease extends ValueAction {

        private final ListPress press;

        public ListRelease(ListPress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "RELEASE LIST";
        }
    }

    public class ListSustain extends ValueAction {

        private final ListPress press;

        public ListSustain(ListPress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "SUSTAIN LIST";
        }
    }

    public class ListCancel extends ValueAction {

        private final ListPress press;

        public ListCancel(ListPress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO implement
        }

        @Override
        public String getDescription() {
            return "CANCEL LIST";
        }
    }
}
