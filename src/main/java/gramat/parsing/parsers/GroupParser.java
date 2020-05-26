package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.parsers.BaseParsers;
import gramat.parsers.CoreParsers;
import gramat.parsers.Mark;
import gramat.parsing.Parser;
import gramat.parsing.Reader;

public class GroupParser {

    public static RawAutomaton parse(Parser parser, Reader reader) {
        if (!reader.pull(Mark.GROUP_BEGIN)) {
            return null;
        }

        reader.skipBlanks();

        var expression = ExpressionParser.parse(parser, reader);

        if (expression == null) {
            throw reader.error("expected expression");
        }

        reader.skipBlanks();

        if (!reader.pull(Mark.GROUP_END)) {
            throw reader.error("expected group end");
        }

        return expression;
    }

}
