package gramat.machine;

import gramat.badges.Badge;
import gramat.symbols.Symbol;
import gramat.symbols.SymbolMatcher;

import java.util.*;

public class Transition {

    private final Map<Badge, SymbolMatcher<Effect>> badges;

    public Transition() {
        badges = new LinkedHashMap<>();
    }

    public Set<Badge> getBadges() {
        return badges.keySet();
    }

    public Set<Symbol> getSymbols(Badge badge) {
        var symbols = badges.get(badge);

        if (symbols == null) {
            return Set.of();
        }

        return symbols.getSymbols();
    }

    public Effect getEffect(Badge badge, Symbol symbol) {
        var symbols = badges.get(badge);

        if (symbols == null) {
            return null;
        }

        return symbols.get(symbol);
    }

    public void add(Badge badge, Symbol symbol, Effect effect) {
        var symbolMap = badges.computeIfAbsent(badge, k -> new SymbolMatcher<>());

        symbolMap.add(symbol, effect);
    }

    public Effect match(Badge badge, char chr) {
        var fallback = (SymbolMatcher<Effect>)null;

        for (var entry : badges.entrySet()) {
            var entryBadge = entry.getKey();
            var entryTransition = entry.getValue();

            if (entryBadge.isWild()) {
                fallback = entryTransition;
            }
            else if (entryBadge == badge) {
                var effect = entryTransition.match(chr);

                if (effect != null) {
                    return effect;
                }
            }
        }

        if (fallback == null) {
            return null;
        }

        return fallback.match(chr);
    }


}
