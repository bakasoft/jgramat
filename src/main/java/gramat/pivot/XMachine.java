package gramat.pivot;

import gramat.pivot.data.TDReference;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedList;

public class XMachine extends XSegment {

    public final String name;

    public XMachine(XLanguage lang, String name, XState initial, XState accepted) {
        super(lang, initial, accepted);
        this.name = name;
    }

    public boolean isRecursive() {
        var control = new HashSet<String>();
        var queue = new LinkedList<XMachine>();

        queue.add(this);

        while (queue.size() > 0) {
            var machine = queue.remove();

            if (control.add(machine.name)) {
                var references = new HashSet<String>();

                machine.initial.walk((state, transitions) -> {
                    for (var transition : transitions) {
                        if (transition.data instanceof TDReference) {
                            var data = (TDReference)transition.data;

                            references.add(data.reference);
                        }
                    }
                });

                if (references.contains(name)) {
                    return true;
                }
                else {
                    for (var ref : references) {
                        if (!control.contains(ref)) {
                            var m = lang.findMachine(ref);

                            queue.add(m);
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("@title ");
        out.println(name);
        super.printAmCode(out);
    }
}
