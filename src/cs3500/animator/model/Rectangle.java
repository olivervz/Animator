package cs3500.animator.model;

import java.util.Objects;

/**
 * represents a rectangle shape. Inherits the posn, width, height, and color fields from its super
 * class, AShape, and calls the super class' constructors.
 */
public class Rectangle extends AShape {

  /**
   * calls the default empty constructor from AShape and passes no params.
   */
  public Rectangle() {
    super();
  }

  /**
   * calls the super constructor from AShape and passes the given params into it.
   *
   * @param posn   represents the position the new rectangle should be at
   * @param width  the width that the rectangle should be of
   * @param height the height that the rectangle should be of
   * @param color  the color (rgb values) that the rectangle should be/have
   */
  public Rectangle(Position2D posn, int width, int height, RGB color) {
    super(posn, width, height, color);
  }

  @Override
  public AShape buildShape(Position2D posn, int width, int height, RGB color) {
    return new Rectangle(posn, width, height, color);
  }

  @Override
  public String getShapeName() {
    return "rectangle";
  }

  @Override
  public boolean equals(Object o) {
    // if it is the same object
    if (o == this) {
      return true;
    }
    // if the object is not an AnimationFrame
    if (!(o instanceof Rectangle)) {
      return false;
    }

    // compare each field
    return this.getPosn().equals(((Rectangle) o).getPosn())
        && this.getWidth() == ((Rectangle) o).getWidth()
        && this.getHeight() == ((Rectangle) o).getHeight()
        && this.getColor().equals(((Rectangle) o).getColor());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getPosn(), this.getWidth(), this.getHeight(), this.getColor());
  }
}