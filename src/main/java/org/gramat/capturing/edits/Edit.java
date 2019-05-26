package org.gramat.capturing.edits;

import org.gramat.capturing.models.ObjectModel;

import java.util.Stack;

public interface Edit {

  void compile(Stack<ObjectModel> wrappers, Stack<Object> values);

}