package gramat.pipeline.encoding;

import gramat.scheme.common.actions.*;
import gramat.scheme.common.actions.transactions.*;
import gramat.scheme.common.badges.Badge;
import gramat.scheme.common.badges.BadgeToken;
import gramat.scheme.common.badges.BadgeWild;
import gramat.eval.transactions.Transaction;
import gramat.exceptions.UnsupportedValueException;
import gramat.scheme.State;
import gramat.scheme.data.automata.*;
import gramat.scheme.common.symbols.*;

import java.util.*;

public class MachineEncoder {

    // TODO Separate this file

    private final Map<Badge, BadgeData> badges;
    private final Map<Transaction, TransactionData> transactions;

    public MachineEncoder() {
        badges = new LinkedHashMap<>();
        transactions = new LinkedHashMap<>();
    }

    public MachineData createMachine(State initial) {
        var machine = new MachineData();
        var states = new LinkedHashMap<State, StateData>();
        var symbols = new LinkedHashMap<Symbol, SymbolData>();
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

                        var mTran = new TransitionData();
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

    private BadgeData makeBadge(Badge badge) {
        return badges.computeIfAbsent(badge, this::createBadge);
    }

    private TransactionData makeTransaction(Transaction transaction) {
        return transactions.computeIfAbsent(transaction, this::createTransaction);
    }

    private List<ActionData> createActions(Action[] actions) {
        var data = new ArrayList<ActionData>();
        if (actions != null) {
            for (var action : actions) {
                data.add(createAction(action));
            }
        }
        return data;
    }

    private ActionData createAction(Action action) {
        if (action instanceof RecursionEnter) {
            var result = new ActionRecursionData();
            result.type = "enter";
            result.badge = makeBadge(((RecursionEnter) action).badge);
            return result;
        }
        else if (action instanceof RecursionExit) {
            var result = new ActionRecursionData();
            result.type = "exit";
            result.badge = makeBadge(((RecursionExit) action).badge);
            return result;
        }
        else if (action instanceof ActionTransaction) {
            var tran = makeTransaction(((ActionTransaction) action).getTransaction());
            if (action instanceof BeginAction) {
                var result = new ActionTransactionData();
                result.type = "begin";
                result.transaction = tran;
                return result;
            }
            else if (action instanceof EndAction) {
                var result = new ActionTransactionData();
                result.type = "end";
                result.transaction = tran;
                return result;
            }
            else if (action instanceof NotBeginAction) {
                var result = new ActionTransactionData();
                result.type = "not-begin";
                result.transaction = tran;
                return result;
            }
            else if (action instanceof NotEndAction) {
                var result = new ActionTransactionData();
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

    private TransactionData createTransaction(Transaction tran) {
        var data = new TransactionData();

        data.id = tran.getID();

        if (tran instanceof ArrayTransaction) {
            data.type = "array";
            data.typeHint = ((ArrayTransaction) tran).getTypeHint();
        }
        else if (tran instanceof AttributeTransaction) {
            data.type = "attribute";
            data.defaultName = ((AttributeTransaction) tran).getDefaultName();
        }
        else if (tran instanceof NameTransaction) {
            data.type = "name";
        }
        else if (tran instanceof ObjectTransaction) {
            data.type = "object";
            data.typeHint = ((ObjectTransaction) tran).getTypeHint();
        }
        else if (tran instanceof ValueTransaction) {
            data.type = "value";
            data.parserName = ((ValueTransaction) tran).getParser().getName();
        }
        else {
            throw new UnsupportedValueException(tran);
        }

        return data;
    }

    public StateData createState(State state) {
        var data = new StateData();
        data.id = state.id;
        data.accepted = state.accepted;
        return data;
    }

    public BadgeData createBadge(Badge badge) {
        var data = new BadgeData();
        if (badge instanceof BadgeToken) {
            data.token = ((BadgeToken) badge).token;
        }
        else if (badge instanceof BadgeWild) {
            data.wild = true;
        }
        else {
            throw new UnsupportedValueException(badge);
        }
        return data;
    }

    public SymbolData createSymbol(Symbol symbol) {
        if (symbol instanceof SymbolChar) {
            var data = new SymbolCharData();
            data.value = ((SymbolChar) symbol).value;
            return data;
        }
        else if (symbol instanceof SymbolRange) {
            var range = (SymbolRange)symbol;
            var data = new SymbolRangeData();
            data.begin = range.begin;
            data.end = range.end;
            return data;
        }
        else if (symbol instanceof SymbolWild) {
            return new SymbolWildData();
        }
        else {
            throw new UnsupportedValueException(symbol);
        }
    }

}
