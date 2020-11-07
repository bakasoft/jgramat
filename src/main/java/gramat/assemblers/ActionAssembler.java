package gramat.assemblers;

import gramat.actions.*;
import gramat.badges.BadgeToken;
import gramat.eval.transactions.Transaction;
import gramat.exceptions.UnsupportedValueException;
import gramat.models.automata.ModelAction;
import gramat.models.automata.ModelActionRecursion;
import gramat.models.automata.ModelActionTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ActionAssembler {

    private static final Action[] EMPTY = new Action[0];

    private final TransactionAssembler transactions;
    private final BadgeAssembler badges;

    public ActionAssembler(TransactionAssembler transactions, BadgeAssembler badges) {
        this.badges = badges;
        this.transactions = transactions;
    }

    public Action[] build(List<ModelAction> models) {
        if (models == null) {
            return EMPTY;
        }

        return models.stream().map(this::build).toArray(Action[]::new);
    }

    public Action build(ModelAction data) {
        if (data instanceof ModelActionRecursion) {
            return build((ModelActionRecursion)data);
        }
        else if (data instanceof ModelActionTransaction) {
            return build((ModelActionTransaction)data);
        }
        else {
            throw new UnsupportedValueException(data);
        }
    }

    public Action build(ModelActionRecursion data) {
        var badge = badges.build(data.badge);

        if (!(badge instanceof BadgeToken)) {
            throw new UnsupportedValueException(badge);
        }

        if (Objects.equals(data.type, "enter")) {
            return new RecursionEnter((BadgeToken) badge);
        } else if (Objects.equals(data.type, "exit")) {
            return new RecursionExit((BadgeToken) badge);
        }
        else {
            throw new UnsupportedValueException(data.type, "action type");
        }
    }

    public Action build(ModelActionTransaction data) {
        var trx = transactions.build(data.transaction);

        if (Objects.equals(data.type, "begin")) {
            return new BeginAction(trx);
        }
        else if (Objects.equals(data.type, "end")) {
            return new EndAction(trx);
        }
        else if (Objects.equals(data.type, "not-begin")) {
            return new NotBeginAction(trx);
        }
        else if (Objects.equals(data.type, "not-end")) {
            return new NotEndAction(trx);
        }
        else {
            throw new RuntimeException("Unsupported action: " + data.type);
        }
    }
}
