package org.bakasoft.gramat;

import org.bakasoft.gramat.util.FileHelper;

import java.nio.file.Paths;

public class TestGrammarResources extends TestBase {{

    pass("URL case", () -> {
        Gramat gramat = new Gramat();
        gramat.load(Paths.get("/Users/sergio.pedraza/bakasoft/jgramat/src/test/resources/url.test.gmt"));
        gramat.test();
    });
    pass("JSON case", () -> {
        Gramat gramat = new Gramat();
        gramat.addTransformation("hex-to-char",
                input -> String.valueOf((char)Integer.parseInt(input, 16)));
        gramat.load(Paths.get("/Users/sergio.pedraza/bakasoft/jgramat/src/test/resources/json.test.gmt"));
        gramat.test();
    });

}}
