package gramat.parsing.parsers;

import gramat.common.TextException;
import gramat.expressions.Expression;
import gramat.expressions.Repetition;
import gramat.parsing.Mark;
import gramat.Grammar;
import gramat.parsing.Reader;

public class RepetitionParser {

    public static Expression parse(Grammar grammar, Reader reader) {
        if (!reader.pull(Mark.REPETITION_BEGIN)) {
            return null;
        }

        reader.skipBlanks();

        Integer min;
        Integer max;

        if (reader.pull(Mark.REPETITION_PLUS)) {
            reader.skipBlanks();

            min = 1;
            max = null;
        }
        else {
            min = reader.readInteger();

            if (min != null) {
                reader.skipBlanks();

                if (reader.pull(Mark.REPETITION_RANGE_SEPARATOR)) {
                    reader.skipBlanks();

                    max = reader.readInteger();

                    if (max == null) {
                        throw new TextException("expected max", reader.getLocation());
                    }

                    reader.skipBlanks();
                }
                else if (reader.pull(Mark.REPETITION_COUNT_MARK)) {
                    max = min;

                    reader.skipBlanks();
                }
                else {
                    max = null;
                }
            } else {
                max = null;
            }
        }

        var content = ExpressionParser.parse(grammar, reader);

        if (content == null) {
            throw new TextException("expected expression", reader.getLocation());
        }

        reader.skipBlanks();

        Expression separator;

        if (reader.pull(Mark.REPETITION_SEPARATOR_MARK)) {
            reader.skipBlanks();

            separator = ExpressionParser.parse(grammar, reader);

            if (separator == null) {
                throw new TextException("expected expression", reader.getLocation());
            }

            reader.skipBlanks();
        }
        else {
            separator = null;
        }

        if (!reader.pull(Mark.REPETITION_END)) {
            throw new TextException("expected }", reader.getLocation());
        }

        return new Repetition(content, separator, min, max);
    }

}
