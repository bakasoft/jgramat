package gramat.models.factories;

import gramat.actions.Action;
import gramat.badges.Badge;
import gramat.badges.BadgeToken;
import gramat.badges.BadgeWild;
import gramat.exceptions.UnsupportedValueException;
import gramat.machine.State;
import gramat.models.automata.*;
import gramat.symbols.*;

import java.util.*;

public class MachineFactory {

    public ModelMachine createMachine(State initial) {
        var machine = new ModelMachine();
        var states = new HashMap<State, ModelState>();
        var badges = new HashMap<Badge, ModelBadge>();
        var symbols = new HashMap<Symbol, ModelSymbol>();
        var control = new HashSet<State>();
        var queue = new LinkedList<State>();

        queue.add(initial);

        while (!queue.isEmpty()) {
            var state = queue.remove();

            if (control.add(state)) {
                var mSource = states.computeIfAbsent(state, this::createState);

                for (var badge : state.transition.getBadges()) {
                    var mBadge = badges.computeIfAbsent(badge, this::createBadge);

                    for (var symbol : state.transition.getSymbols(badge)) {
                        var mSymbol = symbols.computeIfAbsent(symbol, this::createSymbol);
                        var effect = state.transition.getEffect(badge, symbol);
                        var mTarget = states.computeIfAbsent(effect.target, this::createState);

                        var mTran = new ModelTransition();
                        mTran.source = mSource;
                        mTran.target = mTarget;
                        mTran.badge = mBadge;
                        mTran.symbol = mSymbol;
                        mTran.preActions = createActions(effect.before);
                        mTran.postActions = createActions(effect.after);

                        machine.transitions.add(mTran);
                    }
                }
            }
        }

        machine.initial = states.get(initial);

        return machine;
    }

    private List<ModelAction> createActions(Action[] actions) {
        var models = new ArrayList<ModelAction>();
        for (var action : actions) {
            models.add(createAction(action));
        }
        return models;
    }

    private ModelAction createAction(Action action) {
        var model = new ModelAction();
        model.name = action.getName();
        model.arguments = action.getArguments();
        return model;
    }

    public ModelState createState(State state) {
        var model = new ModelState();
        model.id = state.id;
        model.accepted = state.accepted;
        return model;
    }

    public ModelBadge createBadge(Badge badge) {
        var model = new ModelBadge();
        if (badge instanceof BadgeToken) {
            model.token = ((BadgeToken) badge).token;
        }
        else if (badge instanceof BadgeWild) {
            model.wild = true;
        }
        else {
            throw new UnsupportedValueException(badge);
        }
        return model;
    }

    public ModelSymbol createSymbol(Symbol symbol) {
        if (symbol instanceof SymbolChar) {
            var model = new ModelSymbolChar();
            model.value = ((SymbolChar) symbol).value;
            return model;
        }
        else if (symbol instanceof SymbolRange) {
            var range = (SymbolRange)symbol;
            var model = new ModelSymbolRange();
            model.begin = range.begin;
            model.end = range.end;
            return model;
        }
        else if (symbol instanceof SymbolWild) {
            return new ModelSymbolWild();
        }
        else {
            throw new UnsupportedValueException(symbol);
        }
    }

}
