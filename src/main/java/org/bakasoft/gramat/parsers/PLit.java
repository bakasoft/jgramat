package org.bakasoft.gramat.parsers;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.literals.GArray;
import org.bakasoft.gramat.parsing.literals.GLiteral;
import org.bakasoft.gramat.parsing.literals.GMap;
import org.bakasoft.gramat.parsing.literals.GToken;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

// Parsing literals
interface PLit {

  static GLiteral expectLiteral(Gramat gramat, Tape tape) {
    GLiteral literal = tryLiteral(gramat, tape);

    if (literal == null) {
      throw new GrammarException("Expected literal", tape.getLocation());
    }

    return literal;
  }

  static GLiteral tryLiteral(Gramat gramat, Tape tape) {
    if (PCom.trySymbol(tape, PCat.VARIABLE_MARK)) {
      String name = PTok.expectName(tape, "variable name");
      Object value = gramat.getVariable(name);

      if (value == null) {
        throw new RuntimeException("not defined variable: " + name);
      }

      return GLiteral.forceLiteral(value);
    }
    else if (PCom.isChar(tape, PCat.QUOTED_TOKEN_DELIMITER) || PCom.isLetter(tape)) {
      return expectToken(tape);
    }
    else if (PCom.isChar(tape, '[')) {
      return expectArray(gramat, tape);
    }
    else if (PCom.isChar(tape, '{')) {
      return expectMap(gramat, tape);
    }

    return null;
  }

  static GToken expectToken(Tape tape) {
    String content = PTok.expectName(tape, "token");

    return new GToken(content);
  }

  static GArray expectArray(Gramat gramat, Tape tape) {
    ArrayList<GLiteral> list = new ArrayList<>();

    PCom.expectSymbol(tape, '[');

    PCom.skipVoid(tape);

    GLiteral literal;

    while ((literal = tryLiteral(gramat, tape)) != null) {
      list.add(literal);

      PCom.skipVoid(tape);
    }

    PCom.expectSymbol(tape, ']');

    return new GArray(list);
  }

  static GMap expectMap(Gramat gramat, Tape tape) {
    GMap map = new GMap();

    PCom.expectSymbol(tape, '{');

    PCom.skipVoid(tape);

    while (!PCom.trySymbol(tape, '}')) {
      String keyName = PTok.expectName(tape, "key name");

      PCom.skipVoid(tape);

      PCom.expectSymbol(tape, ':');

      PCom.skipVoid(tape);

      GLiteral value = expectLiteral(gramat, tape);

      PCom.skipVoid(tape);

      if (map.put(keyName, value) != null) {
        throw new GrammarException("Key already exists: " + PCom.inspect(keyName), tape.getLocation());
      }
    }

    return map;
  }
}
