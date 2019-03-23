package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.GrammarBuilder;
import org.bakasoft.gramat.TestBase;

public class SymbolTest extends TestBase {{
    GrammarBuilder g = new GrammarBuilder();

    describe("Symbol expression", () -> {
        it ("should detect correct cases", () -> {
            test(g.sym("a"), true, "a");
            test(g.sym("ab"), true, "ab");
            test(g.sym("abc"), true, "abc");
            test(g.sym("a b c"), true, "a b c");
            test(g.sym("{([!])}"), true, "{([!])}");
        });
        it ("should detect wrong cases", () -> {
            test(g.sym("a"), false, "");
            test(g.sym("a"), false, "A");
            test(g.sym("a"), false, "aa");
            test(g.sym("{([!])}"), false, "[!]");
        });
    });

}}
