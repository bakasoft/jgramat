package gramat.formatting;

import gramat.badges.Badge;
import gramat.machine.State;
import gramat.symbols.Symbol;
import gramat.util.StringUtils;

import java.util.*;

public class StateFormatter extends AmFormatter {

    public StateFormatter(Appendable output) {
        super(output);
    }

    public void write(State initial) {
        for (var source : listStates(initial)) {
            if (source == initial) {
                raw("->");
                sp();
                raw(source.id);
                ln();
            }

            for (var badge : listBadges(source)) {
                for (var symbol : listSymbols(source, badge)) {
                    var effect = source.transition.getEffect(badge, symbol);
                    var target = effect.target;
                    var before = StringUtils.join("\n", effect.before);
                    var after = StringUtils.join("\n", effect.after);

                    if (before.length() > 0) {
                        raw(source.id);
                        sp();
                        raw("->");
                        sp();
                        raw(target.id);
                        sp();
                        raw("!<");
                        sp();
                        amstr(before);
                        ln();
                    }

                    raw(source.id);
                    sp();
                    raw("->");
                    sp();
                    raw(target.id);
                    sp();
                    raw(":");
                    sp();
                    amstr(symbol.toString());
                    raw("/");
                    amstr(badge.toString());
                    ln();

                    if (after.length() > 0) {
                        raw(source.id);
                        sp();
                        raw("->");
                        sp();
                        raw(target.id);
                        sp();
                        raw("!>");
                        sp();
                        amstr(after);
                        ln();
                    }
                }
            }

            if (source.accepted) {
                raw(source.id);
                sp();
                raw("<=");
                ln();
            }
        }
    }

    private static List<Badge> listBadges(State state) {
        var badges = new ArrayList<>(state.transition.getBadges());

        badges.sort(Comparator.comparing(Object::toString));

        return badges;
    }

    private static List<Symbol> listSymbols(State state, Badge badge) {
        var symbols = new ArrayList<>(state.transition.getSymbols(badge));

        symbols.sort(Comparator.comparing(Object::toString));

        return symbols;
    }

    private static List<State> listStates(State root) {
        var control = new HashSet<State>();
        var queue = new LinkedList<State>();
        var idNodes = new HashMap<String, State>();

        queue.add(root);

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                if (idNodes.containsKey(state.id)) {
                    throw new RuntimeException();
                }

                idNodes.put(state.id, state);

                for (var badge : state.transition.getBadges()) {
                    for (var symbol : state.transition.getSymbols(badge)) {
                        var effect = state.transition.getEffect(badge, symbol);

                        queue.add(effect.target);
                    }
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
