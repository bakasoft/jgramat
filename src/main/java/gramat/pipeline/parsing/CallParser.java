package gramat.pipeline.parsing;

import gramat.scheme.data.parsing.CallData;

public interface CallParser extends BaseParser, ValueParser, ExpressionParser {

    @Override
    default CallData tryCall() {
        if (tryToken('@')) {
            var call = new CallData();

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
