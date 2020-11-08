package gramat.readers;

import gramat.readers.models.ModelCall;

public interface CallReader extends BaseReader, ValueReader, ExpressionReader {

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
