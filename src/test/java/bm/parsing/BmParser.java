package bm.parsing;

import bm.BmException;
import bm.parsing.data.SourceFile;
import gramat.Grammar;
import gramat.compiling.Compiler;
import gramat.util.parsing.Source;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class BmParser {

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

        List<Path> files;

        try {
            files = Files.walk(suite.getFolder())
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toUnmodifiableList());
        } catch (IOException e) {
            throw new BmException("Cannot read suite folder.", e);
        }

        for (var file : files) {
            terminal.info("Parsing source file: " + suite.computeFileRef(file));

            var source = parseSourceFile(file);

            suite.add(file, source);
        }

        return suite;
    }

    public SourceFile parseSourceFile(Path file) {
        return grammar.evalValue("SourceFile", Source.fromFile(file), SourceFile.class);
    }

}
