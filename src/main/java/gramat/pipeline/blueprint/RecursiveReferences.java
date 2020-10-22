package gramat.pipeline.blueprint;

import gramat.models.expressions.ModelExpression;
import gramat.models.expressions.ModelReference;
import gramat.util.NameMap;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Set;

public class RecursiveReferences {

    public static Set<String> compute(ModelExpression main, NameMap<ModelExpression> expressions) {
        var references = new LinkedHashSet<String>();
        var stack = new ArrayDeque<String>();

        compute(main, expressions, stack, references);

        return references;
    }

    private static void compute(ModelExpression main, NameMap<ModelExpression> expressions, Deque<String> stack, Set<String> result, String name) {
        stack.push(name);

        compute(main, expressions, stack, result);

        stack.pop();
    }

    private static void compute(ModelExpression main, NameMap<ModelExpression> expressions, Deque<String> stack, Set<String> references) {
        for (var child : main.getChildren()) {
            if (child instanceof ModelReference) {
                var reference = (ModelReference)child;

                if (!references.contains(reference.name)) {
                    if (stack.contains(reference.name)) {
                        references.add(reference.name);
                    }
                    else {
                        var ref = expressions.find(reference.name);

                        compute(ref, expressions, stack, references, reference.name);
                    }
                }
            }
            else {
                compute(child, expressions, stack, references);
            }
        }
    }

}
