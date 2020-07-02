package gramat.parsing.parsers;

import gramat.common.TextException;
import gramat.expressions.Expression;
import gramat.expressions.Optional;
import gramat.parsing.Mark;
import gramat.Grammar;
import gramat.parsing.Reader;

public class OptionalParser {

    public static Expression parse(Grammar grammar, Reader reader) {
        if (!reader.pull(Mark.OPTIONAL_BEGIN)) {
            return null;
        }

        reader.skipBlanks();

        var expression = ExpressionParser.parse(grammar, reader);

        if (expression == null) {
            throw new TextException("expected expression", reader.getLocation());
        }

        reader.skipBlanks();

        if (!reader.pull(Mark.OPTIONAL_END)) {
            throw new TextException("expected optional end", reader.getLocation());
        }

        return new Optional(expression);
    }

}
