package gramat.parsers;

public class Mark {

    public static final String SET_KEYWORD = "set";
    public static final String OBJECT_KEYWORD = "object";
    public static final String LIST_KEYWORD = "list";
    public static final String JOIN_KEYWORD = "join";
    public static final String NULL_KEYWORD = "null";
    public static final String TRUE_KEYWORD = "true";
    public static final String MAP_KEYWORD = "map";
    public static final String DEBUG_KEYWORD = "debug";
    public static final String PRINT_KEYWORD = "print";

    public static final char TOKEN_DELIMITER = '`';
    public static final char LITERAL_DELIMITER = '\"';
    public static final char PREDICATE_DELIMITER = '\'';
    public static final char PREDICATE_ITEM_SEPARATOR = ' ';
    public static final char PREDICATE_RANGE_SEPARATOR = '-';

    public static final char VALUE_MARK = '@';
    public static final char ALTERNATION_MARK = '|';

    public static final char GROUP_BEGIN = '(';
    public static final char GROUP_END = ')';
    public static final char NAME_SEPARATOR = ':';
    public static final char HARD_ASSIGNMENT_MARK = '=';
    public static final char END_SOURCE_MARK = '$';
    public static final char BEGIN_SOURCE_MARK = '^';
    public static final char WILD_MARK = '*';

    public static final char REPETITION_BEGIN = '{';
    public static final char REPETITION_END = '}';
    public static final char REPETITION_SEPARATOR_MARK = '/';
    public static final char REPETITION_PLUS = '+';
    public static final char REPETITION_RANGE_SEPARATOR = ',';
    public static final char REPETITION_COUNT_MARK = ';';

    public static final char OPTIONAL_BEGIN = '[';
    public static final char OPTIONAL_END = ']';

    public static final char NEGATION_BEGIN = '<';
    public static final char NEGATION_END = '>';

}
