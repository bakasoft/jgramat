package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.Objects;

public class GTest {

    public final String input;
    public final String rule;
    public final GLiteral output;
    public final boolean failMode;

    public GTest(String input, String rule, GLiteral output, boolean failMode) {
        this.input = Objects.requireNonNull(input);
        this.rule = Objects.requireNonNull(rule);
        this.output = output;
        this.failMode = failMode;
    }

}
