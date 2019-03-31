package org.bakasoft.gramat;

import org.bakasoft.gramat.util.FileHelper;

public class TestGrammarResources extends TestBase {{

    describe("Grammar resources", () -> {
        it ("URL case", () -> {
            Gramat gramat = new Gramat();
            gramat.load(FileHelper.findResourcePath("url.test.gmt"));
            gramat.test();
        });
    });

}}
