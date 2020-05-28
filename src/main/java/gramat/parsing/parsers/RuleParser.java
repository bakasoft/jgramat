package gramat.parsing.parsers;

import gramat.parsers.Mark;
import gramat.parsing.Parser;
import gramat.parsing.Reader;

public class RuleParser {

    public static boolean parse(Parser parser, Reader reader) {
        return reader.transaction(() -> {
            String keyword;

            if (reader.pull(Mark.VALUE_MARK)) {
                keyword = reader.readKeyword();

                if (keyword == null) {
                    throw reader.error("Expected keyword");
                }

                reader.skipBlanks();
            }
            else {
                keyword = null;
            }

            String name = reader.readKeyword();

            if (name == null) {
                return false;
            }

            reader.skipBlanks();

            boolean soft;

            if (reader.pull(Mark.HARD_ASSIGNMENT_MARK)) {
                soft = false;
            }
            else {
                return false;
            }

            reader.skipBlanks();

            var expression = ExpressionParser.parse(parser, reader);

            if (expression == null) {
                throw reader.error("Expected an expression");
            }

            if (keyword != null) {
                expression = ValueMaker.make(parser, reader, keyword, name, null, expression);
            }

            parser.define(name, expression);

            return true;
        });
    }

}
