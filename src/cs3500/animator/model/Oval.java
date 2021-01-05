package cs3500.animator.model;

import java.util.Objects;

/**
 * represents an oval shape. Inherits the posn, width, height, and color fields from its super
 * class, AShape, and calls the super class' constructors.
 */
public class Oval extends AShape {

  /**
   * calls the default empty constructor from AShape and passes no params.
   */
  public Oval() {
    super();
  }

  /**
   * calls the super constructor from AShape and passes the given params into it.
   *
   * @param posn represents the position the new oval should be at
   * @param width the width that the oval should be of
   * @param height the height that the oval should be of
   * @param color the color (rgb values) that the rectangle should be/have
   */
  public Oval(Position2D posn, int width, int height, RGB color) {
    super(posn, width, height, color);
  }

  @Override
  public AShape buildShape(Position2D posn, int width, int height, RGB color) {
    return new Oval(posn, width, height, color);
  }

  @Override
  public String getShapeName() {
    return "oval";
  }

  @Override
    public boolean equals(Object o) {
    // if it is the same object
    if (o == this) {
      return true;
    }
    // if the object is not an AnimationFrame
    if (!(o instanceof Oval)) {
      return false;
    }
    // compare each field
    return this.getPosn().equals(((Oval) o).getPosn())
        && this.getWidth() == ((Oval) o).getWidth()
        && this.getHeight() == ((Oval) o).getHeight()
        && this.getColor().equals(((Oval) o).getColor());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getPosn(), this.getWidth(), this.getHeight(), this.getColor());
  }

}