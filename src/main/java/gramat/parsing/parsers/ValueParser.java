package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.parsing.Mark;
import gramat.parsing.Parser;
import gramat.parsing.Reader;

public class ValueParser {

    public static RawAutomaton parse(Parser parser, Reader reader) {
        return reader.transaction(() -> {
            if (!reader.pull(Mark.VALUE_MARK)) {
                return null;
            }

            String keyword = reader.readKeyword();

            if (keyword == null) {
                throw reader.error("Expected keyword");
            }

            reader.skipBlanks();

            if (!reader.pull(Mark.GROUP_BEGIN)) {
                return null;
            }

            reader.skipBlanks();

            var nameLit = reader.readString(Mark.TOKEN_DELIMITER);
            RawAutomaton nameExp;
            RawAutomaton valueExp;

            if (nameLit != null) {
                nameExp = null;

                reader.skipBlanks();

                if (reader.pull(Mark.NAME_SEPARATOR)) {
                    reader.skipBlanks();

                    valueExp = ExpressionParser.parse(parser, reader);

                    if (valueExp == null) {
                        throw reader.error("Expected expression");
                    }
                }
                else {
                    valueExp = null;
                }
            }
            else {
                var expression = ExpressionParser.parse(parser, reader);

                if (expression == null) {
                    throw reader.error("Expected expression.");
                }

                reader.skipBlanks();

                if (reader.pull(Mark.NAME_SEPARATOR)) {
                    reader.skipBlanks();

                    nameExp = expression;
                    valueExp = ExpressionParser.parse(parser, reader);

                    if (valueExp == null) {
                        throw reader.error("Expected expression");
                    }
                } else {
                    nameExp = null;
                    valueExp = expression;
                }
            }

            if (!reader.pull(Mark.GROUP_END)) {
                throw reader.error("Expected " + Mark.GROUP_END);
            }

            return ValueMaker.make(parser, reader, keyword, nameLit, nameExp, valueExp);
        });
    }

}
