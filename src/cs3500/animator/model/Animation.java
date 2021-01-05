package cs3500.animator.model;

/**
 * A interface to represent a full movement consisting of two animation frames
 * (start and end), with different ticks.
 */
public interface Animation {

  /**
   * gives the caller access to the start field of the animation class.
   *
   * @return the class' start field
   */
  AnimationFrame getStart();

  /**
   * gives the caller access to the end field of the animation class.
   *
   * @return the class' end field
   */
  AnimationFrame getEnd();
}