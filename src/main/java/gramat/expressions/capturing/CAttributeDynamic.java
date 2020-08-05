package gramat.expressions.capturing;

import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;
import gramat.expressions.Expression;

import java.util.List;

public class CAttributeDynamic extends Expression {

    private final Expression name;
    private final Expression value;

    public CAttributeDynamic(Expression name,Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(name, value);
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        var nameAccepted = name.build(compiler, initial);
        var namePress = new AttributeNamePress();
        var nameRelease = new AttributeNameRelease(namePress);
        var nameSustain = new AttributeNameSustain(namePress);

        // setup overrides
        namePress.overrides(nameSustain);
        nameRelease.overrides(nameSustain);

        TRX2.applyActions(compiler, initial, nameAccepted, namePress, nameRelease, nameSustain);

        var valueAccepted = value.build(compiler, nameAccepted);
        var valuePress = new AttributeValuePress();
        var valueRelease = new AttributeValueRelease(valuePress, nameRelease);
        var valueSustain = new AttributeValueSustain(valuePress);

        // setup overrides
        valuePress.overrides(valueSustain);
        valueRelease.overrides(valueSustain);

        TRX2.applyActions(compiler, nameAccepted, valueAccepted, valuePress, valueRelease, valueSustain);

        return valueAccepted;
    }

    public class AttributeNamePress extends ValueAction {

        public boolean active;

        @Override
        public void run(ValueRuntime runtime) {
            if (!active) {
                runtime.pushAssembler();
                active = true;
            }
        }

        @Override
        public String getDescription() {
            return "PRESS ATTRIBUTE NAME";
        }

    }

    public class AttributeNameRelease extends ValueAction {

        private final AttributeNamePress press;

        public String name;

        public AttributeNameRelease(AttributeNamePress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (press.active) {
                var assembler = runtime.popAssembler();

                name = assembler.popString();

                assembler.expectEmpty();

                press.active = false;
            }
        }

        @Override
        public String getDescription() {
            return "RELEASE ATTRIBUTE NAME";
        }
    }

    public class AttributeNameSustain extends ValueAction {

        private final AttributeNamePress press;

        public AttributeNameSustain(AttributeNamePress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO
        }

        @Override
        public String getDescription() {
            return "SUSTAIN ATTRIBUTE NAME";
        }
    }

    public class AttributeNameCancel extends ValueAction {

        private final AttributeNamePress press;

        public AttributeNameCancel(AttributeNamePress press) {
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
            return "CANCEL ATTRIBUTE NAME";
        }
    }

    public class AttributeValuePress extends ValueAction {

        public boolean active;

        @Override
        public void run(ValueRuntime runtime) {
            if (!active) {
                runtime.pushAssembler();
                active = true;
            }
        }

        @Override
        public String getDescription() {
            return "PRESS ATTRIBUTE VALUE";
        }

    }

    public class AttributeValueRelease extends ValueAction {

        private final AttributeValuePress press;

        private final AttributeNameRelease releasedName;


        public AttributeValueRelease(AttributeValuePress press, AttributeNameRelease releasedName) {
            this.press = press;
            this.releasedName = releasedName;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (press.active) {
                if (releasedName.name != null) {
                    var name = releasedName.name;
                    var assembler = runtime.popAssembler();
                    var value = assembler.popValue();

                    assembler.expectEmpty();

                    runtime.peekAssembler().setAttribute(name, value);
                } else {
                    System.out.println("Missing name");
                }

                press.active = false;
            }
        }

        @Override
        public String getDescription() {
            return "RELEASE ATTRIBUTE VALUE";
        }
    }

    public class AttributeValueSustain extends ValueAction {

        private final AttributeValuePress press;

        public AttributeValueSustain(AttributeValuePress press) {
            this.press = press;
        }

        @Override
        public void run(ValueRuntime runtime) {
            // TODO
        }

        @Override
        public String getDescription() {
            return "SUSTAIN ATTRIBUTE VALUE";
        }
    }

    public class AttributeValueCancel extends ValueAction {

        private final AttributeValuePress press;

        public AttributeValueCancel(AttributeValuePress press) {
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
            return "CANCEL ATTRIBUTE VALUE";
        }
    }
}
