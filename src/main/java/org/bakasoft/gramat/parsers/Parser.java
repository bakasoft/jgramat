package org.bakasoft.gramat.parsers;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.PathResolver;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.GRule;
import org.bakasoft.gramat.parsing.directives.GDirective;
import org.bakasoft.gramat.parsing.elements.GElement;

public interface Parser {

  static GElement expectExpression(Tape tape) {
    return PExp.expectExpression(tape);
  }

  static void readGrammarInto(Gramat gramat, PathResolver pathResolver, Tape tape) {
    boolean active = true;

    while(active) {
      PCom.skipVoid(tape);

      if (PCom.isChar(tape, '@')) {
        GDirective directive = PStm.expectDirective(tape);

        directive.evalDirective(gramat, pathResolver);
      }
      else if (PCom.isLetter(tape)) {
        GRule rule = PStm.expectRule(tape);

        gramat.addRule(rule);
      }
      else {
        active = false;
      }
    }

    PCom.skipVoid(tape);

    if (tape.alive()) {
      throw new GrammarException("Expected end of file", tape.getLocation());
    }
  }

}
