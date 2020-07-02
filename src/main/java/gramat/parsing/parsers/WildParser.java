package gramat.parsing.parsers;

import gramat.expressions.Expression;
import gramat.expressions.Wildcard;
import gramat.parsing.Mark;
import gramat.parsing.Reader;

public class WildParser {

    public static Expression parse(Reader reader) {
        if (!reader.pull(Mark.WILD_MARK)) {
            return null;
        }

        return new Wildcard();
    }

}
