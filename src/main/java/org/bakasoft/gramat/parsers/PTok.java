package org.bakasoft.gramat.parsers;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.literals.GLiteral;

// Parsing tokens
interface PTok {

  static String expectName(Tape tape, String description) {
    String name = tryName(tape);

    if (name == null) {
      throw new GrammarException("Expected " + description, tape.getLocation());
    }

    return name;
  }

  static String tryName(Tape tape) {
    int pos0 = tape.getPosition();

    try {
      if (PCom.isChar(tape, PCat.QUOTED_TOKEN_DELIMITER)) {
        return PStr.expectQuotedToken(tape, PCat.QUOTED_TOKEN_DELIMITER);
      }

      if (PCom.isLetter(tape)) {
        StringBuilder buffer = new StringBuilder();

        while (PCom.isLetter(tape) || PCom.isDigit(tape)) {
          buffer.append(tape.peek());
          tape.moveForward();
        }

        return buffer.toString();
      }

      tape.setPosition(pos0);
      return null;
    }
    catch(Exception e) {
      throw new GrammarException("name problem: " + e.getMessage(), tape.getLocationOf(pos0), e);
    }
  }
}
