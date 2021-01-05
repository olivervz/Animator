package cs3500.animator.model;

/**
 * represents a full movement consisting of two animation frames (start + end)
 * at different ticks.
 */
public class AnimationImpl implements Animation {

  private final AnimationFrame start; //the first frame of the motion
  private final AnimationFrame end; //the last frame of the motion

  /**
   * Creates a new animation that consists of the two frames given by the user as parameters.
   *
   * @param start the first frame of the motion
   * @param end the last frame of the motion
   */
  public AnimationImpl(AnimationFrame start, AnimationFrame end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public AnimationFrame getStart() {
    return this.start;
  }

  @Override
  public AnimationFrame getEnd() {
    return this.end;
  }
}