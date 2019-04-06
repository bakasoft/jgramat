package org.bakasoft.gramat.parsing.literals;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.elements.GString;

import java.util.Objects;

public class GToken extends GLiteral {

    public final String content;

    public GToken(String content) {
        this.content = Objects.requireNonNull(content);
    }

    public static GToken expectToken(Tape tape) {
        if (GElement.isLetter(tape)) {
            String content = GElement.expectName(tape, "token");

            return new GToken(content);
        }
        else if (GElement.isChar(tape, '"')) {
            String content = GString.expectQuotedToken(tape);

            return new GToken(content);
        }
        else {
            throw new GrammarException("Invalid token", tape.getLocation());
        }
    }
}
