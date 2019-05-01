package org.bakasoft.gramat.capturing.edits;

import org.bakasoft.gramat.capturing.*;

import java.util.Stack;

public class OpenObject implements Edit {

  private final ObjectBuilder builder;
  private final Class<?> objectType;

  public OpenObject(ObjectBuilder builder, Class<?> objectType) {
    this.builder = builder;
    this.objectType = objectType;
  }

  @Override
  public void compile(Stack<ObjectModel> wrappers, Stack<Object> values) {
    ObjectModel wrapper = builder.createObjectWrapper(objectType);

    wrappers.push(wrapper);
  }

}