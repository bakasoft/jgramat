package gramat.models.factories;

import gramat.actions.*;
import gramat.actions.transactions.*;
import gramat.badges.Badge;
import gramat.badges.BadgeToken;
import gramat.badges.BadgeWild;
import gramat.eval.transactions.Transaction;
import gramat.exceptions.UnsupportedValueException;
import gramat.machine.State;
import gramat.models.automata.*;
import gramat.pipeline.Machine;
import gramat.symbols.*;
import gramat.util.DataUtils;

import java.util.*;

public class MachineFactory {// TODO Rename to MachineModeler or MachineDecompiler or MachineDisassembler

    private final Map<Badge, ModelBadge> badges;
    private final Map<Transaction, ModelTransaction> transactions;

    public MachineFactory() {
        badges = new LinkedHashMap<>();
        transactions = new LinkedHashMap<>();
    }

    public ModelMachine createMachine(State initial) {
        var machine = new ModelMachine();
        var states = new LinkedHashMap<State, ModelState>();
        var symbols = new LinkedHashMap<Symbol, ModelSymbol>();
        var control = new HashSet<State>();
        var queue = new LinkedList<State>();

        machine.transitions = new ArrayList<>();

        queue.add(initial);

        while (!queue.isEmpty()) {
            var state = queue.remove();

            if (control.add(state)) {
                var mSource = states.computeIfAbsent(state, this::createState);

                for (var badge : state.transition.getBadges()) {
                    var mBadge = makeBadge(badge);

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

                        queue.add(effect.target);
                    }
                }
            }
        }

        machine.initial = states.get(initial);
        machine.states = new ArrayList<>(states.values());

        return machine;
    }

    private ModelBadge makeBadge(Badge badge) {
        return badges.computeIfAbsent(badge, this::createBadge);
    }

    private ModelTransaction makeTransaction(Transaction transaction) {
        return transactions.computeIfAbsent(transaction, this::createTransaction);
    }

    private List<ModelAction> createActions(Action[] actions) {
        var models = new ArrayList<ModelAction>();
        if (actions != null) {
            for (var action : actions) {
                models.add(createAction(action));
            }
        }
        return models;
    }

    private ModelAction createAction(Action action) {
        if (action instanceof RecursionEnter) {
            var result = new ModelActionRecursion();
            result.type = "enter";
            result.badge = makeBadge(((RecursionEnter) action).badge);
            return result;
        }
        else if (action instanceof RecursionExit) {
            var result = new ModelActionRecursion();
            result.type = "exit";
            result.badge = makeBadge(((RecursionExit) action).badge);
            return result;
        }
        else if (action instanceof ActionTransaction) {
            var tran = makeTransaction(((ActionTransaction) action).getTransaction());
            if (action instanceof BeginAction) {
                var result = new ModelActionTransaction();
                result.type = "begin";
                result.transaction = tran;
                return result;
            }
            else if (action instanceof EndAction) {
                var result = new ModelActionTransaction();
                result.type = "end";
                result.transaction = tran;
                return result;
            }
            else if (action instanceof NotBeginAction) {
                var result = new ModelActionTransaction();
                result.type = "not-begin";
                result.transaction = tran;
                return result;
            }
            else if (action instanceof NotEndAction) {
                var result = new ModelActionTransaction();
                result.type = "not-end";
                result.transaction = tran;
                return result;
            }
            else {
                throw new UnsupportedValueException(action);
            }
        }
        else {
            throw new UnsupportedValueException(action);
        }
    }

    private ModelTransaction createTransaction(Transaction tran) {
        var model = new ModelTransaction();

        model.id = tran.getID();

        if (tran instanceof ArrayTransaction) {
            model.type = "array";
            model.typeHint = ((ArrayTransaction) tran).getTypeHint();
        }
        else if (tran instanceof AttributeTransaction) {
            model.type = "attribute";
            model.defaultName = ((AttributeTransaction) tran).getDefaultName();
        }
        else if (tran instanceof NameTransaction) {
            model.type = "name";
        }
        else if (tran instanceof ObjectTransaction) {
            model.type = "object";
            model.typeHint = ((ObjectTransaction) tran).getTypeHint();
        }
        else if (tran instanceof ValueTransaction) {
            model.type = "value";
            model.parserName = ((ValueTransaction) tran).getParser().getName();
        }
        else {
            throw new UnsupportedValueException(tran);
        }

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
