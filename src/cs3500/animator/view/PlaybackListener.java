package cs3500.animator.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Implementation of the KeyListener interface that handles all keyboard inputs to our
 * Editing View's animation controls.
 */
public class PlaybackListener implements KeyListener {
  private final AnimationView view;

  public PlaybackListener(AnimationView view) {
    this.view = view;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    switch (e.getKeyChar()) {
      case ' ':
        view.togglePlay();
        break;
      case 'l':
        view.toggleLoop();
        break;
      case 'r':
        view.restart();
        break;
      case 'k':
        view.changeSpeed(5);
        break;
      case 'j':
        view.changeSpeed(-5);
        break;
      default:
        break;
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    // Do nothing
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // Do nothing
  }
}
