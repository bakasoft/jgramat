package gramat.machine;

import gramat.badges.Badge;

import java.util.Stack;

public class Heap {

    private final Stack<Badge> stack;

    public Heap() {
        stack = new Stack<>();
    }

    public void push(Badge badge) {
        stack.push(badge);
    }

    public void pop() {
        stack.pop();
    }

}
