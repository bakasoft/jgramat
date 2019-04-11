package org.bakasoft.gramat.parsers;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Location;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.elements.*;
import org.bakasoft.gramat.parsing.literals.GArray;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.ArrayList;

// Parsing expressions
interface PExp {

  static GExpression expectExpression(Gramat gramat, Tape tape) {
    Location location = tape.getLocation();
    ArrayList<GExpression> expressions = new ArrayList<>();
    ArrayList<GExpression> buffer = new ArrayList<>();
    Runnable flushBuffer = () -> {
      if (buffer.isEmpty()) {
        throw new GrammarException("expected some elements", tape.getLocation());
      }
      else if (buffer.size() == 1) {
        expressions.add(buffer.get(0));
      }
      else {
        expressions.add(new GSequence(location.range(), gramat, buffer.toArray(new GExpression[0])));
      }

      buffer.clear();
    };

    GExpression unit;
    boolean createAlternation = false;

    while ((unit = tryExpressionUnit(gramat, tape)) != null) {
      if (createAlternation) {
        createAlternation = false;

        flushBuffer.run();
      }

      buffer.add(unit);

      PCom.skipVoid(tape);

      if (PCom.trySymbol(tape, PCat.ALTERNATION_OPERATOR)) {
        PCom.skipVoid(tape);

        createAlternation = true;
      }
    }

    if (createAlternation) {
      throw new GrammarException("Expected expression after " + PCom.inspect(PCat.ALTERNATION_OPERATOR), tape.getLocation());
    }

    if (buffer.isEmpty()) {
      throw new GrammarException("No expression found", tape.getLocation());
    }

    flushBuffer.run();

    if (expressions.isEmpty()) {
      throw new GrammarException("Invalid expression", tape.getLocation());
    }
    else if (expressions.size() == 1) {
      return expressions.get(0);
    }

    return new GAlternation(location.range(), gramat, expressions.toArray(new GExpression[0]));
  }

  static GExpression tryExpressionUnit(Gramat gramat, Tape tape) {
    int pos0 = tape.getPosition();

    // check for statements beginning
    if (PStm.isStatementBeginning(tape)) {
      tape.setPosition(pos0);
      return null;
    }
    else if (PCom.isLetter(tape)) {
      return expectReference(gramat, tape);
    }
    else if (PCom.isChar(tape, PCat.CAPTURE_BEGIN)) {
      return expectCapture(gramat, tape);
    }
    else if (PCom.isChar(tape, PCat.REPETITION_BEGIN)) {
      return expectRepetition(gramat, tape);
    }
    else if (PCom.isChar(tape, PCat.OPTIONAL_BEGIN)) {
      return expectOptional(gramat, tape);
    }
    else if (PCom.isChar(tape, PCat.NEGATION_BEGIN)) {
      return expectNegation(gramat, tape);
    }
    else if (PCom.isChar(tape, PCat.GROUP_BEGIN)) {
      return expectGroup(gramat, tape);
    }
    else if (PCom.isChar(tape, PCat.SYMBOL_DELIMITER)) {
      return expectString(gramat, tape);
    }
    else if (PCom.isChar(tape, PCat.PREDICATE_DELIMITER)) {
      return expectPredicate(gramat, tape);
    }
    else if (PCom.trySymbol(tape, PCat.END_OPERATOR)) {
      return expectTerminator(gramat, tape);
    }

    // unknown char
    tape.setPosition(pos0);
    return null;
  }

  static GReference expectReference(Gramat gramat, Tape tape) {
    Location location = tape.getLocation();
    String name = PTok.expectName(tape, "reference");

    return new GReference(location.range(), gramat, name);
  }

  static GString expectString(Gramat gramat, Tape tape) {
    Location location = tape.getLocation();
    String content = PStr.expectQuotedToken(tape, PCat.SYMBOL_DELIMITER);

    return new GString(location.range(), gramat, content);
  }

  static GExpression expectGroup(Gramat gramat, Tape tape) {
    PCom.expectSymbol(tape, PCat.GROUP_BEGIN);
    PCom.skipVoid(tape);

    GExpression expression = expectExpression(gramat, tape);

    PCom.skipVoid(tape);
    PCom.expectSymbol(tape, PCat.GROUP_END);

    return expression;
  }

  static GRepetition expectRepetition(Gramat gramat, Tape tape) {
    Location location = tape.getLocation();

    PCom.expectSymbol(tape, PCat.REPETITION_BEGIN);
    PCom.skipVoid(tape);

    Integer minimum;
    Integer maximum;

    if (PCom.trySymbol(tape, PCat.REPETITION_ONE_MORE_MARK)) {
      PCom.skipVoid(tape);

      minimum = 1;
      maximum = null;
    }
    else {
      minimum = PCom.tryInteger(tape);

      PCom.skipVoid(tape);

      if (minimum != null) {
        if (PCom.trySymbol(tape, PCat.REPETITION_ONE_MORE_MARK)) {
          PCom.skipVoid(tape);

          maximum = null;
        }
        else if (PCom.trySymbol(tape, PCat.REPETITION_RANGE_SEPARATOR)) {
          maximum = PCom.expectInteger(tape);

          PCom.skipVoid(tape);
        }
        else {
          maximum = minimum;
        }
      }
      else {
        maximum = null;
      }
    }

    GExpression expression = expectExpression(gramat, tape);
    GExpression separator;

    PCom.skipVoid(tape);

    if (PCom.trySymbol(tape, PCat.REPETITION_SEPARATOR_MARK)) {
      PCom.skipVoid(tape);

      separator = expectExpression(gramat, tape);

      PCom.skipVoid(tape);
    }
    else {
      separator = null;
    }

    PCom.expectSymbol(tape, PCat.REPETITION_END);

    return new GRepetition(location.range(), gramat, minimum, maximum, expression, separator);
  }

  static GPredicate expectPredicate(Gramat gramat, Tape tape) {
    Location location = tape.getLocation();
    ArrayList<GPredicate.Condition> conditions = new ArrayList<>();

    PCom.expectSymbol(tape, PCat.PREDICATE_DELIMITER);

    while (!PCom.isChar(tape, PCat.PREDICATE_DELIMITER)) {
      if (PCom.isWhitespace(tape)) {
        throw new GrammarException("unexpected whitespace", tape.getLocation());
      }

      char c = PStr.readStringChar(tape);

      if (PCom.trySymbols(tape, PCat.PREDICATE_RANGE_OPERATOR)) {
        char end = PStr.readStringChar(tape);

        conditions.add(new GPredicate.Range(c, end));
      }
      else if (PCom.isWhitespace(tape) || PCom.isChar(tape, PCat.PREDICATE_DELIMITER)) {
        conditions.add(new GPredicate.Option(c));
      }
      else {
        throw new GrammarException("expected whitespace", tape.getLocation());
      }

      PCom.skipVoid(tape);
    }

    PCom.expectSymbol(tape, PCat.PREDICATE_DELIMITER);

    return new GPredicate(location.range(), gramat, conditions.toArray(new GPredicate.Condition[0]));
  }

  static GOptional expectOptional(Gramat gramat, Tape tape) {
    Location location = tape.getLocation();

    PCom.expectSymbol(tape, PCat.OPTIONAL_BEGIN);
    PCom.skipVoid(tape);

    GExpression expression = expectExpression(gramat, tape);

    PCom.skipVoid(tape);
    PCom.expectSymbol(tape, PCat.OPTIONAL_END);

    return new GOptional(location.range(), expression);
  }

  static GNegation expectNegation(Gramat gramat, Tape tape) {
    Location location = tape.getLocation();

    PCom.expectSymbol(tape, PCat.NEGATION_BEGIN);
    PCom.skipVoid(tape);

    GExpression expression = expectExpression(gramat, tape);

    PCom.skipVoid(tape);
    PCom.expectSymbol(tape, PCat.NEGATION_END);

    return new GNegation(location.range(), expression);
  }

  static GExpression expectCapture(Gramat gramat, Tape tape) {
    Location location = tape.getLocation();

    PCom.expectSymbol(tape, PCat.CAPTURE_BEGIN);
    PCom.skipVoid(tape);

    String keyword = PTok.expectName(tape, "capture keyword");

    PCom.skipVoid(tape);

    GArray options = new GArray();
    ArrayList<GExpression> arguments = new ArrayList<>();

    while (PCom.trySymbol(tape, PCat.CAPTURE_ARGUMENT_MARK)) {
      PCom.skipVoid(tape);

      GLiteral option = PLit.expectLiteral(gramat, tape);

      options.add(option);

      PCom.skipVoid(tape);
    }

    if (PCom.trySymbol(tape, PCat.GROUP_BEGIN)) {
      PCom.skipVoid(tape);

      do {
        GExpression argument = expectExpression(gramat, tape);

        arguments.add(argument);

        PCom.skipVoid(tape);

        if (PCom.trySymbol(tape, PCat.EXPRESSION_ARGUMENT_SEPARATOR)) {
          PCom.skipVoid(tape);
        }
      }
      while (!PCom.trySymbol(tape, PCat.GROUP_END));
    }

    return PCap.create(location, gramat, tape, keyword,
        options,
        arguments.toArray(new GExpression[0]));
  }

  static GExpression expectTerminator(Gramat gramat, Tape tape) {
    Location location = tape.getLocation();
    return new GTerminator(location.range(), gramat);
  }
}
