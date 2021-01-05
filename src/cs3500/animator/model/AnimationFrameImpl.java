package cs3500.animator.model;

import java.util.Objects;

/**
 * Implementation of a frame of a specific animation object at any given tick.
 */
public class AnimationFrameImpl implements AnimationFrame {
  AShape shape;
  int tick;

  /**
   * Construct an Animation Frame consisting of a shape at a given tick.
   * @param shape The shape representing the animation frame.
   * @param tick The tick that this shape occurs at.
   */
  public AnimationFrameImpl(AShape shape, int tick) {
    if (tick < 0) {
      throw new IllegalArgumentException("Tick must be greater than 0");
    }
    this.shape = shape;
    this.tick = tick;
  }

  @Override
  public int getTick() {
    return this.tick;
  }

  @Override
  public AShape getShape() {
    return this.shape;
  }

  @Override
  public String toString() {
    return Integer.toString(this.tick) + " " + this.shape.toString();
  }

  @Override
  public boolean equals(Object o) {
    // if it is the same object
    if (o == this) {
      return true;
    }
    // if the object is not an AnimationFrame
    if (!(o instanceof AnimationFrameImpl)) {
      return false;
    }
    // compare each field
    return this.getShape().equals(((AnimationFrameImpl) o).getShape());
  }

  @Override
  public int hashCode() {
    return Objects.hash(shape, tick);
  }
}