package org.bakasoft.gramat.parsers;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.elements.GString;

// Parsing strings
interface PStr {

  static String expectQuotedToken(Tape tape, char delimiter) {
    String literal = tryQuotedToken(tape, delimiter);

    if (literal == null) {
      throw new RuntimeException("Expected string literal");
    }

    return literal;
  }

  static String tryQuotedToken(Tape tape, char delimiter) {
    int pos0 = tape.getPosition();
    StringBuilder content = new StringBuilder();

    if (!PCom.trySymbol(tape, delimiter)) {
      tape.setPosition(pos0);
      return null;
    }

    while (!PCom.isChar(tape, delimiter)) {
      char c = readStringChar(tape);

      content.append(c);
    }

    PCom.expectSymbol(tape, delimiter);

    return content.toString();
  }

  static char readStringChar(Tape tape) {
    char c = tape.peek();
    tape.moveForward();

    if (c == '\\') {
      char escaped = tape.peek();
      tape.moveForward();

      switch (escaped) {
        case '"':
        case '\'':
        case '`':
        case '\\':
          return '\\';
        case 's':
          return ' ';
        case 'n':
          return '\n';
        case 'r':
          return '\r';
        case 't':
          return '\t';
        case 'u':
          return expectCharFromHex(tape);
        default:
          throw new GrammarException("Invalid escape sequence: " + PCom.inspect(c), tape.getLocation());
      }
    }

    return c; // TODO allow only accepted characters
  }

  static char expectCharFromHex(Tape tape) {
    char[] hex = new char[4];

    for (int i = 0; i < hex.length; i++) {
      char c = tape.peek();
      if (c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F' || c >= '0' && c <= '9') {
        hex[i] = c;

        tape.moveForward();
      }
      else {
        throw new GrammarException("expected hexadecimal character", tape.getLocation());
      }
    }

    return (char)Integer.parseInt(new String(hex), 16);
  }

}
