package org.gramat.parsers;

// Parsing catalog
interface PCat {

  String INLINE_COMMENT_BEGIN = "//";
  String BLOCK_COMMENT_BEGIN = "/*";
  String BLOCK_COMMENT_END = "*/";
  String PREDICATE_RANGE_OPERATOR = "..";

  char VARIABLE_MARK = '%';
  char QUOTED_TOKEN_DELIMITER = '\'';
  char ALTERNATION_OPERATOR = '|';
  char END_OPERATOR = '$';
  char SYMBOL_DELIMITER = '"';
  char GROUP_BEGIN = '(';
  char GROUP_END = ')';
  char REPETITION_SEPARATOR_MARK = '/';
  char PREDICATE_DELIMITER = '`';
  char DIRECTIVE_BEGIN = '#';
  char ASSIGNMENT_OPERATOR = '=';
  char CAPTURE_BEGIN = '@';
  char CAPTURE_ARGUMENT_MARK = ':';
  char EXPRESSION_ARGUMENT_SEPARATOR = ',';
  char NEGATION_BEGIN = '<';
  char NEGATION_END = '>';
  char OPTIONAL_BEGIN = '[';
  char OPTIONAL_END = ']';

  char REPETITION_BEGIN = '{';
  char REPETITION_END = '}';
  char REPETITION_ONE_MORE_MARK = '+';
  char REPETITION_RANGE_SEPARATOR = '~';
}
