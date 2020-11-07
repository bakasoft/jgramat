package gramat;

import gramat.eval.Evaluator;
import gramat.formatting.StateFormatter;
import gramat.framework2.StandardProcess;
import gramat.input.Tape;
import gramat.machine.binary.Format;
import gramat.pipeline.Pipeline;
import gramat.pipeline.Sentence;
import gramat.pipeline.Source;
import gramat.util.FSUtils;
import gramat.util.Resources;
import org.junit.Test;
import util.TestUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class GrammarTest {

    @Test
    public void testGrammar() throws IOException {
        var proc = new StandardProcess("test", System.out);  // NOSONAR

        proc.debug("TEST!");

        for (var grammarFile : TestUtils.getGrammarFiles()) {
            var nameExt = FSUtils.extractExtension(grammarFile);
            var compiledFile = Path.of(nameExt.changeTo(".gmc"));
            var gramat = new Gramat();

            Source source;

            try {
                source = Pipeline.toSource(gramat, Tape.of(grammarFile));

                source.runTests(gramat);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (source.main != null) {
                if (!Files.exists(compiledFile)) {
                    compile(source, grammarFile, compiledFile);
                }

                System.out.println("Testing " + compiledFile);

                var state = Format.read(compiledFile);

                for (var jsonCase : TestUtils.getResourceFiles(path -> path.toString().startsWith(nameExt.name) && (path.toString().endsWith(".txt")||path.toString().endsWith(".json")))) {
                    var errorFile = Path.of(FSUtils.baseName(jsonCase) + ".err");
                    var gramat2 = new Gramat();
                    var tape = Tape.of(jsonCase);
                    var evaluator = new Evaluator(gramat2, tape, gramat2.getLogger());

                    Files.deleteIfExists(errorFile);

                    System.out.println("EVALUATING: " + jsonCase);

                    try {
                        var result = evaluator.evalValue(state);

                        System.out.println("    RESULT: " + result);
                    }
                    catch (Exception e) {
                        e.printStackTrace();

                        try (var output = Files.newOutputStream(errorFile)) {
                            var printStream = new PrintStream(output);

                            e.printStackTrace(printStream);

                            printStream.println();
                            printStream.println("STATE:");

                            new StateFormatter(printStream).write(state);
                        }

                        throw e;
                    }
                }
            }
        }
    }

    private static void compile(Source source, Path grammarFile, Path outputFile) {
        System.out.println("Compiling: " + grammarFile);

        var state = Pipeline.toState(new Gramat(), new Sentence(source.main, source.rules));
        var delete = true;
        try {
            try {
                try (var output = Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE_NEW)) {
                    Format.write(state, output);
                }
                delete = false;
            }
            finally {
                if (delete) {
                    Files.delete(outputFile);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
