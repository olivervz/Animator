package cs3500.animator.controller;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.Oval;
import cs3500.animator.model.Rectangle;
import cs3500.animator.view.AnimationView;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.Timer;

/**
 * Implementation of AnimationController.  Constructor accepts a view and a model,
 * and passes information from the model to the view.
 */
public class AnimationControllerImpl implements AnimationController {
  private final AnimationModel model; // the model
  private final AnimationView view; // the view
  private Timer animationTimer; // a timer used to pass data to the Visual view.
  private int tick = 0; // ticks start at 1
  //private final int speed; // speed initialized at start of animation
  private int currentAnimationSpeed; // current speed (may change)

  /**
   * Constructor for the controller, accepts a model, view, and timerDelay.
   * @param model model representation of animation
   * @param view view used to display animation 
   * @param timerDelay time to wait between passing each tick's information to view
   * @throws IllegalArgumentException if model or view is null or timerDelay is less than 1. 
   */
  public AnimationControllerImpl(AnimationModel model, AnimationView view, int timerDelay) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model and View must be defined.");
    }
    if (timerDelay < 1) {
      throw new IllegalArgumentException("Timer Delay must be greater than 0.");
    }
    //this.speed = 1000 / timerDelay;
    this.currentAnimationSpeed = 1000 / timerDelay;
    this.model = model;
    this.view = view;
    // only should add button functionality for the editor view.
    try {
      this.view.addButtonFunctionality(this);
    }
    catch (UnsupportedOperationException ignored) {
    }
    // create an ActionListener to call animate
    ActionListener taskPerformer = e -> {
      try {
        // purpose of the task is to call animate
        animate();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    };
    // create a timer that will call animate() each time
    this.animationTimer = new Timer(timerDelay, taskPerformer);
  }

  /**
   * Ensures that the view continues to update by tick as long as there is information be shown
   * by the view, otherwise it stops the animation.
   */
  private void animate() throws IOException {
    // if speed has been changed
    if (this.currentAnimationSpeed != view.getSpeed()) {
      // update the delay
      animationTimer.setDelay(1000 / view.getSpeed());
      // set the speed to updated
      this.currentAnimationSpeed = view.getSpeed();
    }
    // restarting
    if (view.isRestarting()) {
      tick = 1;
    }
    // first check to see if the view has completed
    if (!view.isLooping()) {
      if (view.isComplete()) {
        // stop the timer, ending the transmission of data
        // from model to view
        this.animationTimer.stop();
      } else {
        this.view.update(model, tick);
        // increment frame
        if (!this.view.isPaused()) {
          tick++;
        }
      }
    }
    // case where its looping
    else {
      // reached end, loop
      if (this.model.getLastTick() < tick) {
        tick = 0;
      }
      this.view.update(model, tick);
      // increment frame
      if (!this.view.isPaused()) {
        tick++;
      }
    }
  }

  @Override
  public void start() {
    // update once for SVG and textual
    try {
      this.view.update(model, 1);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // start the transmission from model to view
    if (!view.isComplete()) {
      this.animationTimer.start();
    }
  }

  @Override
  public void deleteKeyframe(String name, int tick) {
    this.model.deleteKeyframe(name, tick);
  }

  @Override
  public boolean isValidName(String name) {
    return this.model.getShapeDrawOrder().contains(name);
    /*
    if (this.model.getShapeDrawOrder().contains(name)) {
      return true;
    }
    else {
      return false;
    }

     */
  }

  @Override
  public void updateKeyframe(String name, int tick, int xPosn, int yPosn, int width, int height,
      String rgb) {
    this.model.addKeyframe(name, tick, xPosn, yPosn, width, height, rgb);
  }

  @Override
  public void addShape(String name, String type) {
    if (type.equals("rectangle")) {
      this.model.createAnimationObject(name, new Rectangle());
    }
    else if (type.equals("oval")) {
      this.model.createAnimationObject(name, new Oval());
    }
    else {
      throw new IllegalArgumentException("Type must be rectangle or oval");
    }
  }

  @Override
  public void removeShape(String name) {
    this.model.removeShape(name);
  }
}