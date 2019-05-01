package org.gramat.capturing.edits;

import org.gramat.capturing.Edit;
import org.gramat.capturing.ObjectModel;

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
      wrapper.addValue(name, value);
    }
    else {
      wrapper.setValue(name, value);
    }
  }

}