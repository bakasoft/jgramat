package gramat.parsers;

import gramat.compiling.ParseContext;
import gramat.expressions.*;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

import java.util.ArrayList;

public class TextParsers {

    public static Literal parseLiteral(ParseContext context, Source source) {
        var pos0 = source.getPosition();
        var value = BaseParsers.readString(source, '\"');

        if (value == null) {
            return null;
        }

        return new Literal(new Location(source, pos0), value);
    }

    public static Expression parsePredicate(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull('`')) {
            return null;
        }

        var items = new ArrayList<Expression>();

        boolean expectMore = false;

        while (source.alive()) {
            var begin = BaseParsers.readStringChar(source, '`');

            if (begin == null) {
                if (expectMore) {
                    throw source.error("expected more");
                }
                break;
            }

            var sep = BaseParsers.readStringChar(source, '`');

            if (sep == null || sep == ' ') {
                var location = source.locationOf(source.getPosition() - 1);

                items.add(new CharLiteral(location, begin));

                expectMore = (sep != null);
            }
            else if (sep == '-') {
                var end = BaseParsers.readStringChar(source, '`');

                if (end == null) {
                    throw source.error("expected end char");
                }

                var location = source.locationOf(source.getPosition() - 3);

                items.add(new CharRange(location, begin, end));

                var sep2 = BaseParsers.readStringChar(source, '`');

                if (sep2 == null) {
                    break;
                }
                else if (sep2 == ' ') {
                    expectMore = true;
                }
                else {
                    throw source.error("Invalid separator: " + sep);
                }
            }
            else {
                throw source.error("Invalid separator: " + sep);
            }
        }

        source.expect('`');

        if (items.size() == 1){
            var expression = items.get(0);

            if (expression instanceof CharLiteral) {
                context.warning("Use a string literal", expression.getLocation());
            }

            return expression;
        }

        return new Alternation(source.locationOf(pos0), items.toArray(Expression[]::new));
    }

}
