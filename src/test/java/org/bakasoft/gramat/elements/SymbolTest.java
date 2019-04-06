package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.TestBase;

public class SymbolTest extends TestBase {{
    describe("Symbol expression", () -> {
        it ("should detect correct cases", () -> {
            test("\"a\"", true, "a");
            test("\"ab\"", true, "ab");
            test("\"abc\"", true, "abc");
            test("\"a b value\"", true, "a b value");
            test("\"{([!])}\"", true, "{([!])}");
        });
        it ("should detect wrong cases", () -> {
            test("\"a\"", false, "");
            test("\"a\"", false, "A");
            test("\"a\"", false, "aa");
            test("\"{([!])}\"", false, "[!]");
        });
    });

}}
