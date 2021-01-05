import static org.junit.Assert.assertEquals;

import cs3500.animator.model.Animation;
import cs3500.animator.model.AnimationFrame;
import cs3500.animator.model.AnimationFrameImpl;
import cs3500.animator.model.AnimationImpl;
import cs3500.animator.model.Position2D;
import cs3500.animator.model.RGB;
import cs3500.animator.model.Rectangle;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for public AnimationImpl methods. 
 */
public class AnimationImplTest {
  private AnimationFrame f1;
  private AnimationFrame f2;
  private Animation a;

  //a method which takes in all the necessary parameters for a full animation frame and creates one.
  protected AnimationFrame createFrame(int x, int y, int width, int height,
      double r, double g, double b, int tick) {
    return new AnimationFrameImpl(
        new Rectangle().buildShape(new Position2D(x, y), width, height, new RGB(r, g, b)), tick);
  }

  @Before
  public void init() {
    f1 = createFrame(200, 200, 200, 200, 200, 200, 200, 200);
    f2 = createFrame(250, 250, 250, 250, 250, 250, 250, 250);
    a = new AnimationImpl(f1, f2);
  }

  @Test
  public void getStartTest() {
    assertEquals(f1, a.getStart());
  }

  @Test
  public void getEndTest() {
    assertEquals(f2, a.getEnd());
  }
}