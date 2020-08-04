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
        var begin = new AttributeBegin();
        var commit = new AttributeCommit(begin);
        var sustain = new AttributeSustain(begin);

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

    public class AttributeBegin extends ValueAction {

        @Override
        public void run(ValueRuntime runtime) {
            runtime.pushAssembler();
        }

        @Override
        public String getDescription() {
            return "BEGIN ATTRIBUTE: " + name;
        }

    }

    public class AttributeCommit extends ValueAction {

        private final AttributeBegin begin;

        public AttributeCommit(AttributeBegin begin) {
            this.begin = begin;
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
            return "COMMIT ATTRIBUTE: " + name;
        }
    }

    public class AttributeSustain extends ValueAction {

        private final AttributeBegin begin;

        public AttributeSustain(AttributeBegin begin) {
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

    public class AttributeRollback extends ValueAction {

        private final AttributeBegin begin;

        public AttributeRollback(AttributeBegin begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            runtime.popAssembler();
        }

        @Override
        public String getDescription() {
            return "ROLLBACK ATTRIBUTE: " + name;
        }
    }
}
