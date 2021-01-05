import static org.junit.Assert.assertEquals;

import cs3500.animator.model.AShape;
import cs3500.animator.model.Animation;
import cs3500.animator.model.AnimationFrame;
import cs3500.animator.model.AnimationFrameImpl;
import cs3500.animator.model.AnimationImpl;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.model.Oval;
import cs3500.animator.model.Position2D;
import cs3500.animator.model.RGB;
import cs3500.animator.model.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for public Animation Model methods.
 */
public class AnimationModelTest {

  private AnimationModel m;

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

  @Before
  public void init() {
    m = new AnimationModelImpl();
  }

  // Tests for createAnimationObject

  // invalid name, empty string
  @Test
  public void testCreateAnimationObjectInvalidName() {
    try {
      m.createAnimationObject("", new Rectangle());
    } catch (IllegalArgumentException iae) {
      assertEquals("Name must be a valid string", iae.getMessage());
    }
  }

  // invalid name, null object
  @Test
  public void testCreateAnimationObjectNullName() {
    try {
      m.createAnimationObject(null, new Rectangle());
    } catch (IllegalArgumentException iae) {
      assertEquals("Name must be a valid string", iae.getMessage());
    }
  }

  // invalid shape, null
  @Test
  public void testCreateAnimationObjectNullShape() {
    try {
      m.createAnimationObject("R", null);
    } catch (IllegalArgumentException iae) {
      assertEquals("Shape must not be null", iae.getMessage());
    }
  }

  // create an animation object using a pre-existing key
  @Test
  public void testCreateAnimationObjectAlreadyExists() {
    m.createAnimationObject("R", new Rectangle());
    m.createAnimationObject("C", new Oval());
    try {
      m.createAnimationObject("C", new Rectangle());
    } catch (IllegalArgumentException iae) {
      assertEquals("Object \"" + "C" + "\" already exists", iae.getMessage());
    }
  }

  // successfully create several animation objects
  @Test
  public void testCreateAnimationObject() {
    m.createAnimationObject("R", new Rectangle());
    m.createAnimationObject("C", new Oval());
    m.createAnimationObject("R1", new Oval());
    m.createAnimationObject("C1", new Rectangle());
    assertEquals("shape R rectangle\nshape C oval\nshape C1 rectangle\nshape R1 oval\n",
        m.show());
  }

  // Tests for addAnimation()

  // Invalid start frame, null
  @Test
  public void testAddAnimationNullStart() {
    m.createAnimationObject("R", new Rectangle());
    try {
      m.addAnimation("R", null,
          createFrame("R", 10, 10, 200, 200, 255.0, 0.0, 0.0, 1));
    } catch (IllegalArgumentException iae) {
      assertEquals("Start and end frames must not be null", iae.getMessage());
    }
  }

  // invalid end frame, null
  @Test
  public void testAddAnimationNullEnd() {
    m.createAnimationObject("R", new Rectangle());
    try {
      m.addAnimation("R",
          createFrame("R", 10, 10, 200, 200, 255.0, 0.0, 0.0, 1),
          null);
    } catch (IllegalArgumentException iae) {
      assertEquals("Start and end frames must not be null", iae.getMessage());
    }
  }

  // successfully add an object's first animation
  @Test
  public void testAddAnimationAddEmpty() {
    m.createAnimationObject("R", new Rectangle());
    m.addAnimation("R", createFrame("R", 100, 20, 200, 200, 255.0, 0.0, 0.0, 1),
        createFrame("R", 100, 100, 200, 200, 255.0, 0.0, 0.0, 10));
    assertEquals("shape R rectangle\nmotion R 1 100 20 200 200 255 0 0"
        + "\t\t10 100 100 200 200 255 0 0\n", m.show());
  }

