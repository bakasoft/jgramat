package org.bakasoft.gramat;

public class LocationRange {

  private final Tape tape;
  private final Location beginLocation;
  private final Location endLocation;

  public LocationRange(Tape tape, Location beginLocation, Location endLocation) {
    this.tape = tape;
    this.beginLocation = beginLocation;
    this.endLocation = endLocation;
  }

  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();

    output.append(tape);
    output.append(' ');
    output.append('@');

    if (beginLocation != null) {
      output.append(beginLocation.getLine());
      output.append(':');
      output.append(beginLocation.getColumn());
    }

    if (beginLocation != null && endLocation != null) {
      output.append(", ");
    }

    if (endLocation == null) {
      output.append('?');
    }
    else {
      output.append(endLocation.getLine());
      output.append(':');
      output.append(endLocation.getColumn());
    }

    return output.toString();
  }

  public Location getBeginLocation() {
    return beginLocation;
  }

  public Location getEndLocation() {
    return endLocation;
  }
}
