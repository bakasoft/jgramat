package gramat.parsing;

import gramat.GramatException;
import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;
import gramat.compiling.GrammarTest;
import gramat.compiling.ValueParser;
import gramat.eval.RejectedError;
import gramat.expressions.Expression;
import gramat.parsing.parsers.ExpressionParser;
import gramat.parsing.parsers.ImportParser;
import gramat.parsing.parsers.RuleParser;
import gramat.parsing.parsers.TestParser;
import gramat.runtime.EvalContext;
import gramat.util.GramatWriter;
import gramat.util.parsing.ParseException;
import gramat.util.parsing.Source;

import java.nio.file.Path;
import java.util.*;

public class Parser {

    public final Options options;

    private Path workingDir;

    private final LinkedHashMap<String, RawAutomaton> expressions;
    private final List<GrammarTest> tests;

    private final HashSet<Path> parsedFiles;

    public Parser() {
        this(Options.createDefault());
    }

    public Parser(Options options) {
        this.options = options;
        expressions = new LinkedHashMap<>();
        tests = new ArrayList<>();
        parsedFiles = new HashSet<>();
    }

    public CompiledExpression compile(String name) {
        var automaton = expressions.get(name);

        if (automaton == null) {
            throw new RuntimeException("not found: " + name);
        }

        var collapsed = automaton.collapse();
        var root = NContext.compile(collapsed);

        return new CompiledExpression(root);
    }

    public void parse(Path file) {
        if (parsedFiles.add(file)) {
            if (workingDir == null) {
                if (file != null) {
                    workingDir = file.getParent();
                }
            }

            parse(Reader.of(file));
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
            throw reader.error("unexpected char");
        }
    }

    public RawAutomaton findExpression(String name) {
        var expression = expressions.get(name);

        if (expression == null) {
            throw new RuntimeException("not found: " + name);
        }

        return expression;
    }

    public void define(String name, RawAutomaton expression) {
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

    public void addTest(GrammarTest test) {
        this.tests.add(test);
    }

    public void runTests() {
        var exprCache = new HashMap<String, CompiledExpression>();

        for (var test : tests) {
            System.out.println("Testing " + test.expressionName);

            var expression = exprCache.computeIfAbsent(test.expressionName, this::compile);

            Object output;
            RejectedError error;

            try {
                output = expression.eval(test.input);
                error = null;
            }
            catch(RejectedError e) {
                output = null;
                error = e;
            }

            if (error == null) {
                if (test.expectedMatch) {
                    System.out.print("MATCHED: ");
                    new GramatWriter(System.out, "  ").writeValue(output);
                    System.out.println();

                    if (test.expectedValue != null) {
                        test.expectedValue.validate(output);
                        System.out.println("AND ASSERTED");
                    }
                }
                else {
                    throw new AssertionError("ERROR: UNEXPECTED MATCH: " + test.input);
                }
            }
            else if (test.expectedMatch) {
                throw new AssertionError("ERROR: MATCH EXPECTED: " + test.input, error);
            }
            else {
                System.out.println("NOT MATCHED (expected)");

            }
        }
    }
}
