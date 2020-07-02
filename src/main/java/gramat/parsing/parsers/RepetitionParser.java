package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawRepetitionAutomaton;
import gramat.parsing.Mark;
import gramat.parsing.Parser;
import gramat.parsing.Reader;

public class RepetitionParser {

    public static RawAutomaton parse(Parser parser, Reader reader) {
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
                        throw reader.error("expected max");
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

        var content = ExpressionParser.parse(parser, reader);

        if (content == null) {
            throw reader.error("expected expression");
        }

        reader.skipBlanks();

        RawAutomaton separator;

        if (reader.pull(Mark.REPETITION_SEPARATOR_MARK)) {
            reader.skipBlanks();

            separator = ExpressionParser.parse(parser, reader);

            if (separator == null) {
                throw reader.error("expected expression");
            }

            reader.skipBlanks();
        }
        else {
            separator = null;
        }

        if (!reader.pull(Mark.REPETITION_END)) {
            throw reader.error("expected }");
        }

        return new RawRepetitionAutomaton(content, separator, min, max);
    }

}
