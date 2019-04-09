package org.bakasoft.gramat.parsers;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.elements.*;
import org.bakasoft.gramat.parsing.elements.captures.GCapture;
import org.bakasoft.gramat.parsing.elements.captures.GObject;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.ArrayList;

interface PExp {

  static GElement expectExpression(Tape tape) {
    ArrayList<GElement> expressions = new ArrayList<>();
    ArrayList<GElement> buffer = new ArrayList<>();
    Runnable flushBuffer = () -> {
      if (buffer.isEmpty()) {
        throw new GrammarException("expected some elements", tape.getLocation());
      }
      else if (buffer.size() == 1) {
        expressions.add(buffer.get(0));
      }
      else {
        expressions.add(new GSequence(buffer.toArray(new GElement[0])));
      }

      buffer.clear();
    };

    GElement unit;
    boolean createAlternation = false;

    while ((unit = tryExpressionUnit(tape)) != null) {
      if (createAlternation) {
        createAlternation = false;

        flushBuffer.run();
      }

      buffer.add(unit);

      PCom.skipVoid(tape);

      if (PCom.trySymbol(tape, '|')) {
        PCom.skipVoid(tape);

        createAlternation = true;
      }
    }

    if (createAlternation) {
      throw new GrammarException("Expected expression after " + PCom.inspect('|'), tape.getLocation());
    }

    flushBuffer.run();

    if (expressions.isEmpty()) {
      throw new GrammarException("Invalid expression", tape.getLocation());
    }
    else if (expressions.size() == 1) {
      return expressions.get(0);
    }

    return new GAlternation(expressions.toArray(new GElement[0]));
  }

  static GElement expectExpressionUnit(Tape tape) {
    GElement unit = tryExpressionUnit(tape);

    if (unit == null) {
      throw new GrammarException("Expected expression", tape.getLocation());
    }

    return unit;
  }

  static GElement tryExpressionUnit(Tape tape) {
    int pos0 = tape.getPosition();

    // check for statements beginning
    if (PStm.isStatementBeginning(tape)) {
      tape.setPosition(pos0);
      return null;
    }
    else if (PCom.isLetter(tape)) {
      String name = PTok.expectName(tape, "reference or type name");

      PCom.skipVoid(tape);

      // check for object
      if (PCom.trySymbol(tape, ':')) {
        PCom.skipVoid(tape);

        return new GObject(name, expectExpressionUnit(tape));
      }
      // otherwise should be a reference
      else {
        return new GReference(name);
      }
    }
    else if (PCom.isChar(tape, '<')) {
      return expectCapture(tape);
    }
    else if (PCom.isChar(tape, '{')) {
      return expectRepetition(tape);
    }
    else if (PCom.isChar(tape, '[')) {
      return expectOptional(tape);
    }
    else if (PCom.isChar(tape, '!')) {
      return expectNegation(tape);
    }
    else if (PCom.isChar(tape, '(')) {
      return expectGroup(tape);
    }
    else if (PCom.isChar(tape, '"')) {
      return expectString(tape);
    }
    else if (PCom.isChar(tape, '`')) {
      return expectPredicate(tape);
    }
    else if (PCom.trySymbol(tape, '$')) {
      return new GTerminator();
    }

    // unknown char
    tape.setPosition(pos0);
    return null;
  }

  static GString expectString(Tape tape) {
    String content = PStr.expectQuotedToken(tape, '"');

    return new GString(content);
  }

  static GElement expectGroup(Tape tape) {
    PCom.expectSymbol(tape, '(');

    PCom.skipVoid(tape);

    GElement expression = expectExpression(tape);

    PCom.skipVoid(tape);

    PCom.expectSymbol(tape, ')');

    return expression;
  }

  static GRepetition expectRepetition(Tape tape) {
    PCom.expectSymbol(tape, '{');

    PCom.skipVoid(tape);

    Integer minimum = PCom.tryInteger(tape);
    Integer maximum;

    if (minimum != null) {
      PCom.skipVoid(tape);

      if (PCom.trySymbol(tape, ',')) {
        PCom.skipVoid(tape);

        maximum = PCom.expectInteger(tape);

        PCom.skipVoid(tape);
      }
      else if (PCom.trySymbol(tape, ';')) {
        PCom.skipVoid(tape);

        maximum = minimum;
      }
      else {
        maximum = null;
      }
    }
    else {
      maximum = null;
    }

    GElement expression = expectExpression(tape);
    GElement separator;

    PCom.skipVoid(tape);

    if (PCom.trySymbol(tape, '/')) {
      PCom.skipVoid(tape);

      separator = expectExpression(tape);

      PCom.skipVoid(tape);
    }
    else {
      separator = null;
    }

    PCom.expectSymbol(tape, '}');

    return new GRepetition(minimum, maximum, expression, separator);
  }

  static GPredicate expectPredicate(Tape tape) {
    ArrayList<GPredicate.Condition> conditions = new ArrayList<>();

    PCom.expectSymbol(tape, '`');

    while (!PCom.isChar(tape, '`')) {
      if (PCom.isWhitespace(tape)) {
        throw new GrammarException("unexpected whitespace", tape.getLocation());
      }

      char c = PStr.readStringChar(tape);

      if (PCom.trySymbols(tape, "..")) {
        char begin = c;
        char end = PStr.readStringChar(tape);

        conditions.add(new GPredicate.Range(begin, end));
      }
      else if (PCom.isWhitespace(tape) || PCom.isChar(tape, '`')) {
        conditions.add(new GPredicate.Option(c));
      }
      else {
        throw new GrammarException("expected whitespace", tape.getLocation());
      }

      PCom.skipVoid(tape);
    }

    PCom.expectSymbol(tape, '`');

    return new GPredicate(conditions.toArray(new GPredicate.Condition[0]));
  }

  static GOptional expectOptional(Tape tape) {
    PCom.expectSymbol(tape, '[');

    PCom.skipVoid(tape);

    GElement expression = expectExpression(tape);

    PCom.skipVoid(tape);

    PCom.expectSymbol(tape, ']');

    return new GOptional(expression);
  }

  static GNegation expectNegation(Tape tape) {
    PCom.expectSymbol(tape, '!');

    PCom.skipVoid(tape);

    GElement expression = expectExpressionUnit(tape);

    return new GNegation(expression);
  }


  static GCapture expectCapture(Tape tape) {
    PCom.expectSymbol(tape, '<');

    PCom.skipVoid(tape);

    String name = PTok.expectName(tape, "capture kind");
    ArrayList<GLiteral> options = new ArrayList<>();
    ArrayList<GElement> arguments = new ArrayList<>();

    PCom.skipVoid(tape);

    while (!PCom.trySymbol(tape, ':')) {
      GLiteral option = PLit.expectLiteral(tape);

      options.add(option);

      PCom.skipVoid(tape);
    }

    PCom.skipVoid(tape);

    while (!PCom.trySymbol(tape, '>')) {
      GElement argument = expectExpression(tape);

      arguments.add(argument);

      PCom.skipVoid(tape);

      if (PCom.trySymbol(tape, ',')) {
        PCom.skipVoid(tape);
      }
    }

    return GCapture.create(tape, name, options.toArray(new GLiteral[0]), arguments.toArray(new GElement[0]));
  }

}
