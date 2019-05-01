package org.gramat.parsers;

import org.gramat.Gramat;
import org.gramat.GrammarException;
import org.gramat.PathResolver;
import org.gramat.Tape;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.GRule;
import org.gramat.parsing.directives.GDirective;

public interface Parser {

  static GExpression expectExpression(Gramat gramat, Tape tape) {
    return PExp.expectExpression(gramat, tape);
  }

  static void readGrammarInto(Gramat gramat, PathResolver pathResolver, Tape tape) {
    boolean active = true;

    while(active) {
      PCom.skipVoid(tape);

      if (PCom.isChar(tape, PCat.DIRECTIVE_BEGIN)) {
        GDirective directive = PStm.expectDirective(gramat, tape);

        directive.evalDirective(gramat, pathResolver);
      }
      else if (PCom.isLetter(tape)) {
        GRule rule = PStm.expectRule(gramat, tape);

        gramat.addRule(rule.simplify());
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
