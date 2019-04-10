package org.bakasoft.gramat.inspect;

public interface Inspectable {

  void inspectWith(Inspector output);

  default String inspect() {
    Inspector inspector = new Inspector();

    inspectWith(inspector);

    return inspector.getOutput();
  }

}
