package cs3500.animator.view;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.model.Animation;
import cs3500.animator.model.AnimationModel;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * An implementation of AnimationView that produces a textual keyfram representation.  This is
 * the same implementaion as out show() method from assignment 5.  Functionality has been moved
 * into this class, however it remains in the model class as many tests rely on it.
 */
public class TextualAnimationViewImpl implements AnimationView {
  private final BufferedWriter out; // output stream
  private boolean animationComplete; // flag to determine if the animation is finished
  private int[] bounds; // bounds for animation view (not used for this implementaion)

  /**
   * Construct a TextualAnimationViewImpl by assigning the output stream and initially
   * setting the animation to incomplete.
   *
   * @param out output stream to write to.
   */
  public TextualAnimationViewImpl(BufferedWriter out) {
    this.out = out;
    this.animationComplete = false;
    this.bounds = null;
  }

  @Override
  public void setBounds(int[] bounds) {
    // assign bounds, unused
    this.bounds = bounds;
  }

  @Override
  public void update(AnimationModel model, int tick) throws IOException {

    // Same implementation from assignment 5 show() in ModelImpl
    StringBuilder textStr = new StringBuilder();
    for (Map.Entry entry : model.getAnimationMap().entrySet()) {
      //print the header
      textStr.append("shape " + entry.getKey() + " ")
          .append(model.getShapeMap(entry.getKey().toString()).getShapeName())
          .append("\n");

      ArrayList<Animation> animations = (ArrayList<Animation>) entry.getValue();
      //print each motion in the animation list to the string output
      for (Animation animation : animations) {
        textStr.append("motion ").append(entry.getKey()).append(" ")
            .append(animation.getStart().toString()).append("\t\t");
        textStr.append(animation.getEnd().toString()).append("\n");
      }
    }
    // write text to output stream
    out.write(textStr.toString());
    // Animation only requires one call to update to finish
    this.animationComplete = true;
  }

  @Override
  public boolean isComplete() {
    return this.animationComplete;
  }

  @Override
  public boolean isPaused() {
    return false;
  }

  @Override
  public boolean isLooping() {
    return false;
  }

  @Override
  public boolean isRestarting() {
    throw new UnsupportedOperationException("Cannot call restart on a Textual view.");
  }

  @Override
  public int getSpeed() {
    throw new UnsupportedOperationException("Cannot call getSpeed on a Textual view.");
  }

  @Override
  public void togglePlay() {
    throw new UnsupportedOperationException("Cannot call togglePlay on a Textual view.");
  }

  @Override
  public void toggleLoop() {
    throw new UnsupportedOperationException("Cannot call toggleLoop on a Textual view.");
  }

  @Override
  public void restart() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Cannot call restart on a Textual view.");
  }

  @Override
  public void changeSpeed(int factor) {
    throw new UnsupportedOperationException("Cannot call changeSpeed on a Textual view.");
  }

  @Override
  public void addButtonFunctionality(AnimationController controller) {
    throw new UnsupportedOperationException(
        "Cannot call addButtonFunctionality on a Textual view.");
  }

}
