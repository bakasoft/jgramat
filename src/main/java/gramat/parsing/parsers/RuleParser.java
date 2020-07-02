package gramat.parsing.parsers;

import gramat.common.TextException;
import gramat.parsing.Mark;
import gramat.Grammar;
import gramat.parsing.Reader;

public class RuleParser {

    public static boolean parse(Grammar grammar, Reader reader) {
        return reader.transaction(() -> {
            String keyword;

            if (reader.pull(Mark.VALUE_MARK)) {
                keyword = reader.readKeyword();

                if (keyword == null) {
                    throw new TextException("Expected keyword", reader.getLocation());
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

            var expression = ExpressionParser.parse(grammar, reader);

            if (expression == null) {
                throw new TextException("Expected an expression", reader.getLocation());
            }

            if (keyword != null) {
                expression = ValueMaker.make(grammar, reader, keyword, name, null, expression);
            }

            grammar.define(name, expression);

            return true;
        });
    }

}
