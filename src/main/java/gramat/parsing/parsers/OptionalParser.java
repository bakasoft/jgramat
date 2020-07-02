package gramat.parsing.parsers;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawOptionalAutomaton;
import gramat.parsing.Mark;
import gramat.parsing.Parser;
import gramat.parsing.Reader;

public class OptionalParser {

    public static RawAutomaton parse(Parser parser, Reader reader) {
        if (!reader.pull(Mark.OPTIONAL_BEGIN)) {
            return null;
        }

        reader.skipBlanks();

        var expression = ExpressionParser.parse(parser, reader);

        if (expression == null) {
            throw reader.error("expected expression");
        }

        reader.skipBlanks();

        if (!reader.pull(Mark.OPTIONAL_END)) {
            throw reader.error("expected optional end");
        }

        return new RawOptionalAutomaton(expression);
    }

}
