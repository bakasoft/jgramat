package gramat.engine.indet;

import gramat.GramatException;
import gramat.engine.AmCode;
import gramat.engine.deter.DState;
import gramat.engine.deter.DTransition;
import gramat.engine.nodet.NLanguage;
import gramat.engine.nodet.NMachine;
import gramat.engine.nodet.NStateList;
import gramat.engine.symbols.SymbolSource;

import java.util.*;

public class ICompiler {

    private final ILanguage iLang;

    public ICompiler() {
        this.iLang = new ILanguage();
    }

    public ICompiler(ILanguage iLang) {
        this.iLang = iLang;
    }

    public IState convertToDFA(NLanguage nLang, SymbolSource symbols, NMachine source) {
        // convert NDFA to DFA
        var queue = new LinkedList<NStateList>();
        var control = new HashSet<String>();
        var initialClosure = source.initial.getEmptyClosure();

        queue.add(initialClosure);

        do {
            var oldSources = queue.remove();

            if (control.add(oldSources.computeID())) {
                for (var symbol : symbols) {
                    var oldTransitions = nLang.findTransitionsFrom(oldSources, symbol);

                    if (oldTransitions.size() > 0) {
                        // get empty closure of all targets
                        var oldTargets = oldTransitions.collectTargets().getEmptyClosure();

                        if (oldTargets.size() > 0) {
                            var newSource = iLang.mergeState(oldSources);
                            var newTarget = iLang.mergeState(oldTargets);

                            iLang.createTransition(newSource, newTarget, symbol);

                            queue.add(oldTargets);
                        }
                    }
                }
            }
        } while (queue.size() > 0);

        // retrieve initial state
        var initial = iLang.lookupStateByOrigin(initialClosure);

        if (initial == null) {
            throw new GramatException("cannot find initial state");
        }

        // mark accepted states
        for (var state : iLang.states) {
            if (state.origin.contains(source.accepted)) {
                state.accepted = true;
            }
        }

        // distribute actions
        for (var iTran : iLang.transitions) {
            for (var nTran : nLang.transitions) {
                // skip not matching symbols (null symbols actually matches)
                if (nTran.symbol != null && !Objects.equals(iTran.symbol, nTran.symbol)) {
                    continue;
                }
                // skip not matching directions
                else if (!iTran.source.origin.contains(nTran.source) || !iTran.target.origin.contains(nTran.target)) {
                    continue;
                }

                // apply actions
                iTran.actions.addAll(nTran.actions);
            }
        }

        System.out.println("I-DFA >>>>>>>>>>");
        AmCode.write(System.out, iLang, initial);
        System.out.println("<<<<<<<<<< I-DFA");

        return initial;
    }

    public DState compile(IState initial) {
        var stateMap = new HashMap<IState, DState>();

        return make_deter(initial, stateMap);
    }

    private DState make_deter(IState iState, Map<IState, DState> map) {
        var dState = map.get(iState);

        if (dState != null) {
            return dState;
        }

        // map state
        dState = new DState(map.size(), iState.accepted);

        map.put(iState, dState);

        // map transitions
        var iTrans = iLang.findTransitionsBySource(iState);

        if (iTrans.size() > 0) {
            var dTrans = new DTransition[iTrans.size()];

            for (var i = 0; i < iTrans.size(); i++) {
                var iTran = iTrans.get(i);
                var dTarget = make_deter(iTran.target, map);

                dTrans[i] = new DTransition(iTran.symbol, dTarget, iTran.actions.toArray());
            }

            dState.transitions = dTrans;
        }

        return dState;
    }

}
