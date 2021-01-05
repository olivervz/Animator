package cs3500.animator.model;

import java.util.Objects;

/**
 * represents the color of a shape using r, g, and b integer values between 0 and 225.
 */
public class RGB {

  private final double r;
  private final double g;
  private final double b;

  /**
   * constructor takes in an r g and b value and sets them equal to their respective field. integers
   * must be between 0 and 225, inclusive.
   *
   * @param r represents the red value of the color
   * @param g represents the green value of the color
   * @param b represents the blue value of the color
   */
  public RGB(double r, double g, double b) throws IllegalArgumentException {
    //confirm every rgb value is between 0 and 225
    if (r > 255 || r < 0
        || g > 255 || g < 0
        || b > 255 || b < 0) {
      throw new IllegalArgumentException(
          "RGB values must be between 0 and 255, inclusive.");
    }
    //set each param to its respective field
    this.r = r;
    this.g = g;
    this.b = b;
  }

  /**
   * gives the caller access to the r integer field.
   *
   * @return this' r field
   */
  public double getR() {
    return this.r;
  }

  /**
   * gives the caller access to the g integer field.
   *
   * @return this' g field
   */
  public double getG() {
    return this.g;
  }

  /**
   * gives the caller access to the b integer field.
   *
   * @return this' b field
   */
  public double getB() {
    return this.b;
  }

  @Override
  public String toString() {
    return (int) Math.round(this.getR()) + " " + (int) Math.round(this.getG()) + " " + (int) Math
        .round(this.getB());
  }

  @Override
  public boolean equals(Object o) {
    // if it is the same object
    if (o == this) {
      return true;
    }
    // if the object is not an AnimationFrame
    if (!(o instanceof RGB)) {
      return false;
    }
    // compare each field
    return Math.abs(this.getR() - ((RGB) o).getR()) < 0.00001
        && Math.abs(this.getG() - ((RGB) o).getG()) < 0.00001
        && Math.abs(this.getB() - ((RGB) o).getB()) < 0.00001;
  }

  @Override
  public int hashCode() {
    return Objects.hash(r, g, b);
  }
}