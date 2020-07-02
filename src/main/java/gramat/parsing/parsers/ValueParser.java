package gramat.parsing.parsers;

import gramat.common.TextException;
import gramat.expressions.Expression;
import gramat.parsing.Mark;
import gramat.Grammar;
import gramat.parsing.Reader;

public class ValueParser {

    public static Expression parse(Grammar grammar, Reader reader) {
        return reader.transaction(() -> {
            if (!reader.pull(Mark.VALUE_MARK)) {
                return null;
            }

            String keyword = reader.readKeyword();

            if (keyword == null) {
                throw new TextException("Expected keyword", reader.getLocation());
            }

            reader.skipBlanks();

            if (!reader.pull(Mark.GROUP_BEGIN)) {
                return null;
            }

            reader.skipBlanks();

            var nameLit = reader.readString(Mark.TOKEN_DELIMITER);
            Expression nameExp;
            Expression valueExp;

            if (nameLit != null) {
                nameExp = null;

                reader.skipBlanks();

                if (reader.pull(Mark.NAME_SEPARATOR)) {
                    reader.skipBlanks();

                    valueExp = ExpressionParser.parse(grammar, reader);

                    if (valueExp == null) {
                        throw new TextException("Expected expression", reader.getLocation());
                    }
                }
                else {
                    valueExp = null;
                }
            }
            else {
                var expression = ExpressionParser.parse(grammar, reader);

                if (expression == null) {
                    throw new TextException("Expected expression.", reader.getLocation());
                }

                reader.skipBlanks();

                if (reader.pull(Mark.NAME_SEPARATOR)) {
                    reader.skipBlanks();

                    nameExp = expression;
                    valueExp = ExpressionParser.parse(grammar, reader);

                    if (valueExp == null) {
                        throw new TextException("Expected expression", reader.getLocation());
                    }
                } else {
                    nameExp = null;
                    valueExp = expression;
                }
            }

            if (!reader.pull(Mark.GROUP_END)) {
                throw new TextException("Expected " + Mark.GROUP_END, reader.getLocation());
            }

            return ValueMaker.make(grammar, reader, keyword, nameLit, nameExp, valueExp);
        });
    }

}
