package gramat.parsing;

import gramat.input.Tape;
import gramat.models.source.ModelCall;

public interface AmCallParser extends AmBase, AmValue, AmExpressionParser {

    @Override
    default ModelCall tryCall(Tape tape) {
        if (tryToken(tape, '@')) {
            var call = new ModelCall();

            call.keyword = readString(tape);

            if (tryToken(tape, '<')) {
                call.arguments = readArguments(tape);

                expectToken(tape, '>');
            }

            if (tryToken(tape, '(')) {
                call.expression = readExpression(tape);

                expectToken(tape, ')');
            }

            return call;
        }
        return null;
    }

}
