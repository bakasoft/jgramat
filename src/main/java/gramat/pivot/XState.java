package gramat.pivot;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class XState {

    public final int id;
    public final XLanguage lang;

    public boolean wild;

    public XState original;

    public XState(XLanguage lang, int id) {
        this.lang = lang;
        this.id = id;
    }

    public void walk(Consumer<XTransition> consumer) {
        walk((state, transitions) -> {
            for (var transition : transitions) {
                consumer.accept(transition);
            }
        });
    }

    public void walk(BiConsumer<XState, List<XTransition>> consumer) {
        var control = new HashSet<XState>();
        var queue = new LinkedList<XState>();

        queue.add(this);

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                var transitions = lang.findTransitionsFrom(state);

                consumer.accept(state, transitions);

                for (var transition : transitions) {
                    queue.add(transition.target);
                }
            }
        }
    }

    public XState shallowCopy() {
        var copy = lang.createState();
        copy.wild = this.wild;
        copy.original = this;
        return copy;
    }

    public Set<XState> computeEmptyClosure() {
        return lang.computeEmptyClosure(Set.of(this));
    }

    public Set<XState> computeEmptyInverseClosure() {
        return lang.computeEmptyInverseClosure(Set.of(this));
    }

    public static String computeID(Set<XState> states) {
        var ids = states.stream()
                .mapToInt(s -> s.id).toArray();

        Arrays.sort(ids);

        var uid = new StringBuilder();

        for (int i = 0; i < ids.length; i++) {
            if (i > 0) {
                uid.append("_");
            }
            uid.append(ids[i]);
        }

        return uid.toString();
    }

    public XState getOrigin() {
        if (original == null) {
            return this;
        }
        return original.getOrigin();
    }
}
