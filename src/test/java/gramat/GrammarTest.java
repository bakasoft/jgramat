package gramat;

import gramat.scheme.common.badges.BadgeSource;
import gramat.eval.Evaluator;
import gramat.exceptions.UnsupportedValueException;
import gramat.framework.Context;
import gramat.framework.StandardContext;
import gramat.input.Tape;
import gramat.scheme.data.parsing.GrammarData;
import gramat.scheme.State;
import gramat.pipeline.common.Format;
import gramat.formatting.ExpressionFormatter;
import gramat.scheme.data.test.EvalPassData;
import gramat.scheme.common.parsers.ParserSource;
import gramat.pipeline.Pipeline;
import gramat.scheme.common.symbols.Alphabet;
import gramat.util.WorkingFile;
import org.junit.Test;
import util.TestUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public class GrammarTest {

    @Test
    public void testGrammar() throws IOException {
        var resources = TestUtils.getResourcesFolder();
        var test = new StandardContext("test", System.out);
        var grammarFiles = resources.searchFiles(file -> file.hasExtension("gm"));

        test.setTotal(0, grammarFiles.size());

        for (var grammarFile : grammarFiles) {
            test.info("Processing grammar %s...", grammarFile);

            var logFile = grammarFile.withExtension("log");

            logFile.deleteIfExists();

            try (var logStream = logFile.openPrintStream()) {
                var ctx = new StandardContext(grammarFile.getName(), logStream);
                var source = testSource(ctx, grammarFile);

                if (source.main != null) {
                    var badges = new BadgeSource();  // TODO really? this shouldn't be a dependency
                    var state = testCompilation(ctx, grammarFile, source, badges);
                    var testCases = searchTestCases(grammarFile);

                    ctx.setTotal(0, testCases.size());

                    for (var testCase : testCases) {
                        ctx.info("Testing file %s...", testCase);

                        testFileCase(state, testCase, badges);

                        ctx.addProgress(1);
                    }
                }
            }
            logFile.delete();

            test.addProgress(1);
        }
    }

    private List<WorkingFile> searchTestCases(WorkingFile targetFile) {
        return targetFile.getRoot().searchFiles(
                file -> file.getName().startsWith(targetFile.getBaseName())
                        && file.hasExtension("txt", "json"));
    }

    private GrammarData testSource(Context ctx, WorkingFile grammarFile) {
        var tape = Tape.of(grammarFile.getAbsolutePath());

        var parsers = new ParserSource();
        var source = Pipeline.toGrammarData(ctx, tape, parsers);

        var alphabet = new Alphabet();
        var badges = new BadgeSource();

        runTests(ctx, source, alphabet, badges, parsers);

        return source;
    }

    public void runTests(Context ctx, GrammarData source, Alphabet alphabet, BadgeSource badges, ParserSource parsers) {
        var cache = new LinkedHashMap<String, State>();
        for (var test : source.tests) {
            if (test instanceof EvalPassData) {
                var pass = (EvalPassData)test;
                var expressionStr = ExpressionFormatter.format(pass.expression);
                var grammarData = new GrammarData();
                grammarData.main = pass.expression;
                grammarData.rules = source.rules;

                var state = cache.computeIfAbsent(expressionStr, k ->
                        Pipeline.toState(ctx, grammarData, alphabet, badges, parsers)
                );
                var tape = new Tape(pass.input);

                ctx.debug("evaluating: %s", pass.input);

                var evaluator = new Evaluator(ctx, tape, badges);
                var result = evaluator.evalValue(state);

                ctx.debug("result: %s", result);
            }
            else {
                throw new UnsupportedValueException(test);
            }
        }
    }

    private State testCompilation(Context ctx, WorkingFile grammarFile, GrammarData grammarData, BadgeSource badges) throws IOException {
        var compiledFile = grammarFile.withExtension("gmc");

        if (!compiledFile.exists()) {
            ctx.info("Compiling %s...", grammarFile);

            var alphabet = new Alphabet();
            var parsers = new ParserSource();
            var state = Pipeline.toState(ctx, grammarData, alphabet, badges, parsers);

            try (var output = compiledFile.openWriter()) {
                Format.write(state, output);
            }
            catch (IOException e) {
                compiledFile.delete();
                throw e;
            }
        }

        ctx.info("Loading %s...", compiledFile);

        try (var reader = compiledFile.openReader()) {
            return Format.read(reader);
        }
    }

    private void testFileCase(State state, WorkingFile fileCase, BadgeSource badges) throws IOException {
        var logFile = fileCase.withExtension("log");

        logFile.deleteIfExists();

        try (var logStream = logFile.openPrintStream()) {
            var ctx = new StandardContext(fileCase.getName(), logStream);
            var tape = Tape.of(fileCase.getAbsolutePath());
            var evaluator = new Evaluator(ctx, tape, badges);

            ctx.info("Evaluating %s...", fileCase);

            try {
                var result = evaluator.evalValue(state);

                ctx.info("Result: " + result);
            } catch (Exception e) {
                ctx.error(e);
                throw e;
            }
        }
        logFile.delete();
    }

}
