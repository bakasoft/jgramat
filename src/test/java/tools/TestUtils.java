package tools;

import gramat.Context;
import gramat.InvalidSyntaxException;
import gramat.input.Tape;
import gramat.nodes.Node;
import gramat.nodes.NodeContext;
import gramat.source.Decompiler;
import gramat.source.formatting.SourceWriter;

public class TestUtils {

    public static void eval(Node node, String[] inputs) {
        var collapsedNode = node.collapse(new NodeContext());
        var compiledNode = collapsedNode.compile(new NodeContext());

        for (var input : inputs) {
            System.out.println("EVAL " + node + ": " + input);

            var tape = new Tape(input);
            var context = new Context(tape);

            try {
                compiledNode.eval(context);

                if (tape.alive()) {
                    throw new RuntimeException("unexepcted EOF");
                }
            }
            catch(InvalidSyntaxException e) {
                var decompiler = new Decompiler();
                decompiler.decompile(compiledNode, "main");

                decompiler.getSource().write(new SourceWriter());

                throw new AssertionError(e);
            }
        }
    }

}
