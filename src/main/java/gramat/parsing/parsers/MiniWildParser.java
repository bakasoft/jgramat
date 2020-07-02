package gramat.parsing.parsers;

import gramat.expressions.Expression;
import gramat.expressions.Wildcard;
import gramat.parsing.Reader;

public class MiniWildParser {

    public static Expression parse(Reader reader) {
        if (!reader.pull('~')) {
            return null;
        }

        return new Wildcard();  // TODO implement "mini-wild"
    }

}
