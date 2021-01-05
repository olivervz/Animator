package cs3500.animator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This interface offers methods to allow for the user to create animation objects, and add
 * animations to existing objects.  It also offers methods to retrieve animations previously added.
 */
public interface AnimationModel {

  /**
   * Instantiate a new object in the animation.  This object will consist of a list of animations,
   * and a shape.
   *
   * @param name  The name by which the object will be referred to
   * @param shape The shape of the object, this assumes it remains constant
   * @throws IllegalArgumentException if shape or name is null, or if name is empty or if name
   *                                  already exists in HashMap
   */
  void createAnimationObject(String name, AShape shape) throws IllegalArgumentException;

  /**
   * Add an animation consisting of a starting frame, and an ending frame, to an existing animation
   * object.
   *
   * @param name  The name of the object to which the animation should be added.
   * @param start The frame indicating the starting position of the animation.
   * @param end   The frame indicating the ending position of the animation.
   * @throws IllegalArgumentException if start or end are null or it overlaps
   */
  void addAnimation(String name, AnimationFrame start, AnimationFrame end)
      throws IllegalArgumentException;

  /**
   * Returns a list of motions for a given animation object.
   *
   * @param name The name used to access the animations for a given object.
   * @return A list of motions for a given animation object, or null if invalid key
   */
  List<Animation> getAnimationObject(String name);

  /**
   * Return the shape corresponding to an animation object.
   *
   * @param name The name used to access the animations for a given object.
   * @return An empty object representing the shape of the animation, or null if invalid key.
   */
  AShape getShapeMap(String name);


  /**
   * Retrieves the full animationMap.
   *
   * @return The map of all animations.
   */
  Map<String, ArrayList<Animation>> getAnimationMap();

  /**
   * Retrieves a list indicating the correct draw order for the view.
   *
   * @return a list of names in correct draw order.
   */
  ArrayList<String> getShapeDrawOrder();

  /**
   * Print the motions for each shape in the model to the screen in a formatted string.
   *
   * @return A string representation of the added animations
   */
  String show();

  /**
   * Returns the frame of the object corresponding to name which matches tick.
   *
   * @param name the name corresponding to the animation object.
   * @param tick the tick being searched for.
   * @return the object's position matching the tick passed represented by an AnimationFrame or null
   *         if the object has no matches
   */
  AnimationFrame getFrame(String name, int tick);

  /**
   * Remove the shape corresponding to the name provided.
   * 
   * @param name the name of the shape to be removed
   * @throws IllegalArgumentException if name is not a shape
   */
  void removeShape(String name);

  /**
   * Remove the animation corresponding to the startFrame of the given name.
   * 
   * @param name the name of the shape which the animation is being removed
   * @param startTick the starting tick of the animation to be removed
   */
  void removeAnimation(String name, int startTick);

  /**
   * Set the bounding parameters of the animation.  This is defined as a private array
   * of integers. 
   *
   * @param x x position of 0, 0
   * @param y y position of 0, 0
   * @param width width of animation
   * @param height height of animation
   */
  void setBounds(int x, int y, int width, int height);

  /**
   * Retrieve the bounding parameters of the animation.  This is useful for the SVGView and
   * VisualView, as they need to know the bounds.  The 4 bounding parameters is returned as
   * an array of integers.
   * @return an array indicating the bounding parameters [x, y, width, height]
   */
  int[] getBounds();

  /**
   * Return the last tick of the animation, this is updated as animations are added and will
   * represent the final tick of the entire animation. This is useful for detecting when the
   * timer should stop.
   *
   * @return an integer indicating the final tick.
   */
  int getLastTick();

  /**
   * Return a list of shapes indicating every visible shape's position at the provided tick.
   * When creating the visual view, this is used for each tick to repaint() the panel each
   * tick.
   *
   * @param tick the tick to retreive positions for
   * @return a List of shapes at that tick
   */
  List<AShape> animationStateAtTick(int tick);

  /**
   * Delete the keyframe corresponding to the shape with the specified name,
   * at the specified tick.
   * @param name name of the object
   * @param tick tick the keyframe starts at
   * @throws IllegalArgumentException if name doesn't correspond to an object
   */
  void deleteKeyframe(String name, int tick) throws IllegalArgumentException;

  /**
   * Add a keyframe specified by the user's passed parameters.
   * @param name Object name
   * @param tick tick to insert at
   * @param xPosn x posn
   * @param yPosn y posn
   * @param width width of shape
   * @param height height of shape
   * @param rgb rgb separated by commas R,G,B
   * @throws IllegalArgumentException if name doesn't correspond to a shape
   */
  void addKeyframe(String name, int tick, int xPosn, int yPosn, int width, int height, String rgb)
      throws IllegalArgumentException;
}

