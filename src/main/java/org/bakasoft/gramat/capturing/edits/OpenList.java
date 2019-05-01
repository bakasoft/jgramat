package org.bakasoft.gramat.capturing.edits;

import org.bakasoft.gramat.capturing.*;

import java.util.Stack;

public class OpenList implements Edit {

  private final ObjectBuilder builder;
  private final Class<?> listType;

  public OpenList(ObjectBuilder builder, Class<?> listType) {
    this.builder = builder;
    this.listType = listType;
  }

  @Override
  public void compile(Stack<ObjectModel> wrappers, Stack<Object> values) {
    ObjectModel wrapper = builder.createListWrapper(listType);

    wrappers.push(wrapper);
  }

}