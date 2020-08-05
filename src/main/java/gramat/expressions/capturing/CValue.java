package gramat.expressions.capturing;

import gramat.engine.parsers.ValueParser;
import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CValue extends Expression {

    private final Expression content;
    private final ValueParser parser;

    public CValue(Expression content, ValueParser parser) {
        this.content = content;
        this.parser = parser;
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        var accepted = content.build(compiler, initial);
        var press = new ValuePress();
        var release = new ValueRelease(press);
        var sustain = new ValueSustain(press);

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

    public class ValuePress extends ValueAction {

        public Integer position;

        @Override
        public void run(ValueRuntime runtime) {
            if (position != null) {
                System.out.println("WARNING: re-starting @ " + this);
            }

            position = runtime.input.getPosition();
        }

        @Override
        public String getDescription() {
            return "PRESS VALUE";
        }
    }

    public class ValueRelease extends ValueAction {

        private final ValuePress press;

        public ValueRelease(ValuePress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (press.position != null) {
                var position0 = press.position;
                var positionF = runtime.input.getPosition();
                var value = runtime.input.extract(position0, positionF);

                runtime.peekAssembler().pushValue(value, parser);

                press.position = null;
            }
            else {
                System.out.println("Missing start point");
            }
        }

        @Override
        public String getDescription() {
            return "RELEASE VALUE";
        }
    }

    public class ValueSustain extends ValueAction {

        private final ValuePress press;

        public ValueSustain(ValuePress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO
        }

        @Override
        public String getDescription() {
            return "SUSTAIN VALUE";
        }
    }

    public class ValueCancel extends ValueAction {

        private final ValuePress press;

        public ValueCancel(ValuePress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (press.position == null) {
                System.out.println("WARNING: canceling before start @ " + this);
            }

            press.position = null;
        }

        @Override
        public String getDescription() {
            return "CANCEL VALUE";
        }
    }
}
