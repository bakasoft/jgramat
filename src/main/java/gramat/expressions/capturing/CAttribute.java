package gramat.expressions.capturing;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CAttribute extends Expression {

    private final String name;
    private final Expression content;

    public CAttribute(String name, Expression content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        var accepted = content.build(builder, initial);
        var press = new AttributePress();
        var release = new AttributeRelease(press);
        var sustain = new AttributeSustain(press);

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

    public class AttributePress extends ValueAction {

        @Override
        public void run(ValueRuntime runtime) {
            runtime.pushAssembler();
        }

        @Override
        public String getDescription() {
            return "PRESS ATTRIBUTE: " + name;
        }

    }

    public class AttributeRelease extends ValueAction {

        private final AttributePress press;

        public AttributeRelease(AttributePress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            var assembler = runtime.popAssembler();

            var value = assembler.popValue();

            assembler.expectEmpty();

            runtime.peekAssembler().setAttribute(name, value);
        }

        @Override
        public String getDescription() {
            return "RELEASE ATTRIBUTE: " + name;
        }
    }

    public class AttributeSustain extends ValueAction {

        private final AttributePress begin;

        public AttributeSustain(AttributePress begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO
        }

        @Override
        public String getDescription() {
            return "SUSTAIN ATTRIBUTE: " + name;
        }
    }

    public class AttributeCancel extends ValueAction {

        private final AttributePress press;

        public AttributeCancel(AttributePress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            runtime.popAssembler();
        }

        @Override
        public String getDescription() {
            return "CANCEL ATTRIBUTE: " + name;
        }
    }
}
