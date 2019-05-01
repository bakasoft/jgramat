package org.gramat.parsers;

import org.gramat.Gramat;
import org.gramat.GrammarException;
import org.gramat.Location;
import org.gramat.Tape;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.GRule;
import org.gramat.parsing.directives.GDirective;
import org.gramat.parsing.literals.GArray;
import org.gramat.parsing.literals.GLiteral;

// Parsing statements
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
    Location location = tape.getLocation();
    String name = PTok.expectName(tape, "rule name");

    PCom.skipVoid(tape);

    if (!PCom.trySymbol(tape, PCat.ASSIGNMENT_OPERATOR)) {
      throw new GrammarException("Expected rule assignment: " + PCom.inspect(PCat.ASSIGNMENT_OPERATOR), tape.getLocation());
    }

    PCom.skipVoid(tape);

    GExpression expression = PExp.expectExpression(gramat, tape);

    return new GRule(location.range(), name, expression);
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
