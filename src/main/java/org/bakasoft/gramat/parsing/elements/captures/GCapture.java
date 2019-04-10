package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GArray;
import org.bakasoft.gramat.parsing.literals.GLiteral;
import org.bakasoft.gramat.parsing.literals.GMap;
import org.bakasoft.gramat.parsing.literals.GToken;

import java.util.HashMap;
import java.util.Map;

abstract public class GCapture extends GElement {

    private static final String OBJECT = "object";
    private static final String LIST = "list";
    private static final String VALUE = "value";
    private static final String TRANSFORM = "transform";
    private static final String FUNCTION = "function";
    private static final String CALL = "call";
    private static final String IGNORE = "ignore";
    private static final String REPLACE = "replace";
    private static final String SET = "set";
    private static final String ADD = "add";
    private static final String SET_KEY_VALUE = "set-key-value";
    private static final String SET_VALUE_KEY = "set-value-key";
    private static final String ADD_KEY_VALUE = "add-key-value";
    private static final String ADD_VALUE_KEY = "add-value-key";

    public static GCapture create(Tape tape, String keyword, GArray options, GElement[] arguments) {
        try {
            if (OBJECT.equals(keyword)) {
                return new GObject(
                    optionalString(options),
                    mandatoryExpression(arguments)
                );
            } else if (LIST.equals(keyword)) {
                return new GList(
                    optionalString(options),
                    mandatoryExpression(arguments)
                );
            } else if (VALUE.equals(keyword)) {
                return new GValue(
                    optionalString(options),
                    mandatoryExpression(arguments)
                );
            } else if (TRANSFORM.equals(keyword)) {
                return new GTransform(
                    optionalString(options),
                    mandatoryExpression(arguments)
                );
            } else if (FUNCTION.equals(keyword)) {
                return new GFunction(null,
                    options.forceStringList().toArray(new String[0]),
                    mandatoryExpression(arguments));
            } else if (CALL.equals(keyword)) {
                return new GInvocation(
                    mandatoryString(options),
                    arrayExpressions(arguments, 1, null)
                );
            } else if (IGNORE.equals(keyword)) {
                emptyLiterals(options);
                return new GIgnore(mandatoryExpression(arguments));
            } else if (REPLACE.equals(keyword)) {
                GLiteral literal = singleLiteral(options);

                if (literal instanceof GMap) {
                    Map<String, String> replacements = literal.forceStringMap();
                    return new GReplaceMap(replacements,
                        mandatoryExpression(arguments));
                } else if (literal instanceof GToken) {
                    String replacement = literal.forceString();
                    return new GReplaceString(replacement,
                        mandatoryExpression(arguments));
                } else {
                    throw new RuntimeException();
                }
            } else if (SET.equals(keyword)) {
                return new GNamedProperty(
                    mandatoryString(options),
                    false,
                    mandatoryExpression(arguments));
            } else if (ADD.equals(keyword)) {
                return new GNamedProperty(
                    optionalString(options),
                    true,
                    mandatoryExpression(arguments));
            } else if (SET_KEY_VALUE.equals(keyword) || ADD_KEY_VALUE.equals(keyword)
                || SET_VALUE_KEY.equals(keyword) || ADD_VALUE_KEY.equals(keyword)) {
                boolean appendMode = ADD_KEY_VALUE.equals(keyword) || ADD_VALUE_KEY.equals(keyword);
                boolean invertMode = SET_VALUE_KEY.equals(keyword) || ADD_VALUE_KEY.equals(keyword);
                emptyLiterals(options);
                arrayExpressions(arguments, 2, 3);
                if (arguments.length == 2) {
                    return new GDynamicProperty(arguments[0], null, arguments[1], appendMode, invertMode);
                }
                return new GDynamicProperty(arguments[0], arguments[1], arguments[2], appendMode, invertMode);
            }
        }
        catch (Exception e) {
            throw new GrammarException("capture " + keyword + ": " + e.getMessage(), tape.getLocation());
        }

        throw new GrammarException("invalid capture: " + keyword, tape.getLocation());
    }

    private static void emptyLiterals(GArray literals) throws Exception {
        if (literals.size() > 0) {
            throw new Exception("no expected arguments");
        }
    }

    private static GLiteral singleLiteral(GArray literals) throws Exception {
        if (literals.size() != 1) {
            throw new Exception("expected only one argument");
        }

        return literals.get(0);
    }

    private static String optionalString(GArray literals) throws Exception {
        if (literals.size() == 0) {
            return null;
        }

        return singleLiteral(literals).forceString();
    }

    private static String mandatoryString(GArray literals) throws Exception {
        return singleLiteral(literals).forceString();
    }

    private static GElement[] arrayExpressions(GElement[] elements, int minimum, Integer maximum) throws Exception {
        if (elements.length < minimum) {
            throw new Exception("not expected less than " + minimum + " arguments: " + elements.length);
        }
        else if (maximum != null && elements.length > maximum) {
            throw new Exception("not expected more than " + maximum + " arguments" + elements.length);
        }

        return elements;
    }

    private static GElement mandatoryExpression(GElement[] elements) throws Exception {
        if (elements.length != 1) {
            throw new Exception("Expected only one expression");
        }

        return elements[0];
    }

}
