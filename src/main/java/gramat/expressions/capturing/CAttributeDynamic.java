package gramat.expressions.capturing;

import gramat.engine.Action;
import gramat.engine.nodet.NBuilder;
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
    public NState build(NBuilder builder, NState initial) {
        var nameAccepted = name.build(builder, initial);
        var nameBegin = new AttributeNameBegin();
        var nameCommit = new AttributeNameCommit(nameBegin);
        var nameRollback = new AttributeNameRollback(nameBegin);

        TRX.applyActions(builder, initial, nameAccepted, nameBegin, nameCommit, nameRollback);

        var valueAccepted = value.build(builder, nameAccepted);
        var valueBegin = new AttributeValueBegin();
        var valueCommit = new AttributeValueCommit(valueBegin, nameCommit);
        var valueRollback = new AttributeValueRollback(valueBegin);

        TRX.applyActions(builder, nameAccepted, valueAccepted, valueBegin, valueCommit, valueRollback);

        return valueAccepted;
    }

    public class AttributeNameBegin extends ValueAction {

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
            return "BEGIN ATTRIBUTE NAME";
        }

    }

    public class AttributeNameCommit extends ValueAction {

        private final AttributeNameBegin begin;

        public String name;

        public AttributeNameCommit(AttributeNameBegin begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (begin.active) {
                var assembler = runtime.popAssembler();

                name = assembler.popString();

                assembler.expectEmpty();

                begin.active = false;
            }
        }

        @Override
        public String getDescription() {
            return "COMMIT ATTRIBUTE NAME";
        }
    }

    public class AttributeNameRollback extends ValueAction {

        private final AttributeNameBegin begin;

        public AttributeNameRollback(AttributeNameBegin begin) {
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
            return "ROLLBACK ATTRIBUTE NAME";
        }
    }

    public class AttributeValueBegin extends ValueAction {

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
            return "BEGIN ATTRIBUTE VALUE";
        }

    }

    public class AttributeValueCommit extends ValueAction {

        private final AttributeValueBegin begin;

        private final AttributeNameCommit commitedName;


        public AttributeValueCommit(AttributeValueBegin begin, AttributeNameCommit commitedName) {
            this.begin = begin;
            this.commitedName = commitedName;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (begin.active) {
                if (commitedName.name != null) {
                    var name = commitedName.name;
                    var assembler = runtime.popAssembler();
                    var value = assembler.popValue();

                    assembler.expectEmpty();

                    runtime.peekAssembler().setAttribute(name, value);
                } else {
                    System.out.println("Missing name");
                }

                begin.active = false;
            }
        }

        @Override
        public String getDescription() {
            return "COMMIT ATTRIBUTE VALUE";
        }
    }

    public class AttributeValueRollback extends ValueAction {

        private final AttributeValueBegin begin;

        public AttributeValueRollback(AttributeValueBegin begin) {
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
            return "ROLLBACK ATTRIBUTE VALUE";
        }
    }
}
