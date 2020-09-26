package gramat.eval;

import gramat.eval.trx.TransactionManager;
import gramat.framework.Logger;
import gramat.input.Tape;

import java.util.Objects;
import java.util.Stack;

public class Context {

    public final Tape tape;

    public final Logger logger;

    private final Stack<String> callStack;

    private final Stack<Container> containerStack;

    public final TransactionManager transactions;

    private Integer beginPosition;

    public Context(Tape tape, Logger logger) {
        this.tape = tape;
        this.logger = logger;
        this.callStack = new Stack<>();
        this.containerStack = new Stack<>();
        this.transactions = new TransactionManager();
    }

    public void pushCall(String token) {
        logger.debug("push call %s", token);

        callStack.push(token);
    }

    public void popCall(String token) {
        logger.debug("pop call %s", token);

        if (callStack.isEmpty()) {
            throw new RejectedException();
        }

        var actual = callStack.pop();

        if (!Objects.equals(token, actual)) {
            throw new RejectedException("expected: " + token);
        }
    }

    public void pushBegin(int position) {
        logger.debug("push begin %s", position);

        if (beginPosition != null) {
            throw new RuntimeException();
        }
        beginPosition = position;
    }

    public int popBegin() {
        logger.debug("pop begin %s", beginPosition);

        var position = beginPosition;
        if (position == null) {
            throw new RuntimeException();
        }
        beginPosition = null;
        return position;
    }

    public void pushValue(Object value) {
        containerStack.peek().pushValue(value);
    }

    public void pushContainer() {
        logger.debug("push container");

        containerStack.push(new Container(this));
    }

    public Container popContainer() {
        logger.debug("pop container");

        return containerStack.pop();
    }

    public Container peekContainer() {
        return containerStack.peek();
    }
}