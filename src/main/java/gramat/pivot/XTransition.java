package gramat.pivot;

import gramat.pivot.data.TDSymbol;
import gramat.symbols.Symbol;

import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Set;

public class XTransition {

    public final XLanguage lang;

    public XState source;
    public XState target;
    public XTransitionData data;

    public XTransition(XLanguage lang, XState source, XState target, XTransitionData data) {
        this.lang = lang;
        this.source = source;
        this.target = target;
        this.data = data;
    }

    public void delete() {
        source = null;
        target = null;
        data = null;

        lang.transitions.remove(this);
    }

    public XTransition shallowCopy(XState newSource, XState newTarget) {
        return lang.createTransition(newSource, newTarget, data);
    }

    public void printAmCode(PrintStream out) {
        out.print(source.id);
        out.print(" -> ");
        out.print(target.id);

        var tds = (TDSymbol)null;

        if (data != null) {
            if (data instanceof TDSymbol) {
                out.print(" : ");
                tds = (TDSymbol)data;
            } else {
                out.print(" ! ");
            }
            data.printAmCode(out);
        }
        out.println();

        if (tds != null) {
            for (var action : tds.preActions) {
                out.print(source.id);
                out.print(" -> ");
                out.print(target.id);
                out.print(" !< ");
                action.printAmCode(out);
                out.println();
            }

            for (var action : tds.postActions) {
                out.print(source.id);
                out.print(" -> ");
                out.print(target.id);
                out.print(" !> ");
                action.printAmCode(out);
                out.println();
            }
        }
    }

    public static Set<XState> collectTargets(Iterable<XTransition> transitions) {
        var targets = new LinkedHashSet<XState>();

        for (var transition : transitions) {
            targets.add(transition.target);
        }

        return targets;
    }

    public static Set<XState> collectSources(Iterable<XTransition> transitions) {
        var targets = new LinkedHashSet<XState>();

        for (var transition : transitions) {
            targets.add(transition.source);
        }

        return targets;
    }

    public Symbol getSymbol() {
        if (!(data instanceof TDSymbol)) {
            throw new RuntimeException();
        }

        return ((TDSymbol)data).symbol;
    }
}