  // successfully add an animation not "chained" to another
  // (its starting frame is not the same as any existing ending frame
  //  no possible teleportation)
  @Test
  public void testAddAnimationNoAdjacent() {
    m.createAnimationObject("R", new Rectangle());
    m.addAnimation("R", createFrame("R", 100, 20, 200, 200, 255.0, 0.0, 0.0, 1),
        createFrame("R", 100, 100, 200, 200, 255.0, 0.0, 0.0, 10));

    m.addAnimation("R", createFrame("R", 250, 20, 200, 200, 255.0, 255.0, 255.0, 15),
        createFrame("R", 100, 100, 200, 200, 255.0, 0.0, 0.0, 30));
    assertEquals("shape R rectangle\nmotion R 1 100 20 200 200 255 0 0"
        + "\t\t10 100 100 200 200 255 0 0\nmotion R 15 250 20 200 200 255 255 255"
        + "\t\t30 100 100 200 200 255 0 0\n", m.show());
  }

  // successfully add an adjacent animation to a list
  @Test
  public void testAddAnimationNotEmpty() {
    m.createAnimationObject("R", new Rectangle());
    m.addAnimation("R", createFrame("R", 100, 20, 200, 200, 255.0, 0.0, 0.0, 1),
        createFrame("R", 100, 100, 200, 200, 255.0, 0.0, 0.0, 10));
    m.addAnimation("R", createFrame("R", 100, 100, 200, 200, 255.0, 0.0, 0.0, 10),
        createFrame("R", 100, 100, 200, 200, 255.0, 255.0, 0.0, 20));
    assertEquals("shape R rectangle\nmotion R 1 100 20 200 200 255 0 0"
        + "\t\t10 100 100 200 200 255 0 0\nmotion R 10 100 100 200 200 255 0 0"
        + "\t\t20 100 100 200 200 255 255 0\n", m.show());
  }

  // add an animation that teleports, fail
  @Test
  public void testAddAnimationTeleport() {
    m.createAnimationObject("R", new Rectangle());
    m.addAnimation("R", createFrame("R", 100, 20, 200, 200, 255.0, 0.0, 0.0, 1),
        createFrame("R", 100, 100, 200, 200, 255.0, 0.0, 0.0, 10));
    try {
      m.addAnimation("R", createFrame("R", 101, 100, 200, 200, 255.0, 0.0, 0.0, 10),
          createFrame("R", 100, 100, 200, 200, 255.0, 255.0, 0.0, 20));
    } catch (IllegalArgumentException iae) {
      assertEquals("Teleportation is not supported.", iae.getMessage());
    }
  }

  // Tests for getAnimationObject()

  // retrieve a non-existant object
  @Test
  public void testGetAnimationObjectNull() {
    List<Animation> animations = m.getAnimationObject("helloworld");
    assertEquals(null, animations);
  }

  // retrieve an object with no animations
  @Test
  public void testGetAnimationObjectEmpty() {
    m.createAnimationObject("R", new Rectangle());
    List<Animation> animations = m.getAnimationObject("R");
    assertEquals(new ArrayList<Animation>(), animations);
  }

  // retrieve an object with a single animation
  @Test
  public void testGetAnimationObjectSingle() {
    m.createAnimationObject("R", new Rectangle());
    m.addAnimation("R", createFrame("R", 100, 20, 200, 200, 255.0, 0.0, 0.0, 1),
        createFrame("R", 100, 200, 200, 200, 255.0, 0.0, 0.0, 10));
    List<Animation> animations = m.getAnimationObject("R");
    assertEquals("1 100 20 200 200 255 0 0", animations.get(0).getStart().toString());
    assertEquals("10 100 200 200 200 255 0 0", animations.get(0).getEnd().toString());
  }

