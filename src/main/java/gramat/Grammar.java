package gramat;

import gramat.common.TextException;
import gramat.engine.indet.IMachine;
import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NLanguage;
import gramat.expressions.Rule;
import gramat.parsing.*;
import gramat.parsing.parsers.ImportParser;
import gramat.parsing.parsers.RuleParser;
import gramat.parsing.parsers.TestParser;
import gramat.parsing.test.TestValue;
import gramat.tools.RuleSet;

import java.nio.file.Path;
import java.util.*;

public class Grammar {

    public final Options options;

    private Path workingDir;

    private final RuleSet rules;

    private final HashSet<Path> parsedFiles;

    public Grammar() {
        this(Options.createDefault());
    }

    public Grammar(Options options) {
        this.options = options;
        rules = new RuleSet();
        parsedFiles = new HashSet<>();
    }

    public Parser compile(String name) {
        var rule = rules.get(name);

        if (rule == null) {
            throw new RuntimeException("not found: " + name);
        }

        var lang = new NLanguage();
        var builder = new NBuilder(lang);
        var nMachine = builder.compile(rule);
        var iMachine = new IMachine(lang, nMachine);
        var initial = iMachine.compile();

        return new Parser(initial);
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

    public Rule findRule(String name) {
        var rule = rules.get(name);

        if (rule == null) {
            throw new RuntimeException("not found: " + name);
        }

        return rule;
    }

    public void addRule(Rule rule) {
        if (!rules.add(rule)) {
            throw new RuntimeException("already defined: " + rule.name);
        }
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

    public void runTest(String ruleName, String input, boolean expectedMatch, TestValue expectedValue) {
        if (options.ignoreTests) {
            return;  // TODO improve this handling
        }

        System.out.println("Testing " + ruleName);

        var expression = compile(ruleName);

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
