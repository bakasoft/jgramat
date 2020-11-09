package gramat.scheme.core.actions.transactions;

import gramat.eval.transactions.TransactionAdapter;
import gramat.eval.EvalContext;
import gramat.parsers.ValueParser;
import gramat.util.NameMap;

import java.util.Objects;

public class ValueTransaction extends TransactionAdapter {

    public static final String NAME = "value";

    private final ValueParser parser;

    public ValueTransaction(int id, ValueParser parser) {
        super(id);
        this.parser = Objects.requireNonNull(parser);
    }

    public ValueParser getParser() {
        return parser;
    }

    @Override
    public void begin(EvalContext context) {
        var beginPosition = context.tape.getPosition();

        context.pushPosition(beginPosition);
    }

    @Override
    public Runnable prepareCommit(EvalContext context) {
        var end = context.tape.getPosition();

        return () -> {
            var begin = context.popPosition();
            var text = context.tape.extract(begin, end);
            var value = parser.parse(text);

            context.addValue(value);
        };
    }

    @Override
    protected void fillArgs(NameMap<Object> args) {
        args.set("name", NAME);
        args.set("parser", parser.getName());
    }

}
