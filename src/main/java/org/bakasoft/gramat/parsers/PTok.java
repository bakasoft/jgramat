package org.bakasoft.gramat.parsers;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;

interface PTok {

  static String expectName(Tape tape, String description) {
    if (PCom.isChar(tape, '\'')) {
      return PStr.expectQuotedToken(tape, '\'');
    }

    String name = tryName(tape);

    if (name == null) {
      throw new GrammarException("Expected " + description, tape.getLocation());
    }

    return name;
  }

  static String tryName(Tape tape) {
    int pos0 = tape.getPosition();
    if (PCom.isLetter(tape)) {
      StringBuilder name = new StringBuilder();

      while (PCom.isLetter(tape) || PCom.isDigit(tape)) {
        name.append(tape.peek());
        tape.moveForward();
      }

      return name.toString();
    }

    tape.setPosition(pos0);
    return null;
  }
}
