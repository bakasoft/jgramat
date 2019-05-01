package org.bakasoft.gramat.capturing.edits;

import org.bakasoft.gramat.capturing.Edit;
import org.bakasoft.gramat.capturing.ObjectModel;

import java.util.Stack;

public class PushObject implements Edit {

  @Override
  public void compile(Stack<ObjectModel> wrappers, Stack<Object> values) {
    if (wrappers.isEmpty()) {
      throw new RuntimeException();
    }

    ObjectModel wrapper = wrappers.pop();

    values.push(wrapper.getInstance());
  }

}