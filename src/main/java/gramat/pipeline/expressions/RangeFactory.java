package gramat.pipeline.expressions;

import gramat.badges.BadgeMode;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelRange;
import gramat.symbols.Symbol;
import gramat.util.Chain;

public interface RangeFactory extends BaseFactory {

    default Chain<Node> compileRange(Graph graph, Node source, Node target, ModelRange range) {
        var symbol = getAlphabet().range(range.begin, range.end);

        graph.createLink(source, target, symbol, getEmptyBadge());

        return Chain.of(target);
    }

}
