package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;
import org.bakasoft.gramat.parsing.literals.GMap;
import org.bakasoft.gramat.parsing.literals.GToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract public class GCapture extends GElement {

    private static GCapture validate(Tape tape, String kind, GLiteral[] options, GElement[] arguments) {
        if ("object".equals(kind)) {
            String type = getOptionalString(options);
            GElement expression = getSingleExpression(arguments);
            return new GObject(type, expression);
        }
        else if ("list".equals(kind)) {
            String type = getOptionalString(options);
            GElement expression = getSingleExpression(arguments);
            return new GList(type, expression);
        }
        else if ("value".equals(kind)) {
            String type = getOptionalString(options);
            GElement expression = getSingleExpression(arguments);
            return new GValue(type, expression);
        }
        else if ("transform".equals(kind)) {
            String transformName = getOptionalString(options);
            GElement expression = getSingleExpression(arguments);
            return new GTransform(transformName, expression);
        }
        else if ("function".equals(kind)) {
            String[] functionArgs = getArrayStrings(options);
            GElement functionBody = getSingleExpression(arguments);
            return new GFunction(null, functionArgs, functionBody);
        }
        else if ("call".equals(kind)) {
            String functionName = getSingleString(options);
            return new GInvocation(functionName, arguments);
        }
        else if ("ignore".equals(kind)) {
            if (options.length != 0) {
                throw new RuntimeException("expected no option");
            }

            GElement expression = getSingleExpression(arguments);
            return new GIgnore(expression);
        }
        else if ("replace".equals(kind)) {
            if (options.length != 1) {
                throw new RuntimeException();
            }

            GLiteral option = options[0];

            if (option instanceof GMap) {
                Map<String, String> replacements = getStringMap((GMap)option);
                GElement expression = getSingleExpression(arguments);
                return new GReplaceMap(replacements, expression);
            }
            else if (option instanceof GToken) {
                String replacement = getSingleString(options);
                GElement expression = getSingleExpression(arguments);
                return new GReplaceString(replacement, expression);
            }
            else {
                throw new RuntimeException();
            }
        }
        else if ("set".equals(kind) || "add".equals(kind)) {
            String propertyName = getFistString(options);
            boolean appendMode = "add".equals(kind);

            if (arguments.length == 1) {
                return new GNamedProperty(propertyName, appendMode, arguments[0]);
            }
            else if (arguments.length == 2 || arguments.length == 3) {
                boolean invertedMode;

                if ("key-value".equals(propertyName)) {
                    invertedMode = false;
                }
                else if ("value-value".equals(propertyName)) {
                    invertedMode = true;
                }
                else {
                    throw new GrammarException("expected key-value or value-key", tape.getLocation());
                }

                if (arguments.length == 3) {
                    return new GDynamicProperty(arguments[0], arguments[1], arguments[2], appendMode, invertedMode);
                }

                return new GDynamicProperty(arguments[0], null, arguments[1], appendMode, invertedMode);
            }
            else {
                throw new GrammarException("invalid number of arguments", tape.getLocation());
            }
        }

        throw new GrammarException("invalid capture: " + kind, tape.getLocation());
    }

    public static GCapture expectCapture(Tape tape) {
        expectSymbol(tape, '<');

        skipVoid(tape);

        String name = expectName(tape, "capture kind");
        ArrayList<GLiteral> options = new ArrayList<>();
        ArrayList<GElement> arguments = new ArrayList<>();

        skipVoid(tape);

        while (!trySymbol(tape, ':')) {
            GLiteral option = GLiteral.expectLiteral(tape);

            options.add(option);

            skipVoid(tape);
        }

        skipVoid(tape);

        while (!trySymbol(tape, '>')) {
            GElement argument = GElement.expectExpression(tape);

            arguments.add(argument);

            skipVoid(tape);

            if (trySymbol(tape, ',')) {
                skipVoid(tape);
            }
        }

        return validate(tape, name, options.toArray(new GLiteral[0]), arguments.toArray(new GElement[0]));
    }

    private static String getSingleString(GLiteral[] arguments) {
        if (arguments.length != 1) {
            throw new RuntimeException("Expected only one argument");
        }

        return arguments[0].resolveString();
    }

    private static String getOptionalString(GLiteral[] arguments) {
        if (arguments.length == 0) {
            return null;
        }
        else if (arguments.length != 1) {
            throw new RuntimeException("Expected only one argument");
        }

        return arguments[0].resolveString();
    }

    private static String getFistString(GLiteral[] arguments) {
        if (arguments.length == 0) {
            return null;
        }

        return arguments[0].resolveString();
    }

    private static String[] getArrayStrings(GLiteral[] arguments) {
        String[] result = new String[arguments.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = arguments[i].resolveString();
        }

        return result;
    }

    private static Map<String, String> getStringMap(GMap map) {
        HashMap<String, String> result = new HashMap<>();

        for (String key : map.keySet()) {
            GLiteral value = map.get(key);

            if (value != null) {
                result.put(key, value.resolveString());
            }
            else {
                result.put(key, null);
            }
        }

        return result;
    }

    private static GElement getSingleExpression(GElement[] arguments) {
        if (arguments.length != 1) {
            throw new RuntimeException("Expected only one expression");
        }

        return arguments[0];
    }

}
