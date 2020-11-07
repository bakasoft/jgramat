package gramat.parsing;

import gramat.input.Tape;
import gramat.models.source.ModelCall;

public interface AmCallParser extends AmBase, AmValue, AmExpressionParser {

    @Override
    default ModelCall tryCall(Parser parser) {
        if (tryToken(parser, '@')) {
            var call = new ModelCall();

            call.keyword = readString(parser);

            if (tryToken(parser, '<')) {
                call.arguments = readArguments(parser);

                expectToken(parser, '>');
            }

            if (tryToken(parser, '(')) {
                call.expression = readExpression(parser);

                expectToken(parser, ')');
            }

            return call;
        }
        return null;
    }

}
