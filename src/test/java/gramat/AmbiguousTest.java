package gramat;

import gramat.nodes.NodeFactory;
import gramat.symbols.SymbolFactory;
import tools.TestUtils;
import org.junit.Test;

public class AmbiguousTest {

    @Test
    public void test() {
        var symbols = new SymbolFactory();
        var nodes = new NodeFactory();
        var s0 = symbols.literal('0');
        var s1 = symbols.literal('1');

        var nStatus1 = nodes.sequence(
                nodes.symbol(s0),
                nodes.symbol(s0),
                nodes.symbol(s0)
        );

        var nStatus2 = nodes.sequence(
                nodes.symbol(s0),
                nodes.symbol(s0),
                nodes.symbol(s1)
        );

        var nStatus3 = nodes.sequence(
                nodes.symbol(s0),
                nodes.symbol(s1),
                nodes.symbol(s0)
        );

        var nStatus4 = nodes.sequence(
                nodes.symbol(s0),
                nodes.symbol(s1),
                nodes.symbol(s1)
        );

        var nStatus5 = nodes.sequence(
                nodes.symbol(s1),
                nodes.symbol(s0),
                nodes.symbol(s0)
        );

        var nStatus6 = nodes.sequence(
                nodes.symbol(s1),
                nodes.symbol(s0),
                nodes.symbol(s1)
        );

        var nStatus7 = nodes.sequence(
                nodes.symbol(s1),
                nodes.symbol(s1),
                nodes.symbol(s0)
        );

        var nStatus8 = nodes.sequence(
                nodes.symbol(s1),
                nodes.symbol(s1),
                nodes.symbol(s1)
        );

        var nStatus = nodes.alternation(
                nStatus1,
                nStatus2,
                nStatus3,
                nStatus4,
                nStatus5,
                nStatus6,
                nStatus7,
                nStatus8
        );

        TestUtils.eval(nStatus, new String[]{
                "000",
                "001",
                "010",
                "011",
                "100",
                "101",
                "110",
                "111",
        });
    }

}
