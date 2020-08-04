package gramat.expressions.capturing;

import gramat.engine.parsers.ValueParser;
import gramat.engine.nodet.NBuilder;
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
    public NState build(NBuilder builder, NState initial) {
        var accepted = content.build(builder, initial);
        var begin = new ValueBegin();
        var commit = new ValueCommit(begin);
        var sustain = new ValueSustain(begin);

        TRX2.applyActions(builder, initial, accepted, begin, commit, sustain);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(content);
    }

    public class ValueBegin extends ValueAction {

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
            return "BEGIN VALUE";
        }
    }

    public class ValueCommit extends ValueAction {

        private final ValueBegin begin;

        public ValueCommit(ValueBegin begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (begin.position != null) {
                var position0 = begin.position;
                var positionF = runtime.input.getPosition();
                var value = runtime.input.extract(position0, positionF);

                runtime.peekAssembler().pushValue(value, parser);

                begin.position = null;
            }
            else {
                System.out.println("Missing start point");
            }
        }

        @Override
        public String getDescription() {
            return "COMMIT VALUE";
        }
    }

    public class ValueSustain extends ValueAction {

        private final ValueBegin begin;

        public ValueSustain(ValueBegin begin) {
            this.begin = begin;
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

    public class ValueRollback extends ValueAction {

        private final ValueBegin begin;

        public ValueRollback(ValueBegin begin) {
            this.begin = begin;
        }

        @Override
        public void run(ValueRuntime runtime) {
            if (begin.position == null) {
                System.out.println("WARNING: canceling before start @ " + this);
            }

            begin.position = null;
        }

        @Override
        public String getDescription() {
            return "ROLLBACK VALUE";
        }
    }
}
