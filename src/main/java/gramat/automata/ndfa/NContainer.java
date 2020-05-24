package gramat.automata.ndfa;

public interface NContainer {

    NState state();

    NTransition transition(NState source, NState targets, Symbol symbol);

    default void transition(NStateSet sources, NStateSet targets, Symbol symbol) {
        if (sources.isEmpty()) {
            throw new RuntimeException("Missing source states");
        }
        else if (targets.isEmpty()) {
            throw new RuntimeException("Missing target states");
        }

        for (var source : sources) {
            for (var target : targets) {
                transition(source, target, symbol);
            }
        }
    }

    default void transitionChar(NStateSet sources, NState target, int value) {
        transition(sources, NStateSet.of(target), new SymbolChar(value));
    }

    default void transitionChar(NStateSet sources, NStateSet targets, int value) {
        transition(sources, targets, new SymbolChar(value));
    }

    default void transitionRange(NStateSet sources, NStateSet targets, int begin, int end) {
        transition(sources, targets, new SymbolRange(begin, end));
    }

    default void transitionWild(NState source, NStateSet targets) {
        transitionWild(NStateSet.of(source), targets);
    }

    default void transitionWild(NStateSet sources, NStateSet targets) {
        transition(sources, targets, new SymbolWild());
    }
}