  // retrieve an object with multiple animations
  @Test
  public void testGetAnimationObjectMultiple() {
    m.createAnimationObject("R", new Rectangle());
    m.addAnimation("R", createFrame("R", 100, 20, 200, 200, 255.0, 0.0, 0.0, 1),
        createFrame("R", 100, 200, 200, 200, 255.0, 0.0, 0.0, 10));
    m.addAnimation("R", createFrame("R", 100, 200, 200, 200, 255.0, 0.0, 0.0, 10),
        createFrame("R", 200, 200, 200, 200, 255.0, 0.0, 0.0, 20));
    List<Animation> animations = m.getAnimationObject("R");
    // fix this
    assertEquals("1 100 20 200 200 255 0 0", animations.get(0).getStart().toString());
    assertEquals("10 100 200 200 200 255 0 0", animations.get(0).getEnd().toString());
    assertEquals("10 100 200 200 200 255 0 0", animations.get(1).getStart().toString());
    assertEquals("20 200 200 200 200 255 0 0", animations.get(1).getEnd().toString());
  }

  // Tests for getShapeMap()

  // attempt to retrieve a non-existant object
  @Test
  public void testGetShapeMapNull() {
    AShape shape = m.getShapeMap("helloworld");
    assertEquals(null, shape);
  }

  // retrieve a shape from shapeMap
  @Test
  public void testGetShapeMap() {
    m.createAnimationObject("R", new Rectangle());
    AShape shape = m.getShapeMap("R");
    assertEquals(new Rectangle(), shape);
  }

  // Test for show()

