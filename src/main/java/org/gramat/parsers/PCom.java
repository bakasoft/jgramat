package org.gramat.parsers;

import org.gramat.*;
import org.gramat.CharPredicate;
import org.gramat.GrammarException;
import org.gramat.Tape;

// Parsing common things
interface PCom {

  static boolean trySymbol(Tape tape, char symbol) {
    int pos0 = tape.getPosition();

    if (!tape.alive()) {
      tape.setPosition(pos0);
      return false;
    }

    char actual = tape.peek();

    if (symbol != actual) {
      tape.setPosition(pos0);
      return false;
    }

    tape.moveForward();
    return true;
  }

  static void expectSymbols(Tape tape, String text) {
    if (!trySymbols(tape, text)) {
      throw new GrammarException("Expected characters " + inspect(text), tape.getLocation());
    }
  }

  public static void expectSymbol(Tape tape, char c) {
    if (!trySymbol(tape, c)) {
      throw new GrammarException("Expected character " + inspect(c), tape.getLocation());
    }
  }

  static boolean trySymbols(Tape tape, String symbol) {
    int pos0 = tape.getPosition();
    int index = 0;

    while (index < symbol.length()) {
      char expected = symbol.charAt(index);

      if (tape.alive()) {
        char actual = tape.peek();
        if (expected == actual) {
          index++;
          tape.moveForward();
        }
        else {
          tape.setPosition(pos0);
          return false;
        }
      }
      else {
        tape.setPosition(pos0);
        return false;
      }
    }

    return true;
  }

  static Integer expectInteger(Tape tape) {
    Integer value = tryInteger(tape);

    if (value == null) {
      throw new GrammarException("Expected integer", tape.getLocation());
    }

    return value;
  }

  static Integer tryInteger(Tape tape) {
    StringBuilder digits = new StringBuilder();

    while (isDigit(tape)) {
      digits.append(tape.peek());
      tape.moveForward();
    }

    if (digits.length() == 0) {
      return null;
    }

    return Integer.parseInt(digits.toString());
  }

  static void skipVoid(Tape tape) {
    boolean active;

    do {
      active = false;

      while (isWhitespace(tape)) {
        tape.moveForward();
      }

      if (PCom.trySymbols(tape, PCat.INLINE_COMMENT_BEGIN)) {
        active = true;
        while (!PCom.trySymbol(tape, '\n')) {
          if (tape.alive()) {
            tape.moveForward();
          }
          else {
            break;
          }
        }
      }
      else if (PCom.trySymbols(tape, PCat.BLOCK_COMMENT_BEGIN)) {
        active = true;
        while (!PCom.trySymbols(tape, PCat.BLOCK_COMMENT_END)) {
          if (tape.alive()) {
            tape.moveForward();
          }
          else {
            break;
          }
        }
      }
    }
    while (active);
  }

  static boolean isLetter(Tape tape) {
    return is(tape, c -> c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_' || c == '-');
  }

  static boolean isDigit(Tape tape) {
    return is(tape, c -> c >= '0' && c <= '9');
  }

  static boolean isWhitespace(Tape tape) {
    return is(tape, PCom::isWhitespace);
  }

  static boolean isWhitespace(char c) {
    return c == ' ' || c == '\t' || c == '\r' || c == '\n';
  }

  static boolean isChar(Tape tape, char expected) {
    if (tape.alive()) {
      char actual = tape.peek();

      return actual == expected;
    }

    return false;
  }

  static boolean is(Tape tape, CharPredicate predicate) {
    if (tape.alive()) {
      char c = tape.peek();

      return predicate.test(c);
    }

    return false;
  }

  static String inspect(Object obj) {
    // TODO implement data inspector

    if (obj instanceof Character) {
      return "'" + obj + "'";
    }

    return obj.toString();
  }
}
