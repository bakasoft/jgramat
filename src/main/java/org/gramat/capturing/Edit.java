package org.gramat.capturing;

import java.util.Stack;

public interface Edit {

  void compile(Stack<ObjectModel> wrappers, Stack<Object> values);

}