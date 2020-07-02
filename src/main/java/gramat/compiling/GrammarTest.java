package gramat.compiling;

import gramat.epsilon.Input;
import gramat.parsing.test.TestValue;
import gramat.runtime.EvalContext;
import gramat.util.GramatWriter;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;

import java.util.HashMap;

public class GrammarTest {

    public final Location location;
    public final String expressionName;
    public final String input;
    public final boolean expectedMatch;
    public final TestValue expectedValue;

    public GrammarTest(String expressionName, String input, boolean expectedMatch, TestValue expectedValue) {
        this.location = null;
        this.expressionName = expressionName;
        this.input = input;
        this.expectedMatch = expectedMatch;
        this.expectedValue = expectedValue;
    }

    public void run(Compiler compiler) {
        System.out.println("Testing " + expressionName + " <- " + location);

        Expression expression = compiler.compileRule(expressionName);

        if (expression == null) {
            throw new ParseException("Expression not found: " + expressionName, location);
        }

        EvalContext evalContext = new EvalContext(Input.of(input), new HashMap<>());

        boolean matches = expression.eval(evalContext);

        if (expectedMatch && !matches) {
            throw new RuntimeException(expressionName + " did not match: " + evalContext.lastCommitName);
        }
        else if (!expectedMatch && matches) {
            throw new ParseException("Expected to fail: " + expressionName, location);
        }

        // TODO check eval result
        var writer = new GramatWriter(System.out, "  ");

        try {
            var output = evalContext.getValue();
            System.out.print("OUTPUT: ");
            writer.writeValue(output);
            System.out.println();
        }
        catch(ParseException e) {
            System.out.println("OUTPUT: Error/" + e.getMessage());
        }
    }

    public String getExpressionName() {
        return expressionName;
    }

    public boolean getExpectedMatch() {
        return expectedMatch;
    }

    public Location getLocation() {
        return location;
    }
}
