package gramat;

import gramat.nodes.NodeFactory;
import gramat.nodes.impl.NodeSequence;
import gramat.symbols.SymbolFactory;
import tools.TestUtils;
import org.junit.Test;

public class JsonTest {

    private static NodeSequence nString;
    private static NodeSequence nObject;
    private static NodeSequence nArray;
    private static NodeSequence nNumber;
    private static NodeSequence nValue;

    static {
        var symbols = new SymbolFactory();
        var n = new NodeFactory();

        var controlChar = symbols.union(
                symbols.range('\u0000', '\u001F'),
                symbols.literal('\u007F'),
                symbols.range('\u0080', '\u009F')
        );
        var minus = symbols.literal('-');
        var plus = symbols.literal('+');
        var point = symbols.literal('.');
        var zero = symbols.literal('0');
        var oneNine = symbols.range('1', '9');
        var digit = symbols.range('0', '9');
        var hexChar = symbols.union(
                digit,
                symbols.range('a', 'f'),
                symbols.range('A', 'F'));
        var quotationMark = symbols.literal('\"');
        var reverseSolidus = symbols.literal('\\');
        var solidus = symbols.literal('/');
        var bLower = symbols.literal('b');
        var fLower = symbols.literal('f');
        var nLower = symbols.literal('n');
        var rLower = symbols.literal('r');
        var tLower = symbols.literal('t');
        var uLower = symbols.literal('u');
        var eLower = symbols.literal('e');
        var eUpper = symbols.literal('E');
        var noEscaped = symbols.not(symbols.union(
                quotationMark,
                reverseSolidus,
                controlChar
        ));
        var braceOpen = symbols.literal('{');
        var braceClose = symbols.literal('}');
        var bracketOpen = symbols.literal('[');
        var bracketClose = symbols.literal(']');
        var colon = symbols.literal(':');
        var comma = symbols.literal(',');
        var whitespace = symbols.union(
                symbols.literal(' '),
                symbols.literal('\n'),
                symbols.literal('\r'),
                symbols.literal('\t')
        );

        // Node instances
        var nWhitespace = n.repeat(n.symbol(whitespace));
        var nEntry = n.sequence();
        var nEntryList = n.sequence();
        nString = n.sequence();
        nObject = n.sequence();
        nArray = n.sequence();
        nValue = n.sequence();
        nNumber = n.sequence();

        // String
        nString.append(n.symbol(quotationMark));
        nString.append(
                n.repeat(
                        n.alternation(
                                n.symbol(noEscaped),
                                n.sequence(
                                        n.symbol(reverseSolidus),
                                        n.alternation(
                                                n.symbol(quotationMark),
                                                n.symbol(reverseSolidus),
                                                n.symbol(solidus),
                                                n.symbol(bLower),
                                                n.symbol(fLower),
                                                n.symbol(nLower),
                                                n.symbol(rLower),
                                                n.symbol(tLower),
                                                n.sequence(
                                                        n.symbol(uLower),
                                                        n.exact(n.symbol(hexChar), 4)
                                                )
                                        )
                                )
                        )
                )
        );
        nString.append(n.symbol(quotationMark));

        // Entry
        nEntry.append(nString);
        nEntry.append(nWhitespace);
        nEntry.append(n.symbol(colon));
        nEntry.append(nValue);

        // Entry List
        nEntryList.append(nEntry);
        nEntryList.append(n.repeat(
                n.sequence(
                        n.symbol(comma),
                        nWhitespace,
                        nEntry
                )
        ));

        // Object
        nObject.append(n.symbol(braceOpen));
        nObject.append(nWhitespace);
        nObject.append(n.optional(nEntryList));
        nObject.append(nWhitespace);
        nObject.append(n.symbol(braceClose));

        // Array
        nArray.append(n.symbol(bracketOpen));
        nArray.append(n.alternation(
                nWhitespace,
                n.sequence(
                        nValue,
                        n.repeat(n.sequence(
                                n.symbol(comma),
                                nValue
                        ))
                )
        ));
        nArray.append(n.symbol(bracketClose));

        // Number
        nNumber.append(n.optional(n.symbol(minus)));
        nNumber.append(n.alternation(
                n.symbol(zero),
                n.sequence(
                        n.symbol(oneNine),
                        n.repeat(n.symbol(digit))
                )
        ));
        nNumber.append(n.optional(
                n.sequence(
                        n.symbol(point),
                        n.atLeast(1, n.symbol(digit))
                )
        ));
        nNumber.append(n.optional(
                n.sequence(
                        n.optional(n.alternation(n.symbol(eUpper), n.symbol(eLower))),
                        n.optional(n.alternation(n.symbol(minus), n.symbol(plus))),
                        n.atLeast(1, n.symbol(digit))
                )
        ));

        var nTrue = n.token("true");
        var nFalse = n.token("false");
        var nNull = n.token("null");

        // Value
        nValue.append(nWhitespace);
        nValue.append(n.alternation(
                nString,
                nNumber,
                nObject,
                nArray,
                nTrue,
                nFalse,
                nNull
        ));
        nValue.append(nWhitespace);
    }

    @Test
    public void jsonTest() {
        TestUtils.eval(nValue, new String[] {

                // objects
                "{}",
                "{\"abc\": \"def\"}",
                "{\"abc\": \"def\", \"ghi\": \"jkl\"}",
                "{\"abc\": \"def\", \"ghi\": {}}",
                "{\"abc\": {}, \"ghi\": [\"jkl\"]}",
                "{\"abc\": [], \"ghi\": {\"a\": [\"b\"]}}",

                // string
                "\"\"",
                "\"a\"",
                "\"ab\"",
                "\"abc\"",
                "\"abc\\\"\"",
                "\"abc\\\"d\\te\\u0000\"",

                // array
                "[]",
                "[[]]",
                "[\"\", {}, []]",
                "[\"a\", {\"b\": \"c\"}, [\"d\"], null, true, false]",
        });
    }

}
