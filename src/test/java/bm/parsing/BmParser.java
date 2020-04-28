package bm.parsing;

import bm.BmException;
import bm.parsing.data.SourceFile;
import gramat.Grammar;
import gramat.compiling.Compiler;
import gramat.util.FileTool;
import gramat.util.parsing.Source;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class BmParser {

    public static final String DEFAULT_BM_FILE = "bm.json";

    private final BmTerminal terminal;
    private final Grammar grammar;

    public BmParser(BmTerminal terminal) {
        var compiler = new Compiler();
        
        compiler.setType(SourceFile.class);

        try {
            compiler.parseFile(Paths.get(BmParser.class.getResource("/grammar/bm.gm").toURI()));
        } catch (URISyntaxException e) {
            throw new BmException("Cannot load the Bm grammar.", e);
        }

        compiler.compile();

        this.terminal = terminal;
        this.grammar = compiler.createGrammar();
    }

    public BmSuiteFolder parseSuite(Path folder) {
        var suite = new BmSuiteFolder(folder);

        terminal.info("Parsing suite: " + suite.getFolder());

        List<Path> files = FileTool.listVisibleFiles(suite.getFolder());

        var bmFilePath = suite.getFolder().resolve(DEFAULT_BM_FILE);

        if (Files.exists(bmFilePath)) {
            terminal.info("Reading Bm file: " + bmFilePath);

            var bmFile = BmFile.parse(bmFilePath);

            suite.setBmFile(bmFile);
        }
        else {
            terminal.info("Creating Bm file: " + bmFilePath);

            var bmFile = BmFile.create(bmFilePath);

            suite.setBmFile(bmFile);
        }

        var bmFile = suite.getBmFile();
        var sourceFilePattern = bmFile.getSourceFilePattern();

        for (var file : files) {
            var fileName = file.getFileName().toString();

            if (sourceFilePattern.matcher(fileName).matches()) {
                terminal.info("Parsing source file: " + suite.computeFileRef(file));

                var source = parseSourceFile(file);

                suite.add(file, source);
            }
            else if (!DEFAULT_BM_FILE.equals(fileName)) {
                throw new BmException("Unsupported file: " + file);
            }
        }

        return suite;
    }

    public SourceFile parseSourceFile(Path file) {
        return grammar.evalValue("SourceFile", Source.fromFile(file), SourceFile.class);
    }

}
