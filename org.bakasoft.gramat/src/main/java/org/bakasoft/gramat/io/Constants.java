package org.bakasoft.gramat.io;

import org.bakasoft.gramat.util.CharPredicate;

public class Constants {


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
	public static final char COMPLEMENT_MARK = '!';
	public static final char TRUE_PROPERTY_MARK = '?';
	public static final char ARRAY_PROPERTY_MARK = '+';
	public static final char NUMBER_PROPERTY_MARK = '#';
	public static final char NULL_PROPERTY_MARK = '@';
	public static final char DIRECTIVE_PREFFIX = '@';
	public static final char WILD_CHAR = '.';
	
	public static final String REQUIRE_DIRECTIVE = "require";
	
	public static final CharPredicate WHITESPACE_CHAR = (c -> c == ' ' || c == '\t' || c == '\n' || c == '\r');
	public static final CharPredicate ALPHA_LOWER_CHAR = (c -> c >= 'a' && c <= 'z');
	public static final CharPredicate DIGIT_CHAR = (c -> c >= '0' && c <= '9');
	public static final CharPredicate STRING_DELIMITER = (c -> c == STRICT_STRING_DELIMITER_1 || c == STRICT_STRING_DELIMITER_2 || c == FUZZY_STRING_DELIMITER );
	public static final CharPredicate SPECIAL_CHAR = (c -> {
		switch(c) {
		case '+': // ONE_OR_MORE_OPERATOR | ARRAY_PROPERTY_MARK
		case '?': // ZERO_OR_ONE_OPERATOR | TRUE_PROPERTY_MARK
		case '@': // NULL_PROPERTY_MARK | DIRECTIVE_PREFFIX
		case '!': // FALSE_PROPERTY_MARK | COMPLEMENT_MARK
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
		case NUMBER_PROPERTY_MARK:
		case WILD_CHAR:
			return true;
		default:
			return false;
		}
	});
	
}
