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

}
