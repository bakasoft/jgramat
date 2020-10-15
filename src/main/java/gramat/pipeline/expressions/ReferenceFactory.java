package gramat.pipeline.expressions;

import gramat.badges.BadgeMode;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelReference;
import gramat.symbols.Symbol;
import gramat.util.Chain;

public interface ReferenceFactory extends BaseFactory {

    default Chain<Node> compileReference(Graph graph, Node source, Node target, ModelReference reference) {
        var symbol = getAlphabet().reference(reference.name);
        var badge = getEmptyBadge();

        graph.createLink(source, target, symbol, badge);

        registerDependency(reference.name);

        return Chain.of(target);
    }

}
