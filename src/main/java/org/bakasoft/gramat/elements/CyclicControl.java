package org.bakasoft.gramat.elements;

import java.util.Stack;

public class CyclicControl {

    private final Stack<Element> register;

    public CyclicControl() {
        this.register = new Stack<>();
    }

    public void enter(Element element, Runnable action) {
        if (!register.contains(element)) {
            register.push(element);

            action.run();

            register.pop();
        }
    }

    public <T> boolean isCyclic(Element... items) {
        for (Element item : items) {
            if (register.contains(item)) {
                return true;
            }
            else {
                register.push(item);
                if (item.isCyclic(this)) {
                    register.pop();
                    return true;
                }
                else {
                    register.pop();
                }
            }
        }

        return false;
    }

}
