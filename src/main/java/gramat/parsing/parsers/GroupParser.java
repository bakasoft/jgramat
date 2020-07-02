package gramat.parsing.parsers;

import gramat.common.TextException;
import gramat.expressions.Expression;
import gramat.parsing.Mark;
import gramat.Grammar;
import gramat.parsing.Reader;

public class GroupParser {

    public static Expression parse(Grammar grammar, Reader reader) {
        if (!reader.pull(Mark.GROUP_BEGIN)) {
            return null;
        }

        reader.skipBlanks();

        var expression = ExpressionParser.parse(grammar, reader);

        if (expression == null) {
            throw new TextException("expected expression", reader.getLocation());
        }

        reader.skipBlanks();

        if (!reader.pull(Mark.GROUP_END)) {
            throw new TextException("expected group end", reader.getLocation());
        }

        return expression;
    }

}
