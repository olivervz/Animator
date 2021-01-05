package cs3500.animator.model;

/**
 * represents a single frame in the animation list that has a specific time
 * (tick) and shape.
 */
public interface AnimationFrame {

  /**
   * gives the caller access to the tick field of the animation class.
   *
   * @return the class' tick field
   */
  public int getTick();

  /**
   * gives the caller access to the shape field of the animation class.
   *
   * @return the class' shape field
   */
  public AShape getShape();

}