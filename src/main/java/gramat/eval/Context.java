package gramat.eval;

import gramat.eval.trx.TransactionID;
import gramat.eval.trx.TransactionManager;
import gramat.framework.Logger;
import gramat.input.Tape;

import java.util.Objects;
import java.util.Stack;

public class Context {

    public final Tape tape;

    public final Logger logger;

    private final Stack<Container> containerStack;

    private final TransactionManager transactions;

    private final Stack<String> callStack;

    private Integer beginPosition;

    public Context(Tape tape, Logger logger) {
        this.tape = tape;
        this.logger = logger;
        this.containerStack = new Stack<>();
        this.callStack = new Stack<>();
        this.transactions = new TransactionManager(logger, "root");
    }

    public TransactionManager transaction() {
        return transactions;
    }

    public void pushCall(String token) {
        logger.debug("push call: %s", token);

        callStack.push(token);
    }

    public void popCall(String token) {
        logger.debug("pop call: %s", token);

        if (callStack.isEmpty()) {
            throw new RejectedException();
        }

        var actual = callStack.pop();

        if (!Objects.equals(actual, token)) {
            throw new RejectedException("expected: " + token);
        }
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

    public TransactionID transactionID(int id) {
        var level = callStack.size();
        var token = callStack.size() > 0 ? callStack.peek() : null;

        return new TransactionID(id, level, token);
    }
}