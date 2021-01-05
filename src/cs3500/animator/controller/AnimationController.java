package cs3500.animator.controller;

/**
 * Acts as a connection point between the view and the model so that information can be passed
 * and orders can be given on what to animate in the view.
 */
public interface AnimationController {

  /**
   * Starts the timer for this animation. Begin passing information
   * from the model to the view.
   */
  void start();

  /**
   * Delete the keyframe corresponding to the shape with the specified name,
   * at the specified tick.
   * @param name name of the object
   * @param tick tick the keyframe starts at
   * @throws IllegalArgumentException if name doesn't exist or tick < 1
   */
  void deleteKeyframe(String name, int tick);

  /**
   * Returns whether the name passed corresponds to an object.
   * @param name name of animation object
   * @return true if name exists
   */
  boolean isValidName(String name);

  /**
   * Update the keyframe specified by the name and tick with the specified features.
   * @param name Object name
   * @param tick tick to insert at
   * @param xPosn x posn
   * @param yPosn y posn
   * @param width width of shape
   * @param height height of shape
   * @param rgb rgb separated by commas R,G,B
   * @throws IllegalArgumentException if name doesn't exist, tick < 1, xPosn, yPosn,
   *         width, height < 0, RGB values not between 0, 255 inclusive
   */
  void updateKeyframe(String name, int tick, int xPosn, int yPosn,
      int width, int height, String rgb);

  /**
   * Adds a shape of the provided type and name to the animation.
   *
   * @param name name of shape object
   * @param type type of shape (rectangle or oval)
   * @throws IllegalArgumentException if type isn't rectangle or oval, or name exists
   */
  void addShape(String name, String type);

  /**
   * Removes the shape of the specified name from the project.
   * @param name name of shape object
   */
  void removeShape(String name);

}
