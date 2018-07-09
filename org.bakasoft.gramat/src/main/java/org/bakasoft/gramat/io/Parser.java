package org.bakasoft.gramat.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.bakasoft.gramat.building.ExpressionBuilder;
import org.bakasoft.gramat.building.GrammarBuilder;
import org.bakasoft.gramat.building.ImportBuilder;
import org.bakasoft.gramat.building.PropertyBuilder;
import org.bakasoft.gramat.building.PropertyType;
import org.bakasoft.gramat.building.ReferencedRuleBuilder;
import org.bakasoft.gramat.building.RepetitionBuilder;
import org.bakasoft.gramat.building.RuleBuilder;
import org.bakasoft.gramat.building.ConjunctionSequenceBuilder;
import org.bakasoft.gramat.building.DisjunctionSequenceBuilder;
import org.bakasoft.gramat.building.StatementBuilder;
import org.bakasoft.gramat.building.StringLiteralBuilder;
import org.bakasoft.gramat.building.WildCharBuilder;
import org.bakasoft.gramat.util.Tape;

public class Parser {
	
	public static GrammarBuilder parseGrammar(Tape tape) {
		GrammarBuilder grammarBuilder = new GrammarBuilder();
		
		ignoreWhitespace(tape);
		
		while(tape.isOpen()) {
			StatementBuilder statement = parseStatement(tape);
			
			ignoreWhitespace(tape);
			
			grammarBuilder.addStatement(statement);
		}
		
		return grammarBuilder;
	}

	public static StatementBuilder parseStatement(Tape tape) {
		if (tape.peek() == '@') {
			return parseDirective(tape);
		}
		
		return parseRule(tape);
	}
	
	public static StatementBuilder parseDirective(Tape tape) {
		if (tape.peek() != '@') {
			throw new RuntimeException("expected @");
		}
		
		tape.consume();
		
		String keyword = readKeyword(tape);
		
		ignoreWhitespace(tape);
		
		if (keyword.isEmpty()) {
			throw new RuntimeException("expected keyword");
		}
		else if ("import".equals(keyword)) {
			String externalPath = null;
			HashMap<String, String> nameMapping = new HashMap<>();
			boolean loop;
			do {
				String externalName = readIdentifier(tape);
				String localName = null;
				
				ignoreWhitespace(tape);
				
				if (tape.peek() != ',' && tape.peek() != ';' && tape.peek() != 'f') {
					if (!"as".equals(readKeyword(tape))) {
						throw new RuntimeException("expected 'as'");
					}
					
					ignoreWhitespace(tape);
					
					localName = readIdentifier(tape);
					
					ignoreWhitespace(tape);
				}
				
				if (tape.peek() == ',') {
					tape.consume();
					
					ignoreWhitespace(tape);
					loop = true;	
				}
				else if (tape.peek() != ';') {
					if (!"from".equals(readKeyword(tape))) {
						throw new RuntimeException("expected 'from'");
					}
					
					ignoreWhitespace(tape);
					
					externalPath = readIdentifier(tape);
					
					ignoreWhitespace(tape);
					
					loop = false;
				}
				else {
					loop = false;
				}
				
				// fallback name
				if (localName == null) {
					localName = externalName;
				}
				
				// TODO check duplicated
				nameMapping.put(localName, externalName);
			} while(loop);
			
			if (tape.peek() != ';') {
				throw new RuntimeException("expected ';'");
			}
			
			tape.consume();
			
			return new ImportBuilder(externalPath, nameMapping);
		}
		
		throw new RuntimeException("unknown directive: " + keyword);
	}

	public static RuleBuilder parseRule(Tape tape) {
		String name = readIdentifier(tape);
		
		ignoreWhitespace(tape);
		
		if (tape.peek() != '=') {
			throw new RuntimeException();
		}
		
		tape.consume();
		
		ignoreWhitespace(tape);
		
		ExpressionBuilder expression = parse_expression(tape, Constants.STATEMENT_SEPARATOR);
		
		if (tape.peek() != Constants.STATEMENT_SEPARATOR) {
			throw new RuntimeException("expected: " + Constants.STATEMENT_SEPARATOR);
		}
		
		tape.consume();
		
		return new RuleBuilder(name, expression);
	}
	
	public static ExpressionBuilder parseExpression(Tape tape) {
		return parse_expression(tape, Constants.STATEMENT_SEPARATOR);
	}
	
	public static ExpressionBuilder parseGroup(Tape tape) {
		boolean complement = false;
		
		if (tape.peek() != Constants.GROUP_START) {
			throw new RuntimeException("expected '('");
		}
		
		tape.consume();
		
		if (tape.peek() == Constants.COMPLEMENT_MARK) {
			tape.consume();
			complement = true;
		}
		
		ignoreWhitespace(tape);
		
		ExpressionBuilder expr = parse_expression(tape, Constants.GROUP_END);
		
		if (tape.peek() != Constants.GROUP_END) {
			throw new RuntimeException("expected ')'");
		}
		
		tape.consume();
		
		ignoreWhitespace(tape);
		
		if (complement) {
			return expr.getComplement();
		}
		
		return expr;
	}

