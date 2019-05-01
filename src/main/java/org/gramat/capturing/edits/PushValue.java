package org.gramat.capturing.edits;

import org.gramat.capturing.Edit;
import org.gramat.capturing.ObjectModel;

import java.util.Stack;
import java.util.function.Function;

public class PushValue implements Edit {

  private final String text;
  private final Function<String, ?> parser;

  public PushValue(String text, Function<String, ?> parser) {
    this.parser = parser;
    this.text = text;
  }

  @Override
  public void compile(Stack<ObjectModel> wrappers, Stack<Object> values) {
    Object value;

    if (parser != null) {
      value = parser.apply(text);
    }
    else {
      value = text;
    }

    values.push(value);
  }

}