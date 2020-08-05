package gramat.expressions.capturing;

import gramat.engine.nodet.NCompiler;
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
    public NState build(NCompiler compiler, NState initial) {
        var accepted = content.build(compiler, initial);
        var press = new ObjectPress();
        var release = new ObjectRelease(press);
        var sustain = new ObjectSustain(press);

        // setup overrides
        press.overrides(sustain);
        release.overrides(sustain);

        TRX2.applyActions(compiler, initial, accepted, press, release, sustain);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(content);
    }

    public class ObjectPress extends ValueAction {

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
            return "PRESS OBJECT";
        }

    }

    public class ObjectRelease extends ValueAction {

        private final ObjectPress press;

        public ObjectRelease(ObjectPress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (press.active) {
                var assembler = runtime.popAssembler();

                var object = assembler.getAttributes();  // TODO add types

                runtime.peekAssembler().pushValue(object);

                press.active = false;
            }
        }

        @Override
        public String getDescription() {
            return "RELEASE OBJECT";
        }
    }

    public class ObjectSustain extends ValueAction {

        private final ObjectPress press;

        public ObjectSustain(ObjectPress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO
        }

        @Override
        public String getDescription() {
            return "SUSTAIN OBJECT";
        }
    }

    public class ObjectCancel extends ValueAction {

        private final ObjectPress press;

        public ObjectCancel(ObjectPress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (press.active) {
                runtime.popAssembler();
                press.active = false;
            }
        }

        @Override
        public String getDescription() {
            return "CANCEL OBJECT";
        }
    }
}
