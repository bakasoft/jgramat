package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.GrammarBuilder;
import org.bakasoft.gramat.TestBase;

public class AlternationTest extends TestBase {{

    describe("Alternation", () -> {
        GrammarBuilder g = new GrammarBuilder();

        it ("should pass case 1", () -> {
            Element data = g.seq(g.rep(g.alt(g.sym("0"), g.sym("1")), 1), g.end());

            test(data, true, "0");
            test(data, true, "1");
            test(data, true, "10");
            test(data, true, "11");
            test(data, true, "100");
            test(data, true, "101");
            test(data, true, "10011001");
            test(data, false, "");
            test(data, false, "101a");
            test(data, false, "x101");
            test(data, false, "abc");
        });

        it ("should pass case 2", () -> {
            Element data = g.seq(g.sym("a"), g.rep(g.alt(g.sym("x"), g.sym("0"))), g.end());

            test(data, true, "a");
            test(data, true, "ax");
            test(data, true, "ax0");
            test(data, true, "a0x");
            test(data, true, "a00");
            test(data, true, "axx");
            test(data, true, "ax00x");

            test(data, false, "ax0a");
        });
    });
}}
