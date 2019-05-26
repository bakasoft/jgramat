package org.gramat.capturing.edits;

import org.gramat.capturing.models.ObjectModel;

import java.util.Stack;

public class PopValue implements Edit {

  private final String name;
  private final boolean appendMode;

  public PopValue(String name, boolean appendMode) {
    this.name = name;
    this.appendMode = appendMode;
  }

  @Override
  public void compile(Stack<ObjectModel> wrappers, Stack<Object> values) {
    if (values.isEmpty() || wrappers.isEmpty()) {
      throw new RuntimeException();
    }

    Object value = values.pop();
    ObjectModel wrapper = wrappers.peek();

    if (appendMode) {
      if (name != null) {
        wrapper.addValue(name, value);
      }
      else {
        wrapper.addValue(value);
      }
    }
    else {
      if (name == null) {
        throw new RuntimeException();
      }

      wrapper.setValue(name, value);
    }
  }

}