package cs3500.animator.model;

import java.util.Objects;

/**
 * represents a 2 dimensional position on the cartesian plane of the view window, made up of an x
 * and y coordinate which are the horizontal and vertical position respectively.
 */
public class Position2D {

  private final int x;
  private final int y;

  /**
   * sets the x and y field equal to the given respective parameters.
   *
   * @param x shape's horizontal positioning on view's plane
   * @param y shape's vertical positioning on view's plane
   */
  public Position2D(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * give the caller access to the object's x position field.
   *
   * @return this' x field
   */
  public int getX() {
    return this.x;
  }

  /**
   * give the caller access to the object's y position field.
   *
   * @return this' y field
   */
  public int getY() {
    return this.y;
  }

  @Override
  public String toString() {
    return this.x + " " + this.y;
  }

  @Override
  public boolean equals(Object o) {
    // if it is the same object
    if (o == this) {
      return true;
    }
    // if the object is not an AnimationFrame
    if (!(o instanceof Position2D)) {
      return false;
    }
    // compare each field
    return this.getX() == ((Position2D) o).getX()
        && this.getY() == ((Position2D) o).getY();
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}