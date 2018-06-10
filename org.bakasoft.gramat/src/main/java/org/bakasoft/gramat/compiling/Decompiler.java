package org.bakasoft.gramat.compiling;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.bakasoft.gramat.Engine;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.propertyExpressions.NumberProperty;
import org.bakasoft.gramat.propertyExpressions.ObjectProperty;
import org.bakasoft.gramat.propertyExpressions.StringProperty;
import org.bakasoft.gramat.regularExpressions.Complement;
import org.bakasoft.gramat.regularExpressions.ConjunctionSequence;
import org.bakasoft.gramat.regularExpressions.DisjunctionSequence;
import org.bakasoft.gramat.regularExpressions.FuzzyString;
import org.bakasoft.gramat.regularExpressions.OneOrMore;
import org.bakasoft.gramat.regularExpressions.StrictString;
import org.bakasoft.gramat.regularExpressions.ZeroOrMore;
import org.bakasoft.gramat.regularExpressions.ZeroOrOne;

public class Decompiler {

	public static String decompileExpression(Expression expr) {
		try (
				StringWriter writer = new StringWriter(); 
				PrintWriter printer = new PrintWriter(writer)) {
			Grammar grammar = expr.getGrammar();
			Engine engine = grammar.getEngine();
			
			decompile(engine, grammar, expr, writer);
			
			return writer.toString();	
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void decompile(Engine engine, Grammar grammar, Expression expr, StringWriter writer) {
		String id = grammar.getId(expr);
		
		if (id != null) {
			writeId(id, writer);
		} else if (expr instanceof StrictString) {
			writeStrictString(engine, grammar, (StrictString)expr, writer);
		} else if (expr instanceof FuzzyString) {
			writeFuzzyString(engine, grammar, (FuzzyString)expr, writer);
		} else if (expr instanceof Complement) {
			writeComplement(engine, grammar, (Complement)expr, writer);
		} else if (expr instanceof ConjunctionSequence) {
			writeConjunctionSequence(engine, grammar, (ConjunctionSequence)expr, writer);
		} else if (expr instanceof DisjunctionSequence) {
			writeDisjunctionSequence(engine, grammar, (DisjunctionSequence)expr, writer);
		} else if (expr instanceof ZeroOrOne) {
			writeZeroOrOne(engine, grammar, (ZeroOrOne)expr, writer);
		} else if (expr instanceof OneOrMore) {
			writeOneOrMore(engine, grammar, (OneOrMore)expr, writer);
		} else if (expr instanceof ZeroOrMore) {
			writeZeroOrMore(engine, grammar, (ZeroOrMore)expr, writer);
		} else if (expr instanceof StringProperty) {
			writeStringProperty(engine, grammar, (StringProperty)expr, writer);
		} else if (expr instanceof NumberProperty) {
			writeNumberProperty(engine, grammar, (NumberProperty)expr, writer);
		} else if (expr instanceof ObjectProperty) {
			writeObjectProperty(engine, grammar, (ObjectProperty)expr, writer);
		} else {
			throw new RuntimeException("unknown type: " + expr.getClass());
		}
	}

	private static void writeObjectProperty(Engine engine, Grammar grammar, ObjectProperty expr, StringWriter writer) {
		writer.write('{');
		writeId(expr.getPropertyName(), writer);
		writer.write(": ");
		decompile(engine, grammar, expr.getValueExpression(), writer);
		writer.write('}');
	}

	private static void writeZeroOrMore(Engine engine, Grammar grammar, ZeroOrMore expr, StringWriter writer) {
		decompile(engine, grammar, expr.getExpression(), writer);
		writer.write('*');
	}

	private static void writeOneOrMore(Engine engine, Grammar grammar, OneOrMore expr, StringWriter writer) {
		decompile(engine, grammar, expr.getExpression(), writer);
		writer.write('+');
	}

	private static void writeZeroOrOne(Engine engine, Grammar grammar, ZeroOrOne expr, StringWriter writer) {
		decompile(engine, grammar, expr.getExpression(), writer);
		writer.write('?');
	}

	private static void writeConjunctionSequence(Engine engine, Grammar grammar, ConjunctionSequence expr, StringWriter writer) {
		Expression[] items = expr.getExpressions();
		
		for (int i = 0; i < items.length; i++) {
			if (i > 0) {
				writer.write(' ');
			}
			
			decompile(engine, grammar, items[i], writer);
		}
	}
	
	private static void writeDisjunctionSequence(Engine engine, Grammar grammar, DisjunctionSequence expr, StringWriter writer) {
		Expression[] items = expr.getExpressions();
		
		for (int i = 0; i < items.length; i++) {
			if (i > 0) {
				writer.write(" | ");
			}
			
			decompile(engine, grammar, items[i], writer);
		}	
	}

	private static void writeStringProperty(Engine engine, Grammar grammar, StringProperty expr, StringWriter writer) {
		writer.write('<');
		writeId(expr.getPropertyName(), writer);
		writer.write(": ");
		decompile(engine, grammar, expr.getValueExpression(), writer);
		writer.write('>');
	}
	
	private static void writeNumberProperty(Engine engine, Grammar grammar, NumberProperty expr, StringWriter writer) {
		writer.write('<');
		writeId(expr.getPropertyName(), writer);
		writer.write(":# ");
		decompile(engine, grammar, expr.getValueExpression(), writer);
		writer.write('>');
	}

	private static void writeStrictString(Engine engine, Grammar grammar, StrictString expr, StringWriter writer) {
		String value = expr.getValue();
		
		writeDelimitedString('\"', value, writer);
	}

	private static void writeFuzzyString(Engine engine, Grammar grammar, FuzzyString expr, StringWriter writer) {
		String value = expr.getValue();
		
		writeDelimitedString('~', value, writer);
	}

	private static void writeComplement(Engine engine, Grammar grammar, Complement expr, StringWriter writer) {
		writer.write("(!");
		
		decompile(engine, grammar, expr.getExpression(), writer);
		
		writer.write(")");
	}
	
	private static void writeId(String id, StringWriter writer) {
		writer.write(id); // TODO escape?
	}

	private static void writeDelimitedString(char delimiter, String value, StringWriter writer) {
		writer.write(delimiter);
		writer.write(value); // TODO escape!!!
		writer.write(delimiter);
	}
	
}
