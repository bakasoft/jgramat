package gramat.automata.ndfa;

import java.util.Collection;
import java.util.List;

public interface NContainer {

    NState state();

    NTransition transition(NState source, NState targets, Symbol symbol);

    default void transition(Collection<NState> sources, Collection<NState> targets, Symbol symbol) {
        for (var source : sources) {
            for (var target : targets) {
                transition(source, target, symbol);
            }
        }
    }

    default void transitionNull(NState source, NState target) {
        transition(List.of(source), List.of(target), null);
    }

    default void transitionNull(List<NState> sources, NState target) {
        transition(sources, List.of(target), null);
    }

    default void transitionNull(NState source, List<NState> targets) {
        transition(List.of(source), targets, null);
    }

    default void transitionNull(List<NState> sources, List<NState> targets) {
        transition(sources, targets, null);
    }

    default void transitionChar(NState source, NState target, int value) {
        transition(List.of(source), List.of(target), new SymbolChar(value));
    }

    default void transitionRange(NState source, NState target, int begin, int end) {
        transition(List.of(source), List.of(target), new SymbolRange(begin, end));
    }

    default void transitionWild(NState source, NState target) {
        transition(List.of(source), List.of(target), new SymbolWild());
    }
}
