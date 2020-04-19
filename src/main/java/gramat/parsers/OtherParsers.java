package gramat.parsers;

import gramat.compiling.ParseContext;
import gramat.expressions.*;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class OtherParsers {

    public static Reference parseReference(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        var name = BaseParsers.readKeyword(source);

        if (name == null) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        if (source.pull('=')) {
            source.setPosition(pos0);
            return null;
        }

        return new Reference(new Location(source, pos0), name);
    }

    public static EndSource parseEnd(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull('$')) {
            return null;
        }

        return new EndSource(source.locationOf(pos0));
    }

    public static BeginSource parseBegin(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull('^')) {
            return null;
        }

        return new BeginSource(source.locationOf(pos0));
    }

}
