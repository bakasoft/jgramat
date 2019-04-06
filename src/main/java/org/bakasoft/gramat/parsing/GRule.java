package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.elements.captures.*;

import java.util.Map;
import java.util.Objects;

public class GRule {

    public final String name;
    public final GElement expression;

    public GRule(String name, GElement expression) {
        this.name = Objects.requireNonNull(name);
        this.expression = Objects.requireNonNull(expression);
    }

    public GRule simplify() {
        GElement simplified = expression.simplify();

        if (simplified == null) {
            return null;
        }

        return new GRule(name, resolveNames(name, simplified));
    }

    private static GElement resolveNames(String name, GElement expression) {
        if (expression instanceof GObject) {
            GObject gObj = (GObject)expression;

            if (gObj.typeName == null) {
                return new GObject(name, gObj.expression);
            }
        }
        else if (expression instanceof GList) {
            GList gList = (GList)expression;

            if (gList.typeName == null) {
                return new GList(name, gList.expression);
            }
        }
        else if (expression instanceof GValue) {
            GValue gValue = (GValue)expression;

            if (gValue.typeName == null) {
                return new GValue(name, gValue.expression);
            }
        }
        else if (expression instanceof GTransform) {
            GTransform gTran = (GTransform)expression;

            if (gTran.name == null) {
                return new GTransform(name, gTran.expression);
            }
        }
        else if (expression instanceof GFunction) {
            GFunction gFunc = (GFunction)expression;

            if (gFunc.name == null) {
                return new GFunction(name, gFunc.arguments, gFunc.expression);
            }
        }

        return expression;
    }

    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return expression.compile(gramat, compiled);
    }

    public static GRule expectRule(Tape tape) {
        String name = GElement.expectName(tape, "rule name");

        GElement.skipVoid(tape);

        if (!GElement.trySymbol(tape, '=')) {
            throw new GrammarException("Expected rule assignment: " + GElement.inspect("="), tape.getLocation());
        }

        GElement.skipVoid(tape);

        GElement expression = GElement.expectExpression(tape);

        return new GRule(name, expression);
    }

}
