package cs3500.animator.model;

/**
 * represents any shape which all share the field of a position, width, height,
 * and color.
 */
public abstract class AShape {

  private final Position2D posn; //current position of the shape
  private final int width; //current width of the shape
  private final int height; //current height of the shape
  private final RGB color; //current color of the shape

  /**
   * creates a default shape meant only to be a placeholder.
   */
  public AShape() {
    this.posn = new Position2D(0, 0);
    this.width = 0;
    this.height = 0;
    this.color = new RGB(0, 0, 0);
  }

  /**
   * creates a new shape with the given parameters for each field. Width and height must be > 1.
   *
   * @param posn   current position of the shape
   * @param width  current width of the shape
   * @param height current height of the shape
   * @param color  current color of the shape
   * @throws IllegalArgumentException when any shape dimension is less than 1
   */
  public AShape(Position2D posn, int width, int height, RGB color)
      throws IllegalArgumentException {
    //confirm that neither of the shape's dimensions is less than 1
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("All shape dimensions must be greater than 0.");
    }

    //set correct fields if no exception is thrown
    this.posn = posn;
    this.width = width;
    this.height = height;
    this.color = color;
  }

  /**
   * allows the caller to see the posn field of this shape.
   *
   * @return this shape's posn field
   */
  public Position2D getPosn() {
    return this.posn;
  }

  /**
   * allows the caller to see the width field of this shape.
   *
   * @return this shape's width field
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * allows the caller to see the height field of this shape.
   *
   * @return this shape's height field
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * allows the caller to see the color field of this shape.
   *
   * @return this shape's color field
   */
  public RGB getColor() {
    return this.color;
  }

  @Override
  public String toString() {
    StringBuilder retStr = new StringBuilder();
    retStr.append(this.posn.toString()).append(" ").append(this.width)
        .append(" ").append(this.height).append(" ")
        .append(this.color.toString());
    return String.valueOf(retStr);
  }

  /**
   * creates and returns a new shape of the correct type with all fields specified by the caller.
   *
   * @param posn   current position of the shape
   * @param width  current width of the shape
   * @param height current height of the shape
   * @param color  current color of the shape
   * @return a new shape with the fields as the given parameters from this header
   */
  public abstract AShape buildShape(Position2D posn, int width, int height, RGB color);

  /**
   * gives the user the name of the shape that the method called on.
   *
   * @return this shape's name as a field
   */
  public abstract String getShapeName();

}