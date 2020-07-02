package gramat;

import gramat.common.TextException;
import gramat.engine.Determiner;
import gramat.engine.nodet.NMaker;
import gramat.expressions.Expression;
import gramat.parsing.*;
import gramat.parsing.parsers.ImportParser;
import gramat.parsing.parsers.RuleParser;
import gramat.parsing.parsers.TestParser;
import gramat.parsing.test.TestValue;

import java.nio.file.Path;
import java.util.*;

public class Grammar {

    public final Options options;

    private Path workingDir;

    private final LinkedHashMap<String, Expression> expressions;

    private final HashSet<Path> parsedFiles;

    public Grammar() {
        this(Options.createDefault());
    }

    public Grammar(Options options) {
        this.options = options;
        expressions = new LinkedHashMap<>();
        parsedFiles = new HashSet<>();
    }

    public Rule compile(String name) {
        var expression = expressions.get(name);

        if (expression == null) {
            throw new RuntimeException("not found: " + name);
        }

        var machine = NMaker.compile(name, expression);
        var root = Determiner.compile(machine);
        return new Rule(root);
    }

    public void parse(Path file) {
        if (parsedFiles.add(file)) {
            if (workingDir == null) {
                if (file != null) {
                    workingDir = file.getParent();
                }
            }

            parse(new Reader(file));
        }
    }

    public void parse(Reader reader) {
        while (reader.isAlive()) {
            reader.skipBlanks();

            boolean active = RuleParser.parse(this, reader)
                    || TestParser.parse(this, reader)
                    || ImportParser.parse(this, reader);

            if (!active) {
                break;
            }
        }

        if (reader.isAlive()) {
            throw new TextException("unexpected char", reader.getLocation());
        }
    }

    public Expression findExpression(String name) {
        var expression = expressions.get(name);

        if (expression == null) {
            throw new RuntimeException("not found: " + name);
        }

        return expression;
    }

    public void define(String name, Expression expression) {
        if (expressions.containsKey(name)) {
            throw new RuntimeException("already defined: " + name);
        }

        expressions.put(name, expression);
    }

    public Path resolveFile(Path file) {
        if (file.isAbsolute()) {
            return file;
        }

        if (workingDir == null) {
            throw new GramatException("Missing working directory.");
        }

        return workingDir.resolve(file);
    }

    public void runTest(String expressionName, String input, boolean expectedMatch, TestValue expectedValue) {
        if (options.ignoreTests) {
            return;  // TODO improve this handling
        }

        System.out.println("Testing " + expressionName);

        var expression = compile(expressionName);

        Object output;
        Rejection error;

        try {
            output = expression.eval(input);
            error = null;
        }
        catch(Rejection e) {
            output = null;
            error = e;
        }

        if (error == null) {
            if (expectedMatch) {
                System.out.print("OUTPUT: ");
                System.out.println(output);

                if (expectedValue != null) {
                    expectedValue.validate(output);
                    System.out.println("AND ASSERTED");
                }
            }
            else {
                throw new AssertionError("ERROR: UNEXPECTED MATCH: " + input);
            }
        }
        else if (expectedMatch) {
            throw new AssertionError("ERROR: MATCH EXPECTED: " + input, error);
        }
        else {
            System.out.println("NOT MATCHED (expected)");
        }
    }
}
