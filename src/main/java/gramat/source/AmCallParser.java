package gramat.source;

import gramat.models.source.ModelCall;

public interface AmCallParser extends AmBase, AmValue, AmExpressionParser {

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
