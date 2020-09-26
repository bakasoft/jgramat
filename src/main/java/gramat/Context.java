package gramat;

import gramat.capturing.Assembler;
import gramat.input.Tape;

import java.util.Stack;

public class  Context {

    public final Tape tape;

    private final Stack<Assembler> assemblerStack;
    private final Stack<Integer> beginStack;

    public Context(Tape tape) {
        this.tape = tape;
        this.assemblerStack = new Stack<>();
        this.beginStack = new Stack<>();

        pushAssembler();
    }

    public void pushBegin() {
        beginStack.push(tape.getPosition());
    }

    public int popBegin() {
        return beginStack.pop();
    }

    public void pushAssembler() {
        Debug.log("Push assembler", +1);
        assemblerStack.add(new Assembler());
    }

    public Assembler peekAssembler() {
        if (assemblerStack.isEmpty()) {
            throw new RuntimeException();
        }

        return assemblerStack.peek();
    }

    public Assembler popAssembler() {
        Debug.log(-1, "Pop assembler");

        if (assemblerStack.isEmpty()) {
            throw new RuntimeException();
        }

        return assemblerStack.pop();
    }

}
