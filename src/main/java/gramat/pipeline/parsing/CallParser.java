package gramat.pipeline.parsing;

import gramat.models.parsing.ModelCall;

public interface CallParser extends BaseParser, ValueParser, ExpressionParser {

    @Override
    default ModelCall tryCall() {
        if (tryToken('@')) {
            var call = new ModelCall();

            call.keyword = readString();

            if (tryToken('<')) {
                call.arguments = readArguments();

                expectToken('>');
            }

            if (tryToken('(')) {
                call.expression = readExpression();

                expectToken(')');
            }

            return call;
        }
        return null;
    }

}
