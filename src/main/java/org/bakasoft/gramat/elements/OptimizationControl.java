package org.bakasoft.gramat.elements;

public class OptimizationControl extends CyclicControl {

    private Element root;

    private int count;

    public OptimizationControl(Element root) {
        this.root = root;
    }

    public int getCount() {
        return count;
    }

    public boolean hasChanged() {
        return count > 0;
    }

    public void reset() {
        count = 0;
    }

    public void apply(String description, Element older, Element newer) {
        if (root == older) {
            root = newer;
        }

        root.replace(new CyclicControl(), older, newer);

        count++;

//        System.out.println(count + " - " + description);
    }

    public void next(Element element) {
        element.optimize(this);
    }

    public void next(Element[] elements) {
        for (Element element : elements) {
            element.optimize(this);
        }
    }

    public void optimize() {
        root.optimize(this);
    }

    public Element getRoot() {
        return root;
    }
}
