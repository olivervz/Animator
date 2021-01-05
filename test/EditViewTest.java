import static org.junit.Assert.assertEquals;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.controller.AnimationControllerImpl;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.AnimationView;
import cs3500.animator.view.EditingAnimationViewImpl;
import cs3500.animator.view.PlaybackListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import org.junit.Before;
import org.junit.Test;

/**
 * This is a class used for testing all components of the editor view.
 * This includes testing methods included in the view, as well as KeyListeners
 * that control the playback of the animation.
 */
public class EditViewTest {
  private AnimationView v;
  private AnimationController c;
  private PlaybackListener playbackControls;

  @Before
  public void init() throws FileNotFoundException {
    AnimationModel m;
    BufferedReader infile;
    this.v = new EditingAnimationViewImpl(20);
    infile = new BufferedReader(new FileReader(
        new File("src/simpler-example.txt")));
    m = AnimationReader.parseFile(infile, new AnimationModelImpl.Builder());
    this.c = new AnimationControllerImpl(m, v, 1000);
    this.v.setBounds(m.getBounds());
    this.playbackControls = new PlaybackListener(this.v);
  }

  @Test
  public void testIsComplete() {
    c.start();
    assertEquals(false, v.isComplete());
    // sleep until it is finished
    JButton testButton = new JButton();
    KeyEvent loop = new KeyEvent(testButton, 1, 20, 1, 10, 'l');
    playbackControls.keyTyped(loop);
    // wait for animation to finish
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertEquals(true, v.isComplete());
  }

  @Test
  public void testIsPaused() {
    c.start();
    assertEquals(false, v.isPaused());
    // sleep until it is finished
    JButton testButton = new JButton();
    KeyEvent pause = new KeyEvent(testButton, 2, 20, 1, 10, ' ');
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    playbackControls.keyTyped(pause);
    // wait for animation to finish
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertEquals(true, v.isPaused());
  }

  @Test
  public void testIsLooping() {
    c.start();
    // initially looping
    assertEquals(true, v.isLooping());
    // sleep until it is finished
    JButton testButton = new JButton();
    KeyEvent loop = new KeyEvent(testButton, 2, 20, 1, 10, 'l');
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    playbackControls.keyTyped(loop);
    // wait for animation to finish
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertEquals(false, v.isLooping());
  }

  @Test
  public void testIsRestarting() {
    c.start();
    // initially looping
    assertEquals(false, v.isRestarting());
    // sleep until it is finished
    JButton testButton = new JButton();
    KeyEvent restart = new KeyEvent(testButton, 2, 20, 1, 10, 'r');
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    playbackControls.keyTyped(restart);
    assertEquals(true, v.isRestarting());
    // wait for animation to finish
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertEquals(false, v.isRestarting());
  }

  @Test
  public void testGetSpeed() {
    assertEquals(20, v.getSpeed());
  }

  @Test
  public void testTogglePlay() {
    assertEquals(false, v.isPaused());
    v.togglePlay();
    assertEquals(true, v.isPaused());
    v.togglePlay();
    assertEquals(false, v.isPaused());
  }

  @Test
  public void testToggleLoop() {
    assertEquals(true, v.isLooping());
    v.toggleLoop();
    assertEquals(false, v.isLooping());
    v.toggleLoop();
    assertEquals(true, v.isLooping());
  }

  @Test
  public void testRestart() throws IOException {
    assertEquals(false, v.isRestarting());
    v.restart();
    assertEquals(true, v.isRestarting());
    v.update(new AnimationModelImpl(), 1);
    assertEquals(false, v.isRestarting());
  }

  @Test
  public void testChangeSpeed() {
    assertEquals(20, v.getSpeed());
    v.changeSpeed(-10);
    assertEquals(10, v.getSpeed());
    v.changeSpeed(20);
    assertEquals(30, v.getSpeed());
  }
}