	public static ExpressionBuilder parseWildChar(Tape tape) {
		if (tape.peek() != Constants.WILD_CHAR) {
			throw new RuntimeException("expected: " + Constants.WILD_CHAR);
		}
		
		tape.consume();
		
		return new WildCharBuilder();
	}
	
	public static ExpressionBuilder parseStringLiteral(Tape tape) {
		char delimiter = tape.peek();
		
		if (!Constants.STRING_DELIMITER.test(delimiter)) {
			throw new RuntimeException("unexpected: " + delimiter);
		}
		
		boolean fuzzy = (delimiter == Constants.FUZZY_STRING_DELIMITER);
		String text = Parser.readDelimitedString(delimiter, tape);
		
		return new StringLiteralBuilder(text, fuzzy);
	}
	
	public static ExpressionBuilder parseNamePrefixed(Tape tape) {
		String name = readIdentifier(tape);
		boolean canBeOneOrMore = !ignoreWhitespace(tape);
		
		if (tape.isOpen() && (tape.peek() == ':' || tape.peek() == '+')) {
			PropertyType type = null;
			boolean array = (tape.peek() == '+');
			
			tape.consume();
			
			if (array) {
				if (!tape.isOpen() || tape.peek() != ':') {
					if (canBeOneOrMore) {
						RepetitionBuilder result = new RepetitionBuilder(1, null);
						
						result.setExpression(new ReferencedRuleBuilder(name));
						
						return result;
					}
					else {
						throw new RuntimeException("Expected :");	
					}
				}
				
				tape.consume();	
			}
			
			if (tape.peek() == '#') {
				type = PropertyType.NUMBER;
				tape.consume();	
			} else if (tape.peek() == '?') {
				type = PropertyType.TRUE;
				tape.consume();	
			} else if (tape.peek() == '!') {
				type = PropertyType.FALSE;
				tape.consume();	
			} else if (tape.peek() == '@') {
				type = PropertyType.NULL;
				tape.consume();	
			}
			
			ignoreWhitespace(tape);
			
			char opening = tape.peek();
			char ending;
			
			if (opening == '{') {
				if (type != null) {
					throw new RuntimeException("cannot combine object property with " + type);
				}
				
				type = PropertyType.OBJECT;
				ending = '}';
			}
			else if (opening == '<') {
				if (type == null) {
					type = PropertyType.STRING;	
				}
				
				ending = '>';
			}
			else {
				throw new RuntimeException("Expected < or {");
			}
			
			tape.consume();
			
			ignoreWhitespace(tape);
			
			ExpressionBuilder expr = parse_expression(tape, ending);
			
			if (tape.peek() != ending) {
				throw new RuntimeException("Expected: " + ending);
			}
			
			tape.consume();
			
			PropertyBuilder result = new PropertyBuilder(name, type, array);
			
			result.setExpression(expr);
			
			return result;
		}
		
		return new ReferencedRuleBuilder(name);
	}
	
	/* CORE */
	
	private static ExpressionBuilder parse_expression(Tape tape, char endChar) {
		Stack<ArrayList<ExpressionBuilder>> groups = new Stack<>();
		
		while(tape.isOpen() && tape.peek() != endChar) {
			ExpressionBuilder item = parse_expression_item(tape);
			
			if (groups.isEmpty()) {
				groups.push(new ArrayList<ExpressionBuilder>());
			}
			
			groups.peek().add(item);
			
			ignoreWhitespace(tape);
			
			if (tape.isOpen() && tape.peek() == Constants.OR_OPERATOR) {
				tape.consume();
				
				ignoreWhitespace(tape);
				
				groups.push(new ArrayList<ExpressionBuilder>());
			}
		}
		
		ArrayList<ExpressionBuilder> expressions = new ArrayList<>();
		
		for (ArrayList<ExpressionBuilder> group : groups) {
			ExpressionBuilder expression;
			
			if (group.isEmpty()) {
				throw new RuntimeException("empty group");	
			}
			else if (group.size() == 1) {
				expression = group.get(0);
			} 
			else {
				ConjunctionSequenceBuilder seq = new ConjunctionSequenceBuilder();
				
				seq.addExpressions(group);
				
				expression = seq;
			}
			
			expressions.add(expression);
		}

		if (expressions.isEmpty()) {
			throw new RuntimeException("empty rule");
		} else if (expressions.size() == 1) {
			return expressions.get(0);
		}
		
		DisjunctionSequenceBuilder result = new DisjunctionSequenceBuilder();
		result.addExpressions(expressions);
		return result;
	}
	
