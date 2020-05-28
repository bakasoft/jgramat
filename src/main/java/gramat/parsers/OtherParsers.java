package gramat.parsers;

import gramat.automata.raw.units.RawWildAutomaton;
import gramat.compiling.ParseContext;
import gramat.expressions.Expression;
import gramat.expressions.flat.BeginSource;
import gramat.expressions.flat.CharAutomaton;
import gramat.expressions.flat.EndSource;
import gramat.expressions.flat.Reference;
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

        if (source.pull(Mark.HARD_ASSIGNMENT_MARK)) {
            source.setPosition(pos0);
            return null;
        }

        return new Reference(new Location(source, pos0), name);
    }

    public static EndSource parseEnd(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull(Mark.END_SOURCE_MARK)) {
            return null;
        }

        return new EndSource(source.locationOf(pos0));
    }

    public static Expression parseWild(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull(Mark.WILD_MARK)) {
            return null;
        }

        return new CharAutomaton(source.locationOf(pos0), new RawWildAutomaton());
    }

    public static BeginSource parseBegin(ParseContext context, Source source) {
        var pos0 = source.getPosition();

        if (!source.pull(Mark.BEGIN_SOURCE_MARK)) {
            return null;
        }

        return new BeginSource(source.locationOf(pos0));
    }

}
