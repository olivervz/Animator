package cs3500.animator.view;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.model.AnimationModel;
import java.io.IOException;

/**
 * Representation of the three animation view styles that can be created. Contains methods
 * to ensure that the correct bounds are set for the view and the animation runs until it is over.
 */
public interface AnimationView {

  /**
   * sets the bounds field of this object to the given bounds.
   * @param bounds the bounds that should be set for the current view object.
   */
  void setBounds(int [] bounds);


  /**
   * checks that the animation should continue and increases the tick by one each time if so.
   * @param model the model that the view is currently working with
   * @param tick current tick that the animation is on
   * @throws IOException when the file is closed
   */
  void update(AnimationModel model, int tick) throws IOException;

  /**
   * Decides if the animation is finished or not.
   * @return true when the animation is over and should be stopped.
   */
  boolean isComplete();

  /**
   * Determines whether the animation is currently paused.
   * @return true if the animation is paused.
   */
  boolean isPaused();

  /**
   * Determines whether the animation is currently looping.
   * @return true if the animation is looping.
   */
  boolean isLooping();

  /**
   * Determines whether an animation should be restarted.
   * @return true if the animation should restart this call
   */
  boolean isRestarting();

  /**
   * Returns the current speed of the animation.
   * @return an integer representing the number of ticks per second
   */
  int getSpeed();

  /**
   * If animation is currently playing, pause it. Otherwise if it is
   * paused, resume it.
   *
   * @throws UnsupportedOperationException if called on a non-editor view
   */
  void togglePlay() throws UnsupportedOperationException;

  /**
   * If animation is currently set to loop, set it to stop on completion.
   * Otherwise set it to loop on completion.
   *
   * @throws UnsupportedOperationException if called on a non-editor view
   */
  void toggleLoop() throws UnsupportedOperationException;

  /**
   * Restart an animation from its first tick.
   * @throws UnsupportedOperationException if called on a non-editor view
   */
  void restart() throws UnsupportedOperationException;

  /**
   * Change the speed of the animation by a factor specified by the argument.
   * If the factor is positive, increase speed, if negative decrease speed.
   *
   * @param factor factor to increase or decrease speed by.
   * @throws UnsupportedOperationException if called on a non-editor view
   */
  void changeSpeed(int factor) throws UnsupportedOperationException;

  /**
   * Adds functionality that allows for callbacks to the controller.
   * @param controller A controller to do callbacks with.
   * @throws UnsupportedOperationException if called on a non-editor view
   */
  void addButtonFunctionality(AnimationController controller)
      throws UnsupportedOperationException;
}
