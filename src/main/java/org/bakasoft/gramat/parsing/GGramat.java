package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.PathResolver;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.directives.GDirective;
import org.bakasoft.gramat.parsing.elements.GElement;

public class GGramat {

    public static void readGrammarInto(Gramat gramat, PathResolver pathResolver, Tape tape) {
        boolean active = true;

        while(active) {
            GElement.skipVoid(tape);

            if (GElement.isChar(tape, '@')) {
                GDirective directive = GDirective.expectDirective(tape);

                directive.evalDirective(gramat, pathResolver);
            }
            else if (GElement.isLetter(tape)) {
                GRule rule = GRule.expectRule(tape);

                gramat.addRule(rule);
            }
            else {
                active = false;
            }
        }

        GElement.skipVoid(tape);

        if (tape.alive()) {
            throw new GrammarException("Expected end of file", tape.getLocation());
        }
    }

}
