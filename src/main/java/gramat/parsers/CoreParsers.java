package gramat.parsers;

import gramat.expressions.*;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

import java.util.ArrayList;

public class CoreParsers {

    public static NamedExpression parseNamedExpression(Source source) {
        int pos0 = source.getPosition();
        boolean isObject = false;
        boolean isString = false;

        if (source.pull("@object")) {
            isObject = true;
            BaseParsers.skipBlanks(source);
        }
        else if (source.pull("@string")) {
            isString = true;
            BaseParsers.skipBlanks(source);
        }

        String name = BaseParsers.readKeyword(source);

        if (name == null) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        if (!source.pull('=')) {
            source.setPosition(pos0);
            return null;
        }

        BaseParsers.skipBlanks(source);

        Expression expression = parseExpression(source);

        if (expression == null) {
            throw source.error("Expected an expression");
        }

        var location = source.locationOf(pos0);

        if (isObject) {
            expression = new ObjectExp(location, name, expression);
        }
        else if (isString) {
            expression = new StringExp(location, expression);
        }

        return new NamedExpression(location, name, expression);
    }

    public static Expression parseItem(Source source) {
        Expression e;

        e = TextParsers.parsePredicate(source);
        if (e != null) { return e; }

        e = TextParsers.parseLiteral(source);
        if (e != null) { return e; }

        e = OtherParsers.parseReference(source);
        if (e != null) { return e; }

        e = OtherParsers.parseBegin(source);
        if (e != null) { return e; }

        e = OtherParsers.parseEnd(source);
        if (e != null) { return e; }

        e = ValueParsers.parseString(source);
        if (e != null) { return e; }

        e = ValueParsers.parseStringLiteral(source);
        if (e != null) { return e; }

        e = ValueParsers.parseObject(source);
        if (e != null) { return e; }

        e = ValueParsers.parseList(source);
        if (e != null) { return e; }

        e = ValueParsers.parseAttribute(source);
        if (e != null) { return e; }

        e = GroupParsers.parseNegation(source);
        if (e != null) { return e; }

        e = GroupParsers.parseOptional(source);
        if (e != null) { return e; }

        e = GroupParsers.parseRepetition(source);
        if (e != null) { return e; }

        return GroupParsers.parseGroup(source);
    }

    public static Expression parseExpression(Source source) {
        var pos0 = source.getPosition();
        var altSeq = new ArrayList<ArrayList<Expression>>();

        altSeq.add(new ArrayList<>());

        while (source.alive()) {
            var item = parseItem(source);

            if (item == null) {
                break;
            }

            altSeq.get(altSeq.size()-1).add(item);

            BaseParsers.skipBlanks(source);

            if (source.pull('|')) {
                BaseParsers.skipBlanks(source);

                if (altSeq.get(altSeq.size()-1).size() == 0) {
                    throw source.error("Expected expression.");
                }

                altSeq.add(new ArrayList<>());
            }
        }

        if (altSeq.isEmpty()) {
            throw source.error("Expected expression.");
        }

        var options = new ArrayList<Expression>();

        for (var seq : altSeq) {
            if (seq.isEmpty()) {
                throw source.error("Expected expression.");
            }
            else if (seq.size() == 1) {
                options.add(seq.get(0));
            }
            else{
                var s = new Sequence(seq.get(0).getLocation(), seq.toArray(Expression[]::new));

                options.add(s);
            }
        }

        if (options.isEmpty()) {
            throw source.error("Expected expression.");
        }
        else if (options.size() == 1) {
            return options.get(0);
        }

        return new Alternation(new Location(source, pos0), options.toArray(Expression[]::new));
    }

}
