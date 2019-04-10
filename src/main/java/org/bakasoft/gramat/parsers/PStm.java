package org.bakasoft.gramat.parsers;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.GRule;
import org.bakasoft.gramat.parsing.directives.GDirective;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GArray;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.ArrayList;

interface PStm {

  static boolean isStatementBeginning(Tape tape) {
    int pos0 = tape.getPosition();
    boolean result;

    if (PTok.tryName(tape) != null) {
      PCom.skipVoid(tape);

      // if there is a name followed by the assignment symbol, it's a rule!
      result = PCom.trySymbol(tape, PCat.ASSIGNMENT_OPERATOR);
    }
    else if (PCom.trySymbol(tape, PCat.DIRECTIVE_BEGIN)) {
      PCom.skipVoid(tape);

      // if there is an @ followed by a name, it's a directive!
      result = PTok.tryName(tape) != null;
    }
    else {
      PCom.skipVoid(tape);

      result = !tape.alive();
    }

    tape.setPosition(pos0);
    return result;
  }


  static GRule expectRule(Gramat gramat, Tape tape) {
    String name = PTok.expectName(tape, "rule name");

    PCom.skipVoid(tape);

    if (!PCom.trySymbol(tape, PCat.ASSIGNMENT_OPERATOR)) {
      throw new GrammarException("Expected rule assignment: " + PCom.inspect(PCat.ASSIGNMENT_OPERATOR), tape.getLocation());
    }

    PCom.skipVoid(tape);

    GElement expression = PExp.expectExpression(gramat, tape);

    return new GRule(name, expression);
  }

  static GDirective expectDirective(Gramat gramat, Tape tape) {
    PCom.expectSymbol(tape, PCat.DIRECTIVE_BEGIN);

    PCom.skipVoid(tape);

    String name = PTok.expectName(tape, "directive name");

    PCom.skipVoid(tape);

    GArray arguments = new GArray();

    while (!isStatementBeginning(tape)) {
      GLiteral literal = PLit.expectLiteral(gramat, tape);

      arguments.add(literal);

      PCom.skipVoid(tape);
    }

    return new GDirective(name, arguments);
  }

}
