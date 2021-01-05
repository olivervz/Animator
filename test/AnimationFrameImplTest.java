import static org.junit.Assert.assertEquals;

import cs3500.animator.model.AShape;
import cs3500.animator.model.AnimationFrame;
import cs3500.animator.model.AnimationFrameImpl;
import cs3500.animator.model.Position2D;
import cs3500.animator.model.RGB;
import cs3500.animator.model.Rectangle;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for public AnimationFrameImpl methods. 
 */
public class AnimationFrameImplTest {
  private AnimationFrame f;
  private AShape s;

  public AnimationFrame createFrame(int x, int y, int width, int height,
      double r, double g, double b, int tick) {
    return new AnimationFrameImpl(
        new Rectangle().buildShape(new Position2D(x, y), width, height, new RGB(r, g, b)), tick);
  }

  @Before
  public void init() {
    f = createFrame(200, 200, 200, 200, 200, 200, 200, 200);
    s = new Rectangle().buildShape(new Position2D(200, 200), 200, 200, new RGB(200, 200, 200));
  }

  @Test
  public void getTickTest() {
    assertEquals(200, f.getTick());
  }

  @Test
  public void getShapeTest() {
    assertEquals(s, f.getShape());
  }

  @Test
  public void frameToStringTest() {
    assertEquals("200 200 200 200 200 200 200 200", f.toString());
  }
}