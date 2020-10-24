package gramat.actions.transactions;

import gramat.eval.transactions.DefaultTransaction;
import gramat.eval.Context;
import gramat.parsers.ValueParser;

import java.util.Objects;

public class ValueTransaction extends DefaultTransaction {

    private final ValueParser parser;

    public ValueTransaction(int id, ValueParser parser) {
        super(id);
        this.parser = Objects.requireNonNull(parser);
    }

    @Override
    public void begin(Context context) {
        var beginPosition = context.tape.getPosition();

        context.pushPosition(beginPosition);
    }

    @Override
    public Runnable prepareCommit(Context context) {
        var end = context.tape.getPosition();

        return () -> {
            var begin = context.popPosition();
            var text = context.tape.extract(begin, end);
            var value = parser.parse(text);

            context.addValue(value);
        };
    }

    @Override
    public String getName() {
        return "value";
    }

}
