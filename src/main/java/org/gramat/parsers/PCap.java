package org.gramat.parsers;

import org.gramat.Gramat;
import org.gramat.GrammarException;
import org.gramat.Location;
import org.gramat.Tape;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.elements.producers.GUnion;
import org.gramat.parsing.elements.templates.*;
import org.gramat.parsing.elements.mutations.GDynamicProperty;
import org.gramat.parsing.elements.mutations.GNamedProperty;
import org.gramat.parsing.elements.producers.GList;
import org.gramat.parsing.elements.producers.GObject;
import org.gramat.parsing.elements.producers.GValue;
import org.gramat.parsing.elements.templates.GFunction;
import org.gramat.parsing.elements.templates.GInvocation;
import org.gramat.parsing.elements.transforms.GIgnore;
import org.gramat.parsing.elements.transforms.GReplaceMap;
import org.gramat.parsing.elements.transforms.GReplaceString;
import org.gramat.parsing.elements.transforms.GTransform;
import org.gramat.parsing.literals.GArray;
import org.gramat.parsing.literals.GLiteral;
import org.gramat.parsing.literals.GMap;
import org.gramat.parsing.literals.GToken;

import java.util.Map;

// Parsing capturing
interface PCap {

    String OBJECT = "object";
    String LIST = "list";
    String VALUE = "value";
    String UNION = "union";
    String TRANSFORM = "transform";
    String FUNCTION = "function";
    String CALL = "call";
    String IGNORE = "ignore";
    String REPLACE = "replace";
    String SET = "set";
    String ADD = "add";
    String SET_KEY_VALUE = "set-key-value";
    String SET_VALUE_KEY = "set-value-key";
    String ADD_KEY_VALUE = "add-key-value";
    String ADD_VALUE_KEY = "add-value-key";

    static GExpression create(Location location, Gramat gramat, Tape tape, String keyword, GArray options, GExpression[] arguments) {
        try {
            if (OBJECT.equals(keyword)) {
                return new GObject(
                    location.range(),
                    optionalString(options),
                    mandatoryExpression(arguments)
                );
            } else if (LIST.equals(keyword)) {
                return new GList(
                    location.range(),
                    optionalString(options),
                    mandatoryExpression(arguments)
                );
            } else if (VALUE.equals(keyword)) {
                return new GValue(
                    location.range(),
                    optionalString(options),
                    mandatoryExpression(arguments)
                );
            } else if (UNION.equals(keyword)) {
                return new GUnion(location.range(), gramat, null, arguments);
            } else if (TRANSFORM.equals(keyword)) {
                return new GTransform(
                    location.range(),
                    optionalString(options),
                    mandatoryExpression(arguments)
                );
            } else if (FUNCTION.equals(keyword)) {
                return new GFunction(
                    location.range(),
                    null,
                    options.forceStringList().toArray(new String[0]),
                    mandatoryExpression(arguments));
            } else if (CALL.equals(keyword)) {
                return new GInvocation(
                    location.range(),
                    gramat,
                    mandatoryString(options),
                    arrayExpressions(arguments, 1, null)
                );
            } else if (IGNORE.equals(keyword)) {
                emptyLiterals(options);
                return new GIgnore(
                    location.range(),
                    mandatoryExpression(arguments));
            } else if (REPLACE.equals(keyword)) {
                GLiteral literal = singleLiteral(options);

                if (literal instanceof GMap) {
                    Map<String, String> replacements = literal.forceStringMap();
                    return new GReplaceMap(
                        location.range(),
                        replacements,
                        mandatoryExpression(arguments));
                } else if (literal instanceof GToken) {
                    String replacement = literal.forceString();
                    return new GReplaceString(
                        location.range(),
                        replacement,
                        mandatoryExpression(arguments));
                } else {
                    throw new RuntimeException();
                }
            } else if (SET.equals(keyword)) {
                return new GNamedProperty(
                    location.range(),
                    mandatoryString(options),
                    false,
                    mandatoryExpression(arguments));
            } else if (ADD.equals(keyword)) {
                return new GNamedProperty(
                    location.range(),
                    optionalString(options),
                    true,
                    mandatoryExpression(arguments));
            } else if (SET_KEY_VALUE.equals(keyword) || ADD_KEY_VALUE.equals(keyword)
                || SET_VALUE_KEY.equals(keyword) || ADD_VALUE_KEY.equals(keyword)) {
                boolean appendMode = ADD_KEY_VALUE.equals(keyword) || ADD_VALUE_KEY.equals(keyword);
                boolean invertMode = SET_VALUE_KEY.equals(keyword) || ADD_VALUE_KEY.equals(keyword);
                emptyLiterals(options);
                arrayExpressions(arguments, 2, 3);
                GExpression nameExpression;
                GExpression separatorExpression;
                GExpression valueExpression;

                if (arguments.length == 2) {
                    nameExpression = arguments[invertMode ? 1 : 0];
                    separatorExpression = null;
                    valueExpression = arguments[invertMode ? 0 : 1];
                }
                else {
                    nameExpression = arguments[invertMode ? 2 : 0];
                    separatorExpression = arguments[1];
                    valueExpression = arguments[invertMode ? 0 : 2];
                }

                return new GDynamicProperty(location.range(), gramat,
                    nameExpression, separatorExpression, valueExpression,
                    appendMode, invertMode);
            }
        }
        catch (Exception e) {
            throw new GrammarException("capture " + keyword + ": " + e.getMessage(), tape.getLocation());
        }

        throw new GrammarException("invalid capture: " + keyword, tape.getLocation());
    }

    static void emptyLiterals(GArray literals) throws Exception {
        if (literals.size() > 0) {
            throw new Exception("no expected arguments");
        }
    }

    static GLiteral singleLiteral(GArray literals) throws Exception {
        if (literals.size() != 1) {
            throw new Exception("expected only one argument");
        }

        return literals.get(0);
    }

    static String optionalString(GArray literals) throws Exception {
        if (literals.size() == 0) {
            return null;
        }

        return singleLiteral(literals).forceString();
    }

    static String mandatoryString(GArray literals) throws Exception {
        return singleLiteral(literals).forceString();
    }

    static GExpression[] arrayExpressions(GExpression[] elements, int minimum, Integer maximum) throws Exception {
        if (elements.length < minimum) {
            throw new Exception("not expected less than " + minimum + " arguments: " + elements.length);
        }
        else if (maximum != null && elements.length > maximum) {
            throw new Exception("not expected more than " + maximum + " arguments" + elements.length);
        }

        return elements;
    }

    static GExpression mandatoryExpression(GExpression[] elements) throws Exception {
        if (elements.length != 1) {
            throw new Exception("Expected only one expression");
        }

        return elements[0];
    }

}
