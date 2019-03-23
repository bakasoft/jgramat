package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.GrammarBuilder;
import org.bakasoft.gramat.TestBase;

public class RepetitionTest extends TestBase {{

    describe("Repetition", () -> {
        GrammarBuilder g = new GrammarBuilder();

        it ("case1", () -> {
            Element data = g.rep(g.sym("0"), 2, 4, g.sym("-"));

            test(data, false, "");
            test(data, false, "0");
            test(data, true, "0-0");
            test(data, true, "0-0-0-0");
            test(data, false, "0-0-0-0-0");
        });
    });

}}
