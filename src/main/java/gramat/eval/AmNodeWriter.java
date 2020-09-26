package gramat.eval;

import gramat.am.formatting.AmFormatter;

import java.util.*;

public class AmNodeWriter {

    public static void write(State initial, AmFormatter writer) {
        var nodes = list_nodes(initial);

        for (var node : nodes) {
            var source = node.id;

            if (node == initial) {
                writer.initial(source);
            }

            for (var link : list_links(node)) {
                var target = link.target.id;
                var symbol = link.symbol;

                writer.transition(source, target);

                if (link.before != null) {
                    for (var action : link.before) {
                        writer.action(action.getKey(), action.getArguments());
                    }
                }

                writer.symbol(symbol);

                if (link.after != null) {
                    for (var action : link.after) {
                        writer.action(action.getKey(), action.getArguments());
                    }
                }

                writer.end();
            }

            if (node.isAccepted()) {
                writer.accepted(node.id);
            }

            writer.newLine();
        }
    }

    private static List<Transition> list_links(State state) {
        var hashLink = new HashMap<String, Transition>();

        for (var link : state) {
            hashLink.put(link.target.id + "/" + link.symbol + "/" + link.hashCode(), link);
        }

        var hashes = new ArrayList<>(hashLink.keySet());

        Collections.sort(hashes);

        var result = new ArrayList<Transition>();

        for (var hash : hashes) {
            var link = hashLink.get(hash);

            result.add(link);
        }

        return result;
    }

    private static List<State> list_nodes(State root) {
        var control = new HashSet<State>();
        var queue = new LinkedList<State>();
        var idNodes = new HashMap<String, State>();

        queue.add(root);

        while (queue.size() > 0) {
            var node = queue.remove();

            if (control.add(node)) {
                if (idNodes.containsKey(node.id)) {
                    throw new RuntimeException();
                }

                idNodes.put(node.id, node);

                for (var link : node) {
                    queue.add(link.target);
                }
            }
        }

        var ids = new ArrayList<>(idNodes.keySet());

        Collections.sort(ids);

        var result = new ArrayList<State>();

        for (var id : ids) {
            var node = idNodes.get(id);

            result.add(node);
        }

        return result;
    }

}
