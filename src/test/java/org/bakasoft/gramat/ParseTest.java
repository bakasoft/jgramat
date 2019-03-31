package org.bakasoft.gramat;

public class ParseTest extends TestBase {{

    describe("Parsing test", () -> {
        it("parse alternation", () -> {
            testExpression("a|b|(a|b|c)|c");
            testExpression("a|b");
            testExpression("(a b c)|(x y z)");
        });
        it("parse negation", () -> {
            testExpression("!a");
            testExpression("!!a");
            testExpression("!(a b)");
        });
        it("parse object", () -> {
            testExpression("a:b");
            testExpression("a:(a b c)");
            testExpression("a:!{[a]}");
        });
        it("parse optional", () -> {
            testExpression("[a]");
            testExpression("[a b c]");
            testExpression("[a [b] [c d e]]");
        });
        it("parse property", () -> {
            testExpression("<a:b>");
            testExpression("<a:b c>");
            testExpression("<a:b|c>");
        });
        it("parse reference", () -> {
            testExpression("a");
            testExpression("a-b-c");
            testExpression("_a b_ _ a1");
        });
        it("parse repeition", () -> {
            testExpression("{a}");
            testExpression("{1 a b c}");
            testExpression("{1,2 a b/c}");
        });
        it("parse sequence", () -> {
            testExpression("a");
            testExpression("a b c");
            testExpression("a (b c) d");
        });
        it("parse string", () -> {
            testExpression("\"\"");
            testExpression("\"abc\"");
            testExpression("\"\\r\"");
        });
    });

}}
