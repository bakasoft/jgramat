package gramat.eval.value;

import gramat.compiling.ValueParser;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class ValueSave extends SubAction<ValueStart> {

    private final ValueParser parser;

    public ValueSave(ValueStart origin, ValueParser parser) {
        super(origin);
        this.parser = parser;
    }

    @Override
    public void run(Evaluator evaluator) {
        if (origin.position == null) {
            throw new RuntimeException("Missing start point");
        }

        int position0 = origin.position;
        int positionF = evaluator.source.getPosition();
        String value = evaluator.source.extract(position0, positionF);

        evaluator.peekAssembler().pushValue(value, parser);
    }

    @Override
    public String getDescription() {
        return "Save " + parser + " value";
    }
}
