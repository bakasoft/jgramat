package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.Stringifier;
import org.bakasoft.gramat.parsing.elements.*;

public class GStringifier {

    public static String stringify(GElement element) {
        StringBuilder output = new StringBuilder();

        stringifyElement(element, output, true);

        return output.toString();
    }

    private static void stringifyElement(GElement element, StringBuilder output, boolean grouped) {
        if (element instanceof GAlternation) {
            stringifyAlternation((GAlternation)element, output, grouped);
        }
        else if (element instanceof GSequence) {
            stringifySequence((GSequence)element, output, grouped);
        }
        else if (element instanceof GRepetition) {
            stringifyRepetition((GRepetition)element, output, grouped);
        }
        else if (element instanceof GOptional) {
            stringifyOptional((GOptional)element, output, grouped);
        }
        else if (element instanceof GNegation) {
            stringifyNegation((GNegation)element, output, grouped);
        }
        else if (element instanceof GObject) {
            stringifyObject((GObject)element, output, grouped);
        }
        else if (element instanceof GProperty) {
            stringifyProperty((GProperty)element, output, grouped);
        }
        else if (element instanceof GString) {
            stringifyString((GString)element, output, grouped);
        }
        else if (element instanceof GTerminator) {
            stringifyTerminator(output, grouped);
        }
        else if (element instanceof GReference) {
            stringifyReference((GReference)element, output, grouped);
        }
        else {
            throw new RuntimeException("not implemented element: " + element.getClass());
        }
    }

    private static void stringifyReference(GReference ref, StringBuilder output, boolean grouped) {
        output.append(ref.ruleName);
    }

    private static void stringifyGrammar(Gramat gramat, StringBuilder output, boolean grouped) {
        for (GRule rule : gramat.getRules()) {
            stringifyRule(rule, output, true);
            output.append(System.lineSeparator());
        }
    }

    private static void stringifyRule(GRule rule, StringBuilder output, boolean grouped) {
        output.append(rule.name);
        if (rule.objectMode) {
            output.append(":=");
        }
        else {
            output.append("=");
        }
        stringifyElement(rule.expression, output, true);
        output.append(System.lineSeparator());
    }

    private static void stringifyString(GString str, StringBuilder output, boolean grouped) {
        Stringifier.literal(str.content, output);
    }

    private static void stringifyObject(GObject object, StringBuilder output, boolean grouped) {
        output.append(object.typeName);
        output.append(':');
        stringifyElement(object.expression, output, false);
    }

    private static void stringifyProperty(GProperty property, StringBuilder output, boolean grouped) {
        output.append('<');
        output.append(property.propertyName);
        output.append(':');
        stringifyElement(property.expression, output, true);
        output.append('>');
    }

    private static void stringifyNegation(GNegation negation, StringBuilder output, boolean grouped) {
        output.append('!');
        stringifyElement(negation.expression, output, false);
    }

    private static void stringifyOptional(GOptional optional, StringBuilder output, boolean grouped) {
        output.append('[');
        stringifyElement(optional.expression, output, true);
        output.append(']');
    }

    private static void stringifyAlternation(GAlternation alt, StringBuilder output, boolean grouped) {
        if (alt.expressions.length == 1) {
            stringifyElement(alt.expressions[0], output, grouped);
        }
        else {
            if (!grouped) {
                output.append('(');
            }

            for (int i = 0; i < alt.expressions.length; i++) {
                if (i > 0) {
                    output.append('|');
                }

                stringifyElement(alt.expressions[i], output, false);
            }

            if (!grouped) {
                output.append(')');
            }
        }
    }

    private static void stringifySequence(GSequence seq, StringBuilder output, boolean grouped) {
        if (seq.expressions.length == 1) {
            stringifyElement(seq.expressions[0], output, grouped);
        }
        else {
            if (!grouped) {
                output.append('(');
            }

            for (int i = 0; i < seq.expressions.length; i++) {
                if (i > 0) {
                    output.append(' ');
                }

                stringifyElement(seq.expressions[i], output, false);
            }

            if (!grouped) {
                output.append(')');
            }
        }
    }

    private static void stringifyTerminator(StringBuilder output, boolean grouped) {
        output.append('$');
    }

    private static void stringifyRepetition(GRepetition rep, StringBuilder output, boolean grouped) {
        output.append('{');

        if (rep.minimum != null) {
            output.append(rep.minimum);
            if (rep.maximum != null) {
                output.append(',');
                output.append(rep.maximum);

            }
            output.append(' ');
        }

        stringifyElement(rep.expression, output, true);

        if (rep.separator != null) {
            output.append('/');
            stringifyElement(rep.separator, output, true);
        }

        output.append('}');
    }
}
