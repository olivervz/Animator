import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.controller.AnimationControllerImpl;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.AnimationView;
import cs3500.animator.view.TextualAnimationViewImpl;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the AnimationControllerImpl implementation.
 */
public class AnimationControllerImplTest {

  AnimationController c;
  AnimationModel m;
  AnimationModel m2;
  AnimationView v;
  BufferedWriter w;

  @Before
  public void init() {
    try {
      m = AnimationReader.parseFile(new BufferedReader(
              new FileReader(new File("/Users/Oliver/IdeaProjects/HW07/src/simple-example.txt"))),
          new AnimationModelImpl.Builder());
      m2 = AnimationReader.parseFile(new BufferedReader(
              new FileReader(new File("/Users/Oliver/IdeaProjects/HW07/src/simpler-example.txt"))),
          new AnimationModelImpl.Builder());

    } catch (FileNotFoundException e) {
      fail("Init failed, check file path");
    }
    try {
      w = new BufferedWriter(new FileWriter(new File("testing_output.txt")));
      v = new TextualAnimationViewImpl(w);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void nullModel() {
    try {
      c = new AnimationControllerImpl(null, v, 1);
    } catch (IllegalArgumentException e) {
      assertEquals("Model and View must be defined.", e.getMessage());
    }
  }

  @Test
  public void nullView() {
    try {
      c = new AnimationControllerImpl(m, null, 1);
    } catch (IllegalArgumentException e) {
      assertEquals("Model and View must be defined.", e.getMessage());
    }
  }

  @Test
  public void invalidTimerDelay() {
    try {
      c = new AnimationControllerImpl(m, v, 0);
    } catch (IllegalArgumentException e) {
      assertEquals("Timer Delay must be greater than 0.", e.getMessage());
    }
  }

  @Test
  public void testAnimate() {
    c = new AnimationControllerImpl(m, v, 10000);
    c.start();
    try {
      w.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      BufferedReader r = new BufferedReader(new FileReader(new File("test_output.txt")));
      assertEquals(-1, r.read());
      r.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDeleteKeyframe() {
    assertEquals("shape R rectangle\nmotion R 0 10 10 30 30 255 0 0\t\t"
        + "40 50 50 70 70 0 0 255\n", m2.show());
    c = new AnimationControllerImpl(m2, v, 1000);
    try {
      c.deleteKeyframe("X", 25);
    }
    catch (IllegalArgumentException iae) {
      assertEquals("\"X\" does not exist", iae.getMessage());
    }
    try {
      c.deleteKeyframe("R", 600);
    }
    catch (IllegalArgumentException iae) {
      assertEquals("No keyframe matching provided tick", iae.getMessage());
    }
    c.deleteKeyframe("R", 0);
    assertEquals("shape R rectangle\n", m2.show());
  }

  @Test
  public void testIsValidName() {
    c = new AnimationControllerImpl(m2, v, 1000);
    assertEquals(c.isValidName("G"), false);
    assertEquals(c.isValidName("R"), true);
  }

  @Test
  public void testUpdateKeyframe() {
    c = new AnimationControllerImpl(m2, v, 1000);
    try {
      c.updateKeyframe("X", 20, 250, 250, 250, 250, "250,250,250");
    }
    catch (IllegalArgumentException iae) {
      assertEquals("\"X\" does not exist", iae.getMessage());
    }
    c.updateKeyframe("R", 20, 250, 250, 250, 250, "250,250,250");
    assertEquals("shape R rectangle\n"
        + "motion R 0 10 10 30 30 255 0 0\t\t20 250 250 250 250 250 250 250\n"
        + "motion R 20 250 250 250 250 250 250 250\t\t40 50 50 70 70 0 0 255\n", m2.show());
    c.updateKeyframe("R", 20, 150, 150, 150, 150, "150,150,150");
  }

  @Test
  public void testAddShape() {
    c = new AnimationControllerImpl(m2, v, 1000);
    try {
      c.addShape("R", "rectangle");
    }
    catch (IllegalArgumentException iae) {
      assertEquals("Object \"R\" already exists", iae.getMessage());
    }
    try {
      c.addShape("X", "stick figure");
    }
    catch (IllegalArgumentException iae) {
      assertEquals("Type must be rectangle or oval", iae.getMessage());
    }
    c.addShape("X", "oval");
    assertEquals("shape R rectangle\n"
        + "motion R 0 10 10 30 30 255 0 0\t\t40 50 50 70 70 0 0 255\n"
        + "shape X oval\n", m2.show());
  }

  @Test
  public void testRemoveShape() {
    c = new AnimationControllerImpl(m2, v, 1000);
    try {
      m2.removeShape("G");
    }
    catch (IllegalArgumentException iae) {
      assertEquals("\"G\" doesn't exist", iae.getMessage());
    }
    m2.removeShape("R");
    assertEquals("", m2.show());
  }
}
