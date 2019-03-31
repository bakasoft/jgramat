package org.bakasoft.gramat.parsing;

import java.util.Objects;

public class GTest {

    public final String input;
    public final String rule;
    public final GLiteral output;

    public GTest(String input, String rule, GLiteral output) {
        this.input = Objects.requireNonNull(input);
        this.rule = Objects.requireNonNull(rule);
        this.output = Objects.requireNonNull(output);
    }

}
