import static org.junit.Assert.assertEquals;

import cs3500.animator.model.AnimationFrameImpl;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.model.Oval;
import cs3500.animator.model.Position2D;
import cs3500.animator.model.RGB;
import cs3500.animator.model.Rectangle;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Textual View Implementation.  Tests the show
 * method which is directly called whenever the text view is used.
 */
public class TextViewTest {

  AnimationModelImpl m;

  @Before
  public void init() {
    m = new AnimationModelImpl();
  }

  /**
   * Helper function to create frames based on inputs.
   *
   * @param name   name
   * @param x      x position
   * @param y      y position
   * @param width  width
   * @param height height
   * @param r      red RGB
   * @param g      green RGB
   * @param b      blue RGB
   * @param tick   tick
   * @return a constructed frame
   */
  public AnimationFrameImpl createFrame(String name, int x, int y, int width, int height,
      double r, double g, double b, int tick) {
    return new AnimationFrameImpl(
        m.getShapeMap(name).buildShape(
            new Position2D(x, y), width, height, new RGB(r, g, b)), tick);
  }

  @Test
  public void testExample() {
    assertEquals("", m.show());
    m.createAnimationObject("R", new Rectangle());
    m.createAnimationObject("C", new Oval());
    //assertEquals("", m.show());
    m.addAnimation("R",
        createFrame("R", 200, 200, 50, 100, 254.8, 0.3, 0.49, 1),
        createFrame("R", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10));
    m.addAnimation("R",
        createFrame("R", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10),
        createFrame("R", 300, 300, 50, 100, 254.8, 0.3, 0.49, 50));
    m.addAnimation("R",
        createFrame("R", 300, 300, 50, 100, 254.8, 0.3, 0.49, 50),
        createFrame("R", 300, 300, 50, 100, 254.8, 0.3, 0.49, 51));
    m.addAnimation("R",
        createFrame("R", 300, 300, 50, 100, 254.8, 0.3, 0.49, 51),
        createFrame("R", 300, 300, 25, 100, 254.8, 0.3, 0.49, 70));
    m.addAnimation("R",
        createFrame("R", 300, 300, 25, 100, 254.8, 0.3, 0.49, 70),
        createFrame("R", 200, 200, 25, 100, 254.8, 0.3, 0.49, 100));
    m.addAnimation("C",
        createFrame("C", 440, 70, 120, 60, 0.0, 0.0, 255.0, 6),
        createFrame("C", 440, 70, 120, 60, 0.0, 0.0, 255.0, 20));
    m.addAnimation("C",
        createFrame("C", 440, 70, 120, 60, 0.0, 0.0, 255.0, 20),
        createFrame("C", 440, 250, 120, 60, 0.0, 0.0, 255.0, 50));
    m.addAnimation("C",
        createFrame("C", 440, 250, 120, 60, 0.0, 0.0, 255.0, 50),
        createFrame("C", 440, 370, 120, 60, 0.0, 170.0, 85.0, 70));
    m.addAnimation("C",
        createFrame("C", 440, 370, 120, 60, 0.0, 170.0, 85.0, 70),
        createFrame("C", 440, 370, 120, 60, 0.0, 255.0, 0.0, 80));
    m.addAnimation("C",
        createFrame("C", 440, 370, 120, 60, 0.0, 255.0, 0.0, 80),
        createFrame("C", 440, 370, 120, 60, 0.0, 255.0, 0.0, 100));
    assertEquals("shape R rectangle\n"
        + "motion R 1 200 200 50 100 255 0 0\t\t10 10 200 50 100 255 0 0\n"
        + "motion R 10 10 200 50 100 255 0 0\t\t50 300 300 50 100 255 0 0\n"
        + "motion R 50 300 300 50 100 255 0 0\t\t51 300 300 50 100 255 0 0\n"
        + "motion R 51 300 300 50 100 255 0 0\t\t70 300 300 25 100 255 0 0\n"
        + "motion R 70 300 300 25 100 255 0 0\t\t100 200 200 25 100 255 0 0\n"
        + "shape C oval\n"
        + "motion C 6 440 70 120 60 0 0 255\t\t20 440 70 120 60 0 0 255\n"
        + "motion C 20 440 70 120 60 0 0 255\t\t50 440 250 120 60 0 0 255\n"
        + "motion C 50 440 250 120 60 0 0 255\t\t70 440 370 120 60 0 170 85\n"
        + "motion C 70 440 370 120 60 0 170 85\t\t80 440 370 120 60 0 255 0\n"
        + "motion C 80 440 370 120 60 0 255 0\t\t100 440 370 120 60 0 255 0\n", m.show());
  }
}