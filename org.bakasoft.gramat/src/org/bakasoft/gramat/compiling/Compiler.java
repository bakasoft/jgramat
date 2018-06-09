package org.bakasoft.gramat.compiling;

import java.util.ArrayList;
import java.util.Stack;

import org.bakasoft.gramat.Engine;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.compiling.importers.ImportResolver;
import org.bakasoft.gramat.util.Tape;
import org.bakasoft.gramat.errors.CompilationException;
import org.bakasoft.gramat.propertyExpressions.FalseProperty;
import org.bakasoft.gramat.propertyExpressions.NullProperty;
import org.bakasoft.gramat.propertyExpressions.NumberProperty;
import org.bakasoft.gramat.propertyExpressions.ObjectProperty;
import org.bakasoft.gramat.propertyExpressions.StringProperty;
import org.bakasoft.gramat.propertyExpressions.TrueProperty;
import org.bakasoft.gramat.regularExpressions.Complement;
import org.bakasoft.gramat.regularExpressions.ConjunctionSequence;
import org.bakasoft.gramat.regularExpressions.DisjunctionSequence;
import org.bakasoft.gramat.regularExpressions.FuzzyString;
import org.bakasoft.gramat.regularExpressions.OneOrMore;
import org.bakasoft.gramat.regularExpressions.ReferencedExpression;
import org.bakasoft.gramat.regularExpressions.StrictString;
import org.bakasoft.gramat.regularExpressions.ZeroOrMore;
import org.bakasoft.gramat.regularExpressions.ZeroOrOne;
import org.bakasoft.gramat.util.CharPredicate;

public class Compiler {

	public static final char COMMA = ',';
	public static final char LEFT_SQUARE_BRACKET = '[';
	public static final char RIGHT_SQUARE_BRACKET = ']';
	
	
	public static final char IDENTIFIER_DELIMITER = '`';
	public static final char ESCAPE_CHAR = '\\';
	public static final char ELEMENT_ASSIGNMENT_OPERATOR = '=';
	public static final char STATEMENT_SEPARATOR = ';';
	public static final char OR_OPERATOR = '|';
	public static final char GROUP_START = '(';
	public static final char GROUP_END = ')';
	public static final char ZERO_OR_ONE_OPERATOR = '?';
	public static final char ZERO_OR_MORE_OPERATOR = '*';
	public static final char ONE_OR_MORE_OPERATOR = '+';
	public static final char FUZZY_STRING_DELIMITER = '~';
	public static final char PRIMITIVE_START = '<';
	public static final char PRIMITIVE_END = '>';
	public static final char OBJECT_START = '{';
	public static final char OBJECT_END = '}';
	public static final char PROPERTY_ASSIGNMENT_OPERATOR = ':';
	public static final char STRICT_STRING_DELIMITER_1 = '"';
	public static final char STRICT_STRING_DELIMITER_2 = '\'';
	public static final char FALSE_PROPERTY_MARK = '!';
	public static final char TRUE_PROPERTY_MARK = '?';
	public static final char ARRAY_PROPERTY_MARK = '+';
	public static final char NUMBER_PROPERTY_MARK = '#';
	public static final char NULL_PROPERTY_MARK = '@';
	public static final char DIRECTIVE_PREFFIX = '@';
	
	public static final String REQUIRE_DIRECTIVE = "require";
	
	private static final CharPredicate whitespaceChar = (c -> c == ' ' || c == '\t' || c == '\n' || c == '\r');
	private static final CharPredicate alphaLowerChar = (c -> c >= 'a' && c <= 'z');
	private static final CharPredicate specialChar = (c -> {
		switch(c) {
		case '+': // ONE_OR_MORE_OPERATOR | ARRAY_PROPERTY_MARK
		case '?': // ZERO_OR_ONE_OPERATOR | TRUE_PROPERTY_MARK
		case '@': // NULL_PROPERTY_MARK | DIRECTIVE_PREFFIX
		case IDENTIFIER_DELIMITER:
		case ESCAPE_CHAR:
		case ELEMENT_ASSIGNMENT_OPERATOR:
		case STATEMENT_SEPARATOR:
		case OR_OPERATOR:
		case GROUP_START:
		case GROUP_END:
		case ZERO_OR_MORE_OPERATOR:
		case FUZZY_STRING_DELIMITER:
		case PRIMITIVE_START:
		case PRIMITIVE_END:
		case OBJECT_START:
		case OBJECT_END:
		case PROPERTY_ASSIGNMENT_OPERATOR:
		case STRICT_STRING_DELIMITER_1:
		case STRICT_STRING_DELIMITER_2:
		case FALSE_PROPERTY_MARK:
		case NUMBER_PROPERTY_MARK:
			return true;
		default:
			return false;
		}
	});
	
	public static Grammar compileGrammar(Engine engine, Tape tape, ImportResolver importResolver) {
		try {
			Grammar grammar = new Grammar(engine);
			
			while(tape.isOpen()) {
				ignoreWhitespace(tape);
				
				if (tape.peek() == '@') {
					interpretDirective(engine, importResolver, grammar, tape);
				} else {
					String id = readIdentifier(tape);
					
					ignoreWhitespace(tape);
					
					tape.expect(ELEMENT_ASSIGNMENT_OPERATOR);
					
					Expression expr = compileExpression(grammar, tape, STATEMENT_SEPARATOR, false);
					
					grammar.registerRule(id, expr);	
				}
				
				ignoreWhitespace(tape);
			}
			
			return grammar;
		}
		catch (RuntimeException ex) {
			throw new CompilationException(ex, tape.getTextPosition());
		}
	}
	
	public static void interpretDirective(Engine engine, ImportResolver importResolver, Grammar grammar, Tape tape) {
		Runnable action;
		
		tape.expect(DIRECTIVE_PREFFIX);
		
		String directive = readReservedWord(tape);
		
		ignoreWhitespace(tape);
		
		if (REQUIRE_DIRECTIVE.equals(directive)) {
			String id = readIdentifier(tape);
			
			action = () -> {
				grammar.registerRule(id);
			};
		} 
		else if ("import".equals(directive)) {
			String id = readIdentifier(tape);
			
			// TODO add alias support
			
			ignoreWhitespace(tape);
			
			if (!"from".equals(readReservedWord(tape))) {
				throw new RuntimeException();
			}

			ignoreWhitespace(tape);

			String namespace = readIdentifier(tape);
			
			action = () -> {
				Grammar external = engine.findGrammar(namespace);
				Expression expr = external.findRule(id);
				
				grammar.registerRule(id, expr);
			};
		}  
		else if (directive.isEmpty()) {
			throw new RuntimeException();
		} 
		else {
			throw new RuntimeException("unknown directive: " + DIRECTIVE_PREFFIX + directive);
		}
		
		ignoreWhitespace(tape);
		
		tape.expect(STATEMENT_SEPARATOR);
		
		action.run();
	}

	private static String readReservedWord(Tape tape) {
		StringBuilder word = new StringBuilder();
		
		while(alphaLowerChar.test(tape.peek())) {
			word.append(tape.peek());
			
			tape.consume();
		}
		
		return word.toString();
	}

	public static Expression compileExpression(Grammar grammar, Tape tape) {
		try {
			return compileExpression(grammar, tape, STATEMENT_SEPARATOR, true);
		}
		catch (RuntimeException ex) {
			throw new CompilationException(ex, tape.getTextPosition());
		}
	}
	
	private static Expression compileExpression(Grammar grammar, Tape tape, char breaker, boolean optionalBreaker) {
		Stack<ArrayList<Expression>> groups = new Stack<>();
		
		ignoreWhitespace(tape);
		
		while (tape.isOpen() && tape.isNot(breaker)) {
			Expression expr;
			
			if (tape.is(GROUP_START)) {
				expr = compileGroup(grammar, tape);
			} else if (tape.is(PRIMITIVE_START) || tape.is(OBJECT_START)) {
				expr = compileProperty(grammar, tape);
			} else if (tape.is(STRICT_STRING_DELIMITER_1) || tape.is(STRICT_STRING_DELIMITER_2) || tape.is(FUZZY_STRING_DELIMITER)) {
				expr = compileCharacterSequence(grammar, tape);
			} else if (tape.is(IDENTIFIER_DELIMITER) || !specialChar.test(tape.peek())) {
				expr = compileReference(grammar, tape);
			} else {
				throw new RuntimeException("expected rule-item: " + tape.peek());
			}
			
			if (tape.isOpen()) {
				if (tape.is(ZERO_OR_ONE_OPERATOR)) {
					tape.consume();
					
					expr = new ZeroOrOne(grammar, expr);
				} else if (tape.is(ZERO_OR_MORE_OPERATOR)) {
					tape.consume();
					
					expr = new ZeroOrMore(grammar, expr);
				} else if (tape.is(ONE_OR_MORE_OPERATOR)) {
					tape.consume();
					
					expr = new OneOrMore(grammar, expr);
				}
			}
			
			if (groups.isEmpty()) {
				groups.push(new ArrayList<Expression>());
			}
			
			groups.peek().add(expr);
	
			ignoreWhitespace(tape);	
			
			if (tape.isOpen() && tape.isNot(breaker)) {
				if (tape.is(OR_OPERATOR)) {
					tape.consume();
					
					ignoreWhitespace(tape);
					
					groups.push(new ArrayList<Expression>());
				}
			}
		}
		
		if (optionalBreaker) {
			if (tape.isOpen() && tape.is(breaker)) {
				tape.consume();
			}
		} else {
			tape.expect(breaker);
		}
		
		ArrayList<Expression> expressions = new ArrayList<>();
		
		for (ArrayList<Expression> group : groups) {
			Expression expression;
			
			if (group.isEmpty()) {
				throw new RuntimeException("empty rule");	
			}
			else if (group.size() == 1) {
				expression = group.get(0);
			} 
			else {
				expression = new ConjunctionSequence(grammar, group.toArray(new Expression[group.size()]));	
			}
			
			expressions.add(expression);
		}

		if (expressions.isEmpty()) {
			throw new RuntimeException("empty rule");
		} else if (expressions.size() == 1) {
			return expressions.get(0);
		}
		
		return new DisjunctionSequence(grammar, expressions.toArray(new Expression[expressions.size()]));
	}
	
	public static Expression compileGroup(Grammar grammar, Tape tape) {
		boolean complement = false;
		
		tape.expect('(');
		
		if (tape.is('!')) {
			tape.consume();
			
			complement = true;
		}
		
		Expression expr = compileExpression(grammar, tape, ')', false);
		
		if (complement) {
			return new Complement(grammar, expr);
		}
		
		return expr;
	}
	
	public static Expression compileReference(Grammar grammar, Tape tape) {
		String id = readIdentifier(tape);
		Expression expr = grammar.findRule(id);
		
		if (expr != null) {
			return expr;	
		}
		
		return new ReferencedExpression(grammar, id);
	}
	
	public static Expression compileProperty(Grammar grammar, Tape tape) {
		final int OBJECT_DATATYPE = 1;
		final int STRING_DATATYPE = 2;
		final int NUMBER_DATATYPE = 3;
		final int TRUE_DATATYPE = 4;
		final int FALSE_DATATYPE = 5;
		final int NULL_DATATYPE = 6;
		
		char startSymbol = tape.peek();
		char endSymbol;
		char dataType;
		
		if (startSymbol == PRIMITIVE_START) {
			endSymbol = PRIMITIVE_END;
			dataType = STRING_DATATYPE;
		} else if (startSymbol == OBJECT_START) {
			endSymbol = OBJECT_END;
			dataType = OBJECT_DATATYPE;
		} else {
			throw new RuntimeException("expected '{' or '<'");
		}
		
		tape.consume();
		
		ignoreWhitespace(tape);
		
		String fieldName = readIdentifier(tape);
		
		ignoreWhitespace(tape);
		
		// determine if it is array
		boolean isArray;
		
		if (tape.is(ARRAY_PROPERTY_MARK)) {
			isArray = true;
			tape.consume();
		} else {
			isArray = false;
		}
		
		tape.expect(PROPERTY_ASSIGNMENT_OPERATOR);
		
		// determine primitive type
		if (dataType != OBJECT_DATATYPE) {
			if (tape.is(NUMBER_PROPERTY_MARK)) {
				dataType = NUMBER_DATATYPE;
				tape.consume();
			} else if (tape.is(TRUE_PROPERTY_MARK)) {
				dataType = TRUE_DATATYPE;
				tape.consume();
			} else if (tape.is(FALSE_PROPERTY_MARK)) {
				dataType = FALSE_DATATYPE;
				tape.consume();
			} else if (tape.is(NULL_PROPERTY_MARK)) {
				dataType = NULL_DATATYPE;
				tape.consume();
			}	
		}
		
		ignoreWhitespace(tape);
		
		Expression expr = compileExpression(grammar, tape, endSymbol, false);
	
		if (dataType == OBJECT_DATATYPE) {
			return new ObjectProperty(grammar, fieldName, isArray, expr);
		}
		else if (dataType == STRING_DATATYPE) {
			return new StringProperty(grammar, fieldName, isArray, expr);
		} 
		else if (dataType == NUMBER_DATATYPE) {
			return new NumberProperty(grammar, fieldName, isArray, expr);
		} 
		else if (dataType == TRUE_DATATYPE) {
			return new TrueProperty(grammar, fieldName, isArray, expr);
		} 
		else if (dataType == FALSE_DATATYPE) {
			return new FalseProperty(grammar, fieldName, isArray, expr);
		} 
		else if (dataType == NULL_DATATYPE) {
			return new NullProperty(grammar, fieldName, isArray, expr);
		} 
		else {
			throw new IllegalStateException();
		}
	}

	public static Expression compileCharacterSequence(Grammar grammar, Tape tape) {
		char delimiter = tape.peek();
		
		if (delimiter != STRICT_STRING_DELIMITER_1 && delimiter != STRICT_STRING_DELIMITER_2 && delimiter != FUZZY_STRING_DELIMITER) {
			throw new RuntimeException("unexpected: " + delimiter);
		}
		
		String value = readDelimitedString(delimiter, tape);
		
		if (delimiter == FUZZY_STRING_DELIMITER) {
			return new FuzzyString(grammar, value);	
		}
		
		return new StrictString(grammar, value);
	}
	
	public static String readIdentifier(Tape tape) {
		String id;
		
		if (tape.peek() == IDENTIFIER_DELIMITER) {
			id = readDelimitedString(IDENTIFIER_DELIMITER, tape);
		} else {
			StringBuilder output = new StringBuilder();
			
			while(tape.isOpen() && !(specialChar.test(tape.peek()) || whitespaceChar.test(tape.peek()))) {
				output.append(tape.peek());
				
				tape.consume();
			}
			
			id = output.toString();
		}
		
		if (id.length() == 0) {
			throw new RuntimeException("expected to read an identifier " + tape.getTextPosition());
		}
		
		return id;
	}
	
	public static String readDelimitedString(char delimiter, Tape tape) {
		StringBuilder text = new StringBuilder();
		
		tape.expect(delimiter);
		
		while(tape.isOpen() && !tape.is(delimiter)) {
			if (tape.is(ESCAPE_CHAR)) {
				tape.consume();
				
				char c = tape.peek();
				tape.consume();
				
				switch(c) {
				case '\"': 
 				case '\'': 
 				case '`': 
 				case '\\': 
 				case '/': 
 					text.append(c); 
 					break;
 				case 'b': text.append('\b'); break; // backspace
 				case 'f': text.append('\f'); break; // form feed
 				case 'n': text.append('\n'); break; // line feed
 				case 'r': text.append('\r'); break; // carriage return
 				case 't': text.append('\t'); break; // horizontal tab
 				case 'u': 
 					char h1 = tape.peek();
 					tape.consume();
 					char h2 = tape.peek();
 					tape.consume();
 					char h3 = tape.peek();
 					tape.consume();
 					char h4 = tape.peek();
 					tape.consume();
 					
 					text.append((char)Integer.parseInt(new String(new char[] { h1, h2, h3, h4 }), 16)); 
 					break;
				}
			} else {
				text.append(tape.peek());
				tape.consume();
			}
		}
		
		tape.consume();
		
		return text.toString();
	}
	
	public static void ignoreWhitespace(Tape tape) {
		while(tape.isOpen() && tape.is(whitespaceChar)) {
			tape.consume();
		}
		
		if (tape.isOpen() && tape.is('/')) {
			tape.consume();
			tape.expect('*');
			
			if (tape.isOpen()) {
				while (tape.peek() != '*') {
					tape.consume();
				}
				
				tape.expect('*');
				tape.expect('/');
			}
			
			while(tape.isOpen() && tape.is(whitespaceChar)) {
				tape.consume();
			}
		}
	}
	
}
