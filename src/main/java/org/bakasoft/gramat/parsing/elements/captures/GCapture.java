package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;
import org.bakasoft.gramat.parsing.literals.GMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract public class GCapture extends GElement {

    private static GCapture validate(Tape tape, String name, GLiteral[] options, GElement[] arguments) {
        // TODO move constructors logic to here
        if ("object".equals(name)) {
            return new GObject(options, arguments);
        }
        else if ("list".equals(name)) {
            return new GList(options, arguments);
        }
        else if ("value".equals(name)) {
            return new GValue(options, arguments);
        }
        else if ("set".equals(name) || "add".equals(name)) {
            String propertyName = getFistString(options);
            boolean appendMode = "add".equals(name);

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
                    return new GDynamicProperty(arguments[0], arguments[1], appendMode, arguments[2], invertedMode);
                }

                return new GDynamicProperty(arguments[0], null, appendMode, arguments[1], invertedMode);
            }
            else {
                throw new GrammarException("invalid number of arguments", tape.getLocation());
            }
        }
        else if ("transform".equals(name)) {
            return new GTransform(options, arguments);
        }
        else if ("replace".equals(name)) {
            return new GReplaceString(options, arguments);
        }
        else if ("ignore".equals(name)) {
            return new GIgnore(options, arguments);
        }
        else if ("function".equals(name)) {
            return new GFunction(options, arguments);
        }
        else if ("call".equals(name)) {
            return new GInvocation(options, arguments);
        }

        throw new GrammarException("invalid capture: " + name, tape.getLocation());
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

    public static String getSingleString(GLiteral[] arguments) {
        if (arguments.length != 1) {
            throw new RuntimeException("Expected only one argument");
        }

        return arguments[0].resolveString();
    }

    public static String getOptionalString(GLiteral[] arguments) {
        if (arguments.length == 0) {
            return null;
        }
        else if (arguments.length != 1) {
            throw new RuntimeException("Expected only one argument");
        }

        return arguments[0].resolveString();
    }

    public static String getFistString(GLiteral[] arguments) {
        if (arguments.length == 0) {
            return null;
        }

        return arguments[0].resolveString();
    }

    public static String[] getArrayStrings(GLiteral[] arguments) {
        String[] result = new String[arguments.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = arguments[i].resolveString();
        }

        return result;
    }

    public static Map<String, String> getStringMap(GLiteral[] arguments) {
        if (arguments.length != 1) {
            throw new RuntimeException("Expected only one argument");
        }

        if (!(arguments[0] instanceof GMap)) {
            throw new RuntimeException("Expected a map of string-string");
        }

        GMap map = (GMap)arguments[0];
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

    public static GElement getSingleExpression(GElement[] arguments) {
        if (arguments.length != 1) {
            throw new RuntimeException("Expected only one expression");
        }

        return arguments[0];
    }

}
