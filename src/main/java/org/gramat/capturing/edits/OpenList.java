package org.gramat.capturing.edits;

import org.gramat.capturing.ObjectBuilder;
import org.gramat.capturing.models.ObjectModel;

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