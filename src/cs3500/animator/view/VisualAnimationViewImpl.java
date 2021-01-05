package cs3500.animator.view;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.model.AnimationModel;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * Represents the view in which the user visually sees the animation happening on the screen
 * in real time. Holds methods to ensure the animation is updated as much as it needs to be until
 * it is over and can be ended.
 */
public class VisualAnimationViewImpl extends JFrame implements AnimationView {
  private boolean animationComplete;
  private AnimationPanel panel;
  private JScrollPane scrollBars;
  private int speed;

  /**
   * Creates a new animation panel with a preferred size, adds scroll bars, and ensures the
   * panel with scrolling is visible to the user.
   */
  public VisualAnimationViewImpl(int speed) {
    // set title
    super("Excellence Animator");
    this.speed = speed;
    this.animationComplete = false;
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // set field to new AnimationPanel
    this.panel = new AnimationPanel();

    // Set initial panel size
    panel.setPreferredSize(new Dimension(1000, 1000));

    // add scrollbars
    this.scrollBars = new JScrollPane(panel);
    scrollBars.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollBars.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    // add panel to frame
    this.add(scrollBars);

    // set the frame to be visible
    this.setVisible(true);
  }


  @Override
  public void setBounds(int [] bounds) {
    // set field bounds
    this.panel.setBounds(bounds);
    // set preferred and minimum size of the JFrame
    this.setPreferredSize(new Dimension(bounds[2], bounds[3]));
    this.setMinimumSize(new Dimension(bounds[2], bounds[3]));
    this.setVisible(true);
    this.scrollBars.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.scrollBars.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
  }

  @Override
  public void update(AnimationModel model, int tick) throws IOException {
    // Draw the model's state at a specified tick
    this.panel.animate(model.animationStateAtTick(tick));
    // if last tick, set the animation to complete
    if (model.getLastTick() <= tick) {
      this.animationComplete = true;
    }
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
    return false;
  }

  @Override
  public int getSpeed() {
    return this.speed;
  }

  @Override
  public void togglePlay() {
    throw new UnsupportedOperationException("Cannot call togglePlay on a visual view");
  }

  @Override
  public void toggleLoop() {
    throw new UnsupportedOperationException("Cannot call toggleLoop on a visual view");
  }

  @Override
  public void restart() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Cannot call restart on a visual view");
  }

  @Override
  public void changeSpeed(int factor) {
    throw new UnsupportedOperationException("Cannot call changeSpeed on a visual view");
  }

  @Override
  public void addButtonFunctionality(AnimationController controller) {
    throw new UnsupportedOperationException("Cannot call addButtonFunctionality on a visual view");
  }
}
