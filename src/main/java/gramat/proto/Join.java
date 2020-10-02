package gramat.proto;

import gramat.actions.ActionStore;

import java.util.Objects;

public class Join {

    public final Token token;
    public final ActionStore beforeActions;
    public final ActionStore afterActions;

    public Join(Token token, ActionStore beforeActions, ActionStore afterActions) {
        this.token = Objects.requireNonNull(token);
        this.beforeActions = beforeActions;
        this.afterActions = afterActions;
    }
}