	private static ExpressionBuilder parse_expression_item(Tape tape) {
		ExpressionBuilder expr;
		char c = tape.peek();
		
		if (c == Constants.GROUP_START) {
			expr = parseGroup(tape);
		}
		else if (c == Constants.WILD_CHAR) {
			expr = parseWildChar(tape);
		}
		else if (Constants.STRING_DELIMITER.test(c)) {
			expr = parseStringLiteral(tape);
		} 
		else if (c == Constants.IDENTIFIER_DELIMITER || !Constants.SPECIAL_CHAR.test(c)) {
			expr = parseNamePrefixed(tape);
		}
		else {
			throw new RuntimeException("unexpected: " + c);	
		}
		
		if (tape.isOpen()) {
			c = tape.peek();
			
			if (c == Constants.ZERO_OR_ONE_OPERATOR) {
				tape.consume();
				
				RepetitionBuilder r = new RepetitionBuilder(0, 1);
				r.setExpression(expr);
				expr = r;
			}
			else if (c == Constants.ZERO_OR_MORE_OPERATOR) {
				tape.consume();
				
				RepetitionBuilder r = new RepetitionBuilder(0, null);
				r.setExpression(expr);
				expr = r;
			}
			else if (c == Constants.ONE_OR_MORE_OPERATOR) {
				tape.consume();
				
				RepetitionBuilder r = new RepetitionBuilder(1, null);
				r.setExpression(expr);
				expr = r;
			}
			else if (c == '{') {
				tape.consume();
				
				ignoreWhitespace(tape);
				
				Integer minimum = readInteger(tape);
				
				if (minimum == null) {
					throw new RuntimeException("expected number");
				}
				
				ignoreWhitespace(tape);
				
				Integer maximum;
				
				if (tape.peek() == ',') {
					tape.consume();
					
					ignoreWhitespace(tape);	
					
					maximum = readInteger(tape);
					
					if (maximum != null) {
						ignoreWhitespace(tape);	
					}
				} else {
					maximum = minimum;
				}

				if (tape.peek() != '}') {
					throw new RuntimeException("unexpected: " + tape.peek());
				}
				
				tape.consume();
				
				RepetitionBuilder r = new RepetitionBuilder(minimum, maximum);
				r.setExpression(expr);
				expr = r;
			}
		}
		
		if (expr.getParent() != null) {
			expr.toString();
		}
		
		return expr;
	}
	
	
	/* UTIL */
	
	public static String readIdentifier(Tape tape) {
		char c = tape.peek();
		
		if (c == Constants.IDENTIFIER_DELIMITER) {
			return readDelimitedString(c, tape);
		} else if (Constants.SPECIAL_CHAR.test(c)) {
			throw new RuntimeException("unexpected: " + c);
		}
		
		StringBuilder name = new StringBuilder();
		
		do {
			name.append(c);
			
			tape.consume();
			
			if (tape.isOpen()) {
				c = tape.peek();	
			} else {
				break;
			}
		} 
		while(!Constants.SPECIAL_CHAR.test(c) && !Constants.WHITESPACE_CHAR.test(c));
		
		return name.toString();
	}
	
	public static Integer readInteger(Tape tape) {
		StringBuilder text = new StringBuilder();
		
		while(Constants.DIGIT_CHAR.test(tape.peek())) {
			text.append(tape.peek());
			
			tape.consume();
		}
		
		if (text.length() == 0) {
			return null;
		}
		
		return Integer.parseInt(text.toString());
	}
	
	public static String readKeyword(Tape tape) {
		StringBuilder text = new StringBuilder();
		
		while(Constants.ALPHA_LOWER_CHAR.test(tape.peek())) {
			text.append(tape.peek());
			
			tape.consume();
		}
		
		if (text.length() == 0) {
			return null;
		}
		
		return text.toString();
	}

	public static String readDelimitedString(char delimiter, Tape tape) {
		StringBuilder text = new StringBuilder();
		
		if (tape.peek() != delimiter) {
			throw new RuntimeException();
		}
		
		tape.consume();
		
		while(tape.isOpen() && tape.peek() != delimiter) {
			if (tape.peek() == Constants.ESCAPE_CHAR) {
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
	
	public static boolean ignoreWhitespace(Tape tape) {
		boolean consumed = false;
		
		while(tape.isOpen() && Constants.WHITESPACE_CHAR.test(tape.peek())) {
			tape.consume();
			consumed = true;
		}
		
		if (tape.isOpen() && tape.peek() == '/') {
			tape.consume();
			
			if (tape.peek() != '*') {
				throw new RuntimeException();
			}
			
			tape.consume();
			
			consumed = true;
			
			if (tape.isOpen()) {
				while (tape.peek() != '*') {
					tape.consume();
				}
				
				if (tape.peek() != '*') {
					throw new RuntimeException();
				}
				
				tape.consume();
				
				if (tape.peek() != '/') {
					throw new RuntimeException();
				}
				
				tape.consume();
			}
			
			while(tape.isOpen() && Constants.WHITESPACE_CHAR.test(tape.peek())) {
				tape.consume();
			}
		}
		
		// TODO: what about: "    /* */    /*   */"
		
		return consumed;
	}
	
	
}
