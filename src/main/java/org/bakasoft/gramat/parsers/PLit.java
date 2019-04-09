package org.bakasoft.gramat.parsers;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.literals.GArray;
import org.bakasoft.gramat.parsing.literals.GLiteral;
import org.bakasoft.gramat.parsing.literals.GMap;
import org.bakasoft.gramat.parsing.literals.GToken;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

interface PLit {

  static GLiteral expectLiteral(Tape tape) {
    GLiteral literal = tryLiteral(tape);

    if (literal == null) {
      throw new GrammarException("Expected literal", tape.getLocation());
    }

    return literal;
  }

  static GLiteral tryLiteral(Tape tape) {
    if (PCom.isChar(tape, '\'') || PCom.isLetter(tape)) {
      return expectToken(tape);
    }
    else if (PCom.isChar(tape, '[')) {
      return expectArray(tape);
    }
    else if (PCom.isChar(tape, '{')) {
      return expectMap(tape);
    }

    return null;
  }

  static GToken expectToken(Tape tape) {
    String content = PTok.expectName(tape, "token");

    return new GToken(content);
  }

  static GArray expectArray(Tape tape) {
    ArrayList<GLiteral> list = new ArrayList<>();

    PCom.expectSymbol(tape, '[');

    PCom.skipVoid(tape);

    GLiteral literal;

    while ((literal = tryLiteral(tape)) != null) {
      list.add(literal);

      PCom.skipVoid(tape);
    }

    PCom.expectSymbol(tape, ']');

    return new GArray(list.toArray(new GLiteral[0]));
  }

  static GMap expectMap(Tape tape) {
    Map<String, GLiteral> map = new LinkedHashMap<>();

    PCom.expectSymbol(tape, '{');

    PCom.skipVoid(tape);

    while (!PCom.trySymbol(tape, '}')) {
      String keyName = PTok.expectName(tape, "key name");

      PCom.skipVoid(tape);

      PCom.expectSymbol(tape, ':');

      PCom.skipVoid(tape);

      GLiteral value = expectLiteral(tape);

      PCom.skipVoid(tape);

      if (map.put(keyName, value) != null) {
        throw new GrammarException("Key already exists: " + PCom.inspect(keyName), tape.getLocation());
      }
    }

    return new GMap(map);
  }
}