  // Test example from assignment
  @Test
  public void testClassExample() {
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

  // Tests for getFrame()

  // retrieve a frame from an empty map
  @Test
  public void testGetFrameEmpty() {
    m.createAnimationObject("R", new Rectangle());
    AnimationFrame frame = m.getFrame("R", 1);
    assertEquals(null, frame);
  }

  // retrieve a frame that doesn't exist in the map
  @Test
  public void testGetFrameInvalidFrame() {
    m.createAnimationObject("R", new Rectangle());
    m.addAnimation("R",
        createFrame("R", 200, 200, 50, 100, 254.8, 0.3, 0.49, 1),
        createFrame("R", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10));
    AnimationFrame frame = m.getFrame("R", 5);
    assertEquals(null, frame);
  }

  // successfully retrieve a frame from the list
  @Test
  public void testGetFrame() {
    m.createAnimationObject("R", new Rectangle());
    m.addAnimation("R",
        createFrame("R", 200, 200, 50, 100, 254.8, 0.3, 0.49, 1),
        createFrame("R", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10));
    AnimationFrame frame = m.getFrame("R", 10);
    assertEquals(createFrame("R", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10), frame);
  }

  /* for debug
  @Test
  public void testAnimationStateAtTickLarger() {
    try {
      AnimationModel m2 = AnimationReader.parseFile(new FileReader(
          "/Users/Oliver/IdeaProjects/HW06/src/buildings.txt"), 
          new AnimationModelImpl.Builder());
      for (int i = 1; i < m2.getLastTick(); i ++) {
        System.out.println("TICK: " + i);
        for (int j = 0; j < m2.animationStateAtTick(i).size(); j ++) {
          System.out.println("\t" 
          + m2.animationStateAtTick(i).get(j).getShapeName() 
          + " " + m2.animationStateAtTick(i).get(j));
        }
      }
    } catch (FileNotFoundException e) {
      fail();
    }
  }

   */

  @Test
  public void testAnimationStateAtTick() {
    m.createAnimationObject("R", new Rectangle());
    m.createAnimationObject("C", new Oval());
    //assertEquals("", m.show());
    m.addAnimation("R",
        createFrame("R", 200, 200, 50, 100, 254.8, 0.3, 0.49, 1),
        createFrame("R", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10));
    m.addAnimation("R",
        createFrame("R", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10),
        createFrame("R", 300, 300, 50, 100, 254.8, 0.3, 0.49, 50));
    m.addAnimation("C",
        createFrame("C", 440, 70, 120, 60, 0.0, 0.0, 255.0, 6),
        createFrame("C", 440, 70, 120, 60, 0.0, 0.0, 255.0, 20));
    m.addAnimation("C",
        createFrame("C", 440, 70, 120, 60, 0.0, 0.0, 255.0, 20),
        createFrame("C", 440, 250, 120, 60, 0.0, 0.0, 255.0, 50));
    m.addAnimation("C",
        createFrame("C", 440, 370, 120, 60, 0.0, 255.0, 0.0, 80),
        createFrame("C", 440, 370, 120, 60, 0.0, 255.0, 0.0, 100));
    assertEquals(null, m.animationStateAtTick(0));
    ArrayList<AShape> test = new ArrayList<AShape>();
    test.add(new Rectangle(new Position2D(200, 200), 50, 100,
        new RGB(254, 0, 0)));
    assertEquals(test, m.animationStateAtTick(1));
    test = new ArrayList<AShape>();
    test.add(new Rectangle(new Position2D(300, 300), 50, 100, new RGB(254, 0, 0)));
    test.add(new Oval(new Position2D(440, 250), 120, 60, new RGB(0, 0, 255)));
    assertEquals(test, m.animationStateAtTick(50));
    test = new ArrayList<AShape>();
    test.add(new Rectangle(new Position2D(155, 250), 50, 100, new RGB(254, 0, 0)));
    test.add(new Oval(new Position2D(440, 130), 120, 60, new RGB(0, 0, 255)));
    assertEquals(test, m.animationStateAtTick(30));
    assertEquals(100, m.getLastTick());
  }

  @Test
  public void testRemoveShape() {
    m.createAnimationObject("Test", new Rectangle());
    m.addAnimation("Test",
        createFrame("Test", 200, 200, 50, 100, 254.8, 0.3, 0.49, 1),
        createFrame("Test", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10));
    assertEquals(
        "shape Test rectangle\nmotion Test 1 200 200 50 100 255 0 0\t\t10 10 200 50 100 255 0 0\n",
        m.show());
    try {
      m.removeShape("Test2");
    }
    catch (IllegalArgumentException iae) {
      assertEquals(iae.getMessage(), "\"Test2\" doesn't exist");
    }
    assertEquals(
        "shape Test rectangle\nmotion Test 1 200 200 50 100 255 0 0\t\t10 10 200 50 100 255 0 0\n",
        m.show());
    m.removeShape("Test");
    assertEquals("", m.show());
  }

  @Test
  public void testRemoveAnimation() {
    m.createAnimationObject("Test", new Rectangle());
    m.addAnimation("Test",
        createFrame("Test", 200, 200, 50, 100, 254.8, 0.3, 0.49, 1),
        createFrame("Test", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10));
    assertEquals(
        "shape Test rectangle\nmotion Test 1 200 200 50 100 255 0 0\t\t10 10 200 50 100 255 0 0\n",
        m.show());
    m.removeAnimation("Test", 10);
    assertEquals(
        "shape Test rectangle\nmotion Test 1 200 200 50 100 255 0 0\t\t10 10 200 50 100 255 0 0\n",
        m.show());
    m.removeAnimation("Test1", 1);
    assertEquals(
        "shape Test rectangle\nmotion Test 1 200 200 50 100 255 0 0\t\t10 10 200 50 100 255 0 0\n",
        m.show());
    m.removeAnimation("Test", 1);
    assertEquals("shape Test rectangle\n", m.show());

  }

  @Test
  public void testGetAndSetBounds() {
    assertEquals(null, m.getBounds());
    m.setBounds(1, 2, 3, 4);
    assertEquals(1, m.getBounds()[0]);
    assertEquals(2, m.getBounds()[1]);
    assertEquals(3, m.getBounds()[2]);
    assertEquals(4, m.getBounds()[3]);
    m.setBounds(2, 4, 6, 8);
    assertEquals(2, m.getBounds()[0]);
    assertEquals(4, m.getBounds()[1]);
    assertEquals(6, m.getBounds()[2]);
    assertEquals(8, m.getBounds()[3]);
  }

  @Test
  public void testGetLastTick() {
    assertEquals(0, m.getLastTick());
    //creating variables for the second assert
    m.createAnimationObject("Test", new Rectangle());
    m.addAnimation("Test",
        createFrame("Test", 200, 200, 50, 100, 254.8, 0.3, 0.49, 1),
        createFrame("Test", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10));
    assertEquals(10, m.getLastTick());
    m.addAnimation("Test",
        createFrame("Test", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10),
        createFrame("Test", 10, 200, 50, 100, 254.8, 0.3, 0.49, 25));
    assertEquals(25, m.getLastTick());
    m.removeAnimation("Test", 10);
    assertEquals(10, m.getLastTick());
  }

  @Test
  public void testGetAnimationMap() {
    HashMap<String, ArrayList<Animation>> aMap = new HashMap<String, ArrayList<Animation>>();

    assertEquals(aMap, m.getAnimationMap());
    m.createAnimationObject("Test", new Rectangle());
    m.createAnimationObject("Test1", new Oval());
    m.addAnimation("Test",
        createFrame("Test", 200, 200, 50, 100, 254.8, 0.3, 0.49, 1),
        createFrame("Test", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10));
    m.addAnimation("Test1",
        createFrame("Test1", 20, 20, 5, 10, 25, 0, 0, 5),
        createFrame("Test1", 1, 20, 5, 10, 25, 0, 0, 10));
    ArrayList<Animation> list = new ArrayList<Animation>();
    list.add(new AnimationImpl(
        createFrame("Test", 200, 200, 50, 100, 254.8, 0.3, 0.49, 1),
        createFrame("Test", 10, 200, 50, 100, 254.8, 0.3, 0.49, 10)
    ));
    aMap.put("Test", list);
    list = new ArrayList<Animation>();
    list.add(new AnimationImpl(
        createFrame("Test1", 20, 20, 5, 10, 25, 0, 0, 5),
        createFrame("Test1", 1, 20, 5, 10, 25, 0, 0, 10)
    ));
    aMap.put("Test1", list);
    assertEquals(aMap.get("Test").get(0).getStart().toString(),
        m.getAnimationMap().get("Test").get(0).getStart().toString());
    assertEquals(aMap.get("Test").get(0).getEnd().toString(),
        m.getAnimationMap().get("Test").get(0).getEnd().toString());
    assertEquals(aMap.get("Test1").get(0).getStart().toString(),
        m.getAnimationMap().get("Test1").get(0).getStart().toString());
    assertEquals(aMap.get("Test1").get(0).getEnd().toString(),
        m.getAnimationMap().get("Test1").get(0).getEnd().toString());
    assertEquals(aMap.get("Test").size(), m.getAnimationMap().get("Test").size());
    assertEquals(aMap.get("Test1").size(), m.getAnimationMap().get("Test1").size());
  }

  @Test
  public void testGetShapeDrawOrder() {
    m.createAnimationObject("first", new Rectangle());
    m.createAnimationObject("second", new Rectangle());
    m.createAnimationObject("third", new Rectangle());
    m.createAnimationObject("fourth", new Rectangle());
    ArrayList<String> list = new ArrayList<String>();
    list.add("first");
    list.add("second");
    list.add("third");
    list.add("fourth");
    assertEquals(list, m.getShapeDrawOrder());
    m.removeShape("second");
    list.remove("second");
    assertEquals(list, m.getShapeDrawOrder());
  }

  @Test
  public void testDeleteKeyframe() {
    m.createAnimationObject("R", new Rectangle());
    m.addAnimation("R",
        createFrame("R", 100, 100, 100, 100,
            100.0, 100.0, 100.0, 10),
        createFrame("R", 200, 200, 200, 200,
            200.0, 200.0, 200.0, 20));
    // add an intermediate keyframe
    m.addKeyframe("R", 15, 150, 150, 150, 150, "150,150,150");
    assertEquals("shape R rectangle\n"
        + "motion R 10 100 100 100 100 100 100 100\t\t15 150 150 150 150 150 150 150\n"
        + "motion R 15 150 150 150 150 150 150 150\t\t20 200 200 200 200 200 200 200\n", m.show());
    // delete that keyframe
    m.deleteKeyframe("R", 15);
    assertEquals("shape R rectangle\n"
        + "motion R 10 100 100 100 100 100 100 100\t\t20 200 200 200 200 200 200 200\n", m.show());
    // add a keyframe before any others
    m.addKeyframe("R", 0, 1, 1, 1, 1, "1,1,1");
    assertEquals("shape R rectangle\n"
        + "motion R 10 100 100 100 100 100 100 100\t\t20 200 200 200 200 200 200 200\n"
        + "motion R 0 1 1 1 1 1 1 1\t\t10 100 100 100 100 100 100 100\n", m.show());
    // delete that keyframe
    m.deleteKeyframe("R", 0);
    assertEquals("shape R rectangle\n"
        + "motion R 10 100 100 100 100 100 100 100\t\t20 200 200 200 200 200 200 200\n", m.show());
    // add a keyframe after any others
    m.addKeyframe("R", 255, 255, 255, 255, 255, "255,255,255");
    assertEquals("shape R rectangle\n"
        + "motion R 10 100 100 100 100 100 100 100\t\t20 200 200 200 200 200 200 200\n"
        + "motion R 20 200 200 200 200 200 200 200\t\t255 255 255 255 255 255 255 255\n", m.show());
    // delete that keyframe
    m.deleteKeyframe("R", 255);
    assertEquals("shape R rectangle\n"
        + "motion R 10 100 100 100 100 100 100 100\t\t20 200 200 200 200 200 200 200\n", m.show());
  }

  @Test
  public void testAddKeyframe() {
    m.createAnimationObject("R", new Rectangle());
    m.addAnimation("R",
        createFrame("R", 100, 100, 100, 100,
            100.0, 100.0, 100.0, 10),
        createFrame("R", 200, 200, 200, 200,
            200.0, 200.0, 200.0, 20));
    // add an intermediate keyframe
    m.addKeyframe("R", 15, 150, 150, 150, 150, "150,150,150");
    assertEquals("shape R rectangle\n"
        + "motion R 10 100 100 100 100 100 100 100\t\t15 150 150 150 150 150 150 150\n"
        + "motion R 15 150 150 150 150 150 150 150\t\t20 200 200 200 200 200 200 200\n", m.show());
    // add a keyframe before any others
    m.addKeyframe("R", 0, 1, 1, 1, 1, "1,1,1");
    assertEquals("shape R rectangle\n"
        + "motion R 10 100 100 100 100 100 100 100\t\t15 150 150 150 150 150 150 150\n"
        + "motion R 15 150 150 150 150 150 150 150\t\t20 200 200 200 200 200 200 200\n"
        + "motion R 0 1 1 1 1 1 1 1\t\t10 100 100 100 100 100 100 100\n", m.show());
    // add a keyframe after any others
    m.addKeyframe("R", 255, 255, 255, 255, 255, "255,255,255");
    assertEquals("shape R rectangle\n"
        + "motion R 10 100 100 100 100 100 100 100\t\t15 150 150 150 150 150 150 150\n"
        + "motion R 15 150 150 150 150 150 150 150\t\t20 200 200 200 200 200 200 200\n"
        + "motion R 0 1 1 1 1 1 1 1\t\t10 100 100 100 100 100 100 100\n"
        + "motion R 20 200 200 200 200 200 200 200\t\t255 255 255 255 255 255 255 255\n", m.show());
    // overwrite a previous keyframe
    m.addKeyframe("R", 15, 30, 30, 30, 30, "30,30,30");
    assertEquals("shape R rectangle\n"
        + "motion R 0 1 1 1 1 1 1 1\t\t10 100 100 100 100 100 100 100\n"
        + "motion R 20 200 200 200 200 200 200 200\t\t255 255 255 255 255 255 255 255\n"
        + "motion R 10 100 100 100 100 100 100 100\t\t15 30 30 30 30 30 30 30\n"
        + "motion R 15 30 30 30 30 30 30 30\t\t20 200 200 200 200 200 200 200\n", m.show());
  }
}

