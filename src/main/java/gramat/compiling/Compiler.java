package gramat.compiling;

import gramat.GramatException;
import gramat.Grammar;
import gramat.builtin.*;
import gramat.expressions.Expression;
import gramat.expressions.NamedExpression;
import gramat.parsers.BaseParsers;
import gramat.parsers.CoreParsers;
import gramat.parsers.Mark;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;
import gramat.util.parsing.Source;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Compiler extends LinkContext implements ParseContext {

    private Path workingDir;

    private final List<NamedExpression> rules;

    private final Set<Path> parsedFiles;

    private final List<GrammarTest> tests;

    private final HashMap<String, ValueParser> valueParsers;

    private final HashMap<String, Class<?>> types;

    public Compiler() {
        rules = new ArrayList<>();
        parsedFiles = new HashSet<>();
        tests = new ArrayList<>();
        valueParsers = new HashMap<>();
        types = new HashMap<>();

        setParser("boolean", new BooleanParser());
        setParser("integer", new IntegerParser());
        setParser("number", new NumberParser());
        setParser("string", new StringParser());
        setParser("hex-to-char", new HexToCharParser());
    }

    public EvalContext createEvalContext(Source source) {
        return new EvalContext(source, types);
    }

    public Grammar createGrammar() {
        var expressions = new HashMap<String, Expression>();

        for (var expression : rules) {
            if (expressions.put(expression.getName(), expression) != null) {
                throw new GramatException("duplicated expression: " + expression.getName());
            }
        }

        return new Grammar(expressions, types);
    }

    public Expression compileRule(String ruleName) {
        System.out.println("Compiling rule: " + ruleName);

        NamedExpression rule = null;

        for (var r : rules) {
            if (ruleName.equals(r.getName())) {
                rule = r;
                break;
            }
        }

        if (rule == null) {
            throw new GramatException("no rule found");
        }

        return rule.optimize(this);
    }

    public void parse(Source source) {
        if (workingDir == null) {
            if (source.getFile() != null) {
                workingDir = source.getFile().getParent();
            }
        }

        while (source.alive()) {
            BaseParsers.skipBlanks(source);

            var somethingParsed = parseRule(source) || parseImport(source) || parseTest(source);

            if (!somethingParsed) {
                break;
            }
        }

        if (source.alive()) {
            throw source.error("Unexpected char: " + source.peek());
        }
    }

    private Source parseInput(Source source) {
        if (source.pull("@load")) {
            BaseParsers.skipBlanks(source);

            source.expect(Mark.GROUP_BEGIN);

            BaseParsers.skipBlanks(source);

            var path = BaseParsers.readString(source, Mark.TOKEN_DELIMITER);

            if (path == null) {
                throw source.error("Expected file path");
            }

            var file = resolveFile(Path.of(path));
            var input = Source.fromFile(file);

            BaseParsers.skipBlanks(source);

            source.expect(Mark.GROUP_END);

            return input;
        }

        var value = BaseParsers.readString(source, Mark.TOKEN_DELIMITER);

        if (value == null) {
            return null;
        }

        return new Source(value, null);
    }

    private boolean parseTest(Source source) {
        int pos0 = source.getPosition();
        boolean expectedMatch;

        if (source.pull("@pass")) {
            expectedMatch = true;
        }
        else if (source.pull("@fail")) {
            expectedMatch = false;
        }
        else {
            return false;
        }

        BaseParsers.skipBlanks(source);

        var exprName = BaseParsers.readKeyword(source);

        BaseParsers.skipBlanks(source);

        var input = parseInput(source);

        var location = source.locationOf(pos0);
        var test = new GrammarTest(location, exprName, input, expectedMatch);

        tests.add(test);

        return true;
    }

    private boolean parseRule(Source source) {
        var rule = CoreParsers.parseNamedExpression(this, source);

        if (rule == null) {
            return false;
        }

        rules.add(rule);

        return true;
    }

    private boolean parseImport(Source source) {
        int pos0 = source.getPosition();

        if (!source.pull("@import")) {
            source.setPosition(pos0);
            return false;
        }

        BaseParsers.skipBlanks(source);

        String path = BaseParsers.readString(source, Mark.TOKEN_DELIMITER);

        if (path == null) {
            throw source.error("Expected file path string.");
        }

        var file = Paths.get(path);

        if (parsedFiles.contains(file)) {
            return true;
        }

        parsedFiles.add(file);

        parseFile(file);

        return true;
    }

    public void parseFile(Path file) {
        var source = Source.fromFile(resolveFile(file));

        parse(source);
    }

    private Path resolveFile(Path file) {
        if (file.isAbsolute()) {
            return file;
        }

        if (workingDir == null) {
            throw new GramatException("Missing working directory.");
        }

        return workingDir.resolve(file);
    }

    public void setParser(String name, ValueParser parser) {
        valueParsers.put(name, parser);
    }

    public ValueParser getParser(String name) {
        return valueParsers.get(name);
    }

    @Override
    public NamedExpression getExpression(String name) {
        NamedExpression expression = null;

        for (var rule : rules) {
            if (Objects.equals(rule.getName(), name)) {
                if (expression != null) {
                    // TODO include other location
                    throw new ParseException("Ambiguous expression: " + name, rule.getLocation());
                }

                expression = rule;
            }
        }

        return expression;
    }


    public void setType(Class<?> type) {
        setType(type.getSimpleName(), type);
    }

    public void setType(String name, Class<?> type) {
        types.put(name, type);
    }

    @Override
    public Class<?> getType(String name) {
        return types.get(name);
    }

    @Override
    public void warning(String message, Location location) {
        System.err.println(message + " <- " + location);
    }

}
