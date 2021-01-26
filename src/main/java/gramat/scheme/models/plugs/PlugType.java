package gramat.scheme.models.plugs;

import gramat.scheme.models.Link;
import gramat.scheme.models.Root;

public enum PlugType {

    SOURCE_TO_SOURCE,
    SOURCE_TO_NODE,
    SOURCE_TO_TARGET,
    NODE_TO_SOURCE,
    NODE_TO_NODE,
    NODE_TO_TARGET,
    TARGET_TO_SOURCE,
    TARGET_TO_NODE,
    TARGET_TO_TARGET;

    public static PlugType compute(Root root, Link link) {
        if (link.source == root.source) {  // From GrammarData
            if (link.target == root.source) {  // To GrammarData
                return SOURCE_TO_SOURCE;
            }
            else if (root.targets.contains(link.target)) {  // To Target
                return SOURCE_TO_TARGET;
            }
            else {  // To Node
                return SOURCE_TO_NODE;
            }
        }
        else if (root.targets.contains(link.source)) {  // From Target
            if (link.target == root.source) {  // To GrammarData
                return TARGET_TO_SOURCE;
            }
            else if (root.targets.contains(link.target)) {  // To Target
                return TARGET_TO_TARGET;
            }
            else {  // To Node
                return TARGET_TO_NODE;
            }
        }
        else {  // From Node
            if (link.target == root.source) {  // To GrammarData
                return NODE_TO_SOURCE;
            }
            else if (root.targets.contains(link.target)) {  // To Target
                return NODE_TO_TARGET;
            }
            else {  // To Node
                return NODE_TO_NODE;
            }
        }
    }
}