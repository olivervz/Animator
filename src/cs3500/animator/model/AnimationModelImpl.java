package cs3500.animator.model;

import cs3500.animator.util.AnimationBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an implementation of the interface allowing users to add animations.
 */
public final class AnimationModelImpl implements AnimationModel {

  private Map<String, ArrayList<Animation>> animationMap;
  private Map<String, AShape> shapeMap;
  private int lastTick;
  private int[] bounds;
  private Map<Integer, ArrayList<AShape>> stateMap; // contains the object's state at every tick
  private ArrayList<String> shapeDrawOrder;

  /**
   * Initialize an AnimationModel by creating two HashMaps. The first HashMap stores key value pairs
   * representing an object's name and it's corresponding list of movements throughout the
   * animation. The second HashMap stores key value pairs representing an object's name and it's
   * corresponding shape.
   */
  public AnimationModelImpl() {
    animationMap = new HashMap<String, ArrayList<Animation>>();
    shapeMap = new HashMap<String, AShape>();
    stateMap = new HashMap<Integer, ArrayList<AShape>>();
    shapeDrawOrder = new ArrayList<String>();
    lastTick = 0;
  }

  @Override
  public void createAnimationObject(String name, AShape shape) {
    // if name isn't a valid HashMap key, throw IllegalArgumentException
    if (name == null || name.equals("")) {
      throw new IllegalArgumentException("Name must be a valid string");
    }
    // If shape isn't a valid shape, throw IllegalArgumentException
    if (shape == null) {
      throw new IllegalArgumentException("Shape must not be null");
    }
    // If name already in HashMap
    if (this.animationMap.containsKey(name)) {
      throw new IllegalArgumentException("Object \"" + name + "\" already exists");
    }
    // Add a key value pair of the object's name and an empty list of motions
    animationMap.put(name, new ArrayList<Animation>());
    // Add a key value pair of the object's name and an empty shape
    shapeMap.put(name, shape);
    shapeDrawOrder.add(name);
  }

  @Override
  public void addAnimation(String name, AnimationFrame start, AnimationFrame end) {
    // start or end is null
    if (start == null || end == null) {
      throw new IllegalArgumentException("Start and end frames must not be null");
    }
    // overlaps with another animation
    if (isOverlapping(name, start.getTick()) || isOverlapping(name, end.getTick())) {
      throw new IllegalArgumentException("Start and end frames mut not overlap other animations");
    }

    // no previous animations for this object
    if (this.getFrame(name, start.getTick()) == null) {
      animationMap.get(name).add(new AnimationImpl(start, end));
    } else if (this.getFrame(name, start.getTick()).equals(start)) {
      animationMap.get(name).add(new AnimationImpl(start, end));
    } else {
      throw new IllegalArgumentException("Teleportation is not supported.");
    }
    if (end.getTick() > this.lastTick) {
      this.lastTick = end.getTick();
    }
    this.recalculateLastTick();
    this.fillStateMap(name, start, end);
  }

  /**
   * fill in each intermediate value to the state map.
   *
   * @param start starting frame
   * @param end   ending frame
   */
  private void fillStateMap(String name, AnimationFrame start, AnimationFrame end) {
    int startTick = start.getTick();
    int endTick = end.getTick();
    int newX = start.getShape().getPosn().getX();
    int newY = start.getShape().getPosn().getY();
    int newWidth = start.getShape().getWidth();
    int newHeight = start.getShape().getHeight();
    int newR = (int) start.getShape().getColor().getR();
    int newG = (int) start.getShape().getColor().getG();
    int newB = (int) start.getShape().getColor().getB();

    for (int i = startTick; i <= endTick; i++) {
      if (end.getShape().getPosn().getX() != start.getShape().getPosn().getX()) {
        newX = linearInterpolation(start.getShape().getPosn().getX(),
            end.getShape().getPosn().getX(), startTick, endTick, i);
      }
      if (end.getShape().getPosn().getY() != start.getShape().getPosn().getY()) {
        newY = linearInterpolation(start.getShape().getPosn().getY(),
            end.getShape().getPosn().getY(), startTick, endTick, i);
      }
      if (end.getShape().getColor().getR() != start.getShape().getColor().getR()) {
        newR = linearInterpolation((int) start.getShape().getColor().getR(),
            (int) end.getShape().getColor().getR(), startTick, endTick, i);
      }
      if (end.getShape().getColor().getG() != start.getShape().getColor().getG()) {
        newG = linearInterpolation((int) start.getShape().getColor().getG(),
            (int) end.getShape().getColor().getG(), startTick, endTick, i);
      }
      if (end.getShape().getColor().getB() != start.getShape().getColor().getB()) {
        newB = linearInterpolation((int) start.getShape().getColor().getB(),
            (int) end.getShape().getColor().getB(), startTick, endTick, i);
      }
      if (end.getShape().getWidth() != start.getShape().getWidth()) {
        newWidth = linearInterpolation(start.getShape().getWidth(),
            end.getShape().getWidth(), startTick, endTick, i);
      }
      if (end.getShape().getHeight() != start.getShape().getHeight()) {
        newHeight = linearInterpolation(start.getShape().getHeight(),
            end.getShape().getHeight(), startTick, endTick, i);
      }
      ArrayList<AShape> shapes = new ArrayList<AShape>();
      if (this.stateMap.containsKey(i)) {
        shapes = this.stateMap.get(i);
      }
      shapes.add(shapeMap.get(name).buildShape(new Position2D(newX, newY),
          newWidth, newHeight, new RGB(newR, newG, newB)));

      stateMap.put(i, shapes);
    }
  }

  /**
   * Apply the linear interpolation formula to calculate any intermediate value between a start and
   * end tick.
   *
   * @param start     start value
   * @param end       end value
   * @param startTick start tick
   * @param endTick   end tick
   * @param tick      desired value's tick
   * @return the integer value representing tick's intermediate value
   */
  private int linearInterpolation(int start, int end, int startTick, int endTick, int tick) {
    // for the purposes of linear interpolation, cast all values to floats for the calculation
    float fstart = start;
    float fend = end;
    float fstartTick = startTick;
    float fendTick = endTick;
    float ftick = tick;
    return (int) (fstart * ((fendTick - ftick) / (fendTick - fstartTick)) + fend * (
        (ftick - fstartTick) / (fendTick - fstartTick)));
  }

  /**
   * Returns whether the passed tick occurs within a previous or future animation.
   *
   * @param name Name of object to search
   * @param tick tick value to determine if overlap
   * @return whether the tick occurs within another animation
   */
  private boolean isOverlapping(String name, int tick) {
    // iterate through each animation
    for (int i = 0; i < animationMap.get(name).size(); i++) {
      // determine whether the passed tick occurs within a previous or future
      // animation
      if (animationMap.get(name).get(i).getStart().getTick() < tick &&
          animationMap.get(name).get(i).getEnd().getTick() > tick) {
        return true;
      }
    }
    return false;
  }

  @Override
  public List<Animation> getAnimationObject(String name) {
    // returns null if not in list
    return this.animationMap.get(name);
  }


  @Override
  public AShape getShapeMap(String name) {
    // returns null if not in list
    return this.shapeMap.get(name);
  }

  @Override
  public Map<String, ArrayList<Animation>> getAnimationMap() {
    return this.animationMap;
  }

  @Override
  public ArrayList<String> getShapeDrawOrder() {
    return this.shapeDrawOrder;
  }


  @Override
  public String show() {
    StringBuilder retStr = new StringBuilder();
    for (Map.Entry entry : this.animationMap.entrySet()) {
      //print the header
      retStr.append("shape " + entry.getKey() + " ")
          .append(this.getShapeMap(entry.getKey().toString()).getShapeName())
          .append("\n");

      ArrayList<Animation> animations = (ArrayList<Animation>) entry.getValue();
      //print each motion in the animation list to the string output
      for (Animation animation : animations) {
        retStr.append("motion ").append(entry.getKey()).append(" ")
            .append(animation.getStart().toString()).append("\t\t");
        retStr.append(animation.getEnd().toString()).append("\n");
      }
    }
    return String.valueOf(retStr);
  }

  @Override
  public AnimationFrame getFrame(String name, int tick) {
    if (this.animationMap.get(name).size() == 0) {
      return null;
    }
    for (int i = 0; i < this.animationMap.get(name).size(); i++) {
      if (this.animationMap.get(name).get(i).getEnd().getTick()
          == tick) {
        return this.animationMap.get(name).get(i).getEnd();
      }
    }
    // no matches
    return null;
  }

  @Override
  public void removeShape(String name) {
    if (!animationMap.containsKey(name)) {
      throw new IllegalArgumentException("\"" + name + "\" doesn't exist");
    }
    // remove the shape from everything
    this.shapeMap.remove(name);
    this.animationMap.remove(name);
    this.shapeDrawOrder.remove(name);
    // re-create stateMap without the removed shape
    rebuildStateMap();
  }

  @Override
  public void removeAnimation(String name, int startTick) {
    if (animationMap.containsKey(name)) {
      for (int i = 0; i < animationMap.get(name).size(); i++) {
        if (animationMap.get(name).get(i).getStart().getTick() == startTick) {
          animationMap.get(name).remove(i);
          recalculateLastTick();
          rebuildStateMap();
        }
      }
    }
  }

  // Recalculate the last tick
  private void recalculateLastTick() {
    int lastTick = 0;
    for (Map.Entry<String, ArrayList<Animation>> entry: this.animationMap.entrySet()) {
      for (Animation animation: entry.getValue()) {
        if (animation.getEnd().getTick() > lastTick) {
          lastTick = animation.getEnd().getTick();
        }
      }
    }
    this.lastTick = lastTick;
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    this.bounds = new int[]{x, y, width, height};
  }

  @Override
  public int[] getBounds() {
    return this.bounds;
  }

  @Override
  public int getLastTick() {
    return this.lastTick;
  }

  @Override
  public List<AShape> animationStateAtTick(int tick) {
    return stateMap.get(tick);
  }

  @Override
  public void deleteKeyframe(String name, int tick) {
    if (!shapeMap.containsKey(name)) {
      throw new IllegalArgumentException("\"" + name + "\"" + " does not exist");
    }
    if (tick < 0) {
      throw new IllegalArgumentException("tick can't be less than 0");
    }
    // check for intermediate animation
    ArrayList<Animation> list = this.animationMap.get(name);
    for (int i = 0; i < list.size(); i ++) {
      // found starting, see if there's anything that ends on that tick
      if (list.get(i).getStart().getTick() == tick) {
        for (int j = 0; j < list.size(); j ++) {
          // found ending, meaning its a middle keyframe
          if (list.get(j).getEnd().getTick() == tick) {
            // create a new combined animation, remove the others
            AnimationFrame start = list.get(j).getStart();
            AnimationFrame end = list.get(i).getEnd();
            removeAnimation(name, tick);
            removeAnimation(name, list.get(j).getStart().getTick());
            addAnimation(name, start, end);
            rebuildStateMap();
            recalculateLastTick();
            return;
          }
        }
      }
    }

    // check for first or last animation
    for (Animation animation: this.animationMap.get(name)) {
      if (animation.getStart().getTick() == tick) {
        removeAnimation(name, tick);
        rebuildStateMap();
        recalculateLastTick();
        return;
      }
      else if (animation.getEnd().getTick() == tick) {
        removeAnimation(name, animation.getStart().getTick());
        rebuildStateMap();
        recalculateLastTick();
        return;
      }
    }
    throw new IllegalArgumentException("No keyframe matching provided tick");
  }

  @Override
  public void addKeyframe(String name, int tick, int xPosn, int yPosn, int width, int height,
      String rgb) {
    // parse and split RGB elements
    String [] rgbArr = rgb.split(",");
    int r = Integer.parseInt(rgbArr[0]);
    int g = Integer.parseInt(rgbArr[1]);
    int b = Integer.parseInt(rgbArr[2]);

    // ensure the map contains the key
    if (!shapeMap.containsKey(name)) {
      throw new IllegalArgumentException("\"" + name + "\"" + " does not exist");
    }

    // create a keyframe
    AnimationFrame keyframe = new AnimationFrameImpl(this.shapeMap.get(name).buildShape(
        new Position2D(xPosn, yPosn), width, height,
        new RGB((double)r, (double)g, (double)b)), tick);

    // if its the first and only tick being added, make it last from tick - lastTick
    if (animationMap.get(name).size() == 0) {
      // empty list, create an animation which is just this frame for one tick
      this.addAnimation(name, keyframe, keyframe);
      return;
    }

    // if keyframe is between two animations (already been implemented)
    for (Animation animation : animationMap.get(name)) {
      // end of one animation is the keyframe, check to see if a start is too
      if (animation.getEnd().getTick() == tick) {
        for (Animation animationNext : animationMap.get(name)) {
          // an animation starts on this keyframe
          if (animationNext.getStart().getTick() == tick) {
            Animation firstHalf = new AnimationImpl(animation.getStart(),
                keyframe);
            Animation secondHalf = new AnimationImpl(keyframe,
                animationNext.getEnd());
            removeAnimation(name, animation.getStart().getTick());
            removeAnimation(name, animationNext.getStart().getTick());
            addAnimation(name, firstHalf.getStart(), firstHalf.getEnd());
            addAnimation(name, secondHalf.getStart(), secondHalf.getEnd());
            return;
          }
        }
      }
    }

    // if the keyframe is before any animation
    // find earliest Animation frame
    boolean beforeFirstAnimation = true;
    for (Animation animation: animationMap.get(name)) {
      // if an animation is found
      if (animation.getStart().getTick() < tick) {
        beforeFirstAnimation = false;
      }
    }

    // insert the animation before
    if (beforeFirstAnimation) {
      AnimationFrame earliestFrame = null;
      int earliestTick = this.getLastTick();
      // find the earliest frame and link
      ArrayList<Animation> list = animationMap.get(name);
      for (int i = 0; i < list.size(); i ++) {
        Animation animation = list.get(i);
        if (animation.getStart().getTick() <= earliestTick
            && (tick != animation.getStart().getTick())) {
          earliestTick = animation.getStart().getTick();
          earliestFrame = animation.getStart();
        }
      }
      // create new animation and add
      this.removeAnimation(name, tick);
      this.addAnimation(name, keyframe, earliestFrame);
      return;
    }

    // if the keyframe is after any animation
    // find latest Animation frame
    boolean afterLastAnimation = true;
    for (Animation animation: animationMap.get(name)) {
      // if an animation is found
      if (animation.getEnd().getTick() > tick) {
        afterLastAnimation = false;
      }
    }

    // insert the animation after
    if (afterLastAnimation) {
      AnimationFrame lastFrame = null;
      int latestTick = 0;
      // find the earliest frame and link
      ArrayList<Animation> list = animationMap.get(name);
      for (int i = 0; i < list.size(); i ++) {
        Animation animation = list.get(i);
        if (animation.getEnd().getTick() >= latestTick
            && tick != animation.getEnd().getTick()) {
          latestTick = animation.getEnd().getTick();
          lastFrame = animation.getEnd();
        }
      }
      // create new animation and add
      removeAnimation(name, latestTick);
      this.addAnimation(name, lastFrame, keyframe);
      return;
    }

    // attempt to insert the keyframe between two animations
    for (Animation animation : animationMap.get(name)) {
      if (keyframe.getTick() > animation.getStart().getTick() &&
          keyframe.getTick() < animation.getEnd().getTick()) {
        Animation firstHalf = new AnimationImpl(animation.getStart(), keyframe);
        Animation secondHalf = new AnimationImpl(keyframe, animation.getEnd());
        this.removeAnimation(name, animation.getStart().getTick());
        this.addAnimation(name, firstHalf.getStart(), firstHalf.getEnd());
        this.addAnimation(name, secondHalf.getStart(), secondHalf.getEnd());
        recalculateLastTick();
        return;
      }
    }
    throw new IllegalArgumentException("No matching tick");
  }

  private void rebuildStateMap() {
    this.stateMap = new HashMap<Integer, ArrayList<AShape>>();
    for (HashMap.Entry<String, ArrayList<Animation>> entry : animationMap.entrySet()) {
      for (Animation animation : entry.getValue()) {
        this.fillStateMap(entry.getKey(), animation.getStart(), animation.getEnd());
      }
    }
  }

  /**
   * Builder to construct AnimationModels dynamically. Still need to implement Add keyframe, this
   * would require additional functionality not required for this assignmment.
   */
  public static final class Builder implements AnimationBuilder<AnimationModel> {

    private HashMap<String, ArrayList<Animation>> animationMap;
    private HashMap<String, AShape> shapeMap;
    AnimationModel model;

    /**
     * Constructor that initializes the variables.
     */
    public Builder() {
      model = new AnimationModelImpl();
      animationMap = new HashMap<String, ArrayList<Animation>>();
      shapeMap = new HashMap<String, AShape>();
    }

    @Override
    public AnimationModel build() {
      return this.model;
    }

    @Override
    public AnimationBuilder<AnimationModel> setBounds(int x, int y, int width, int height) {
      model.setBounds(x, y, width, height);
      return this;
    }

    @Override
    public AnimationBuilder<AnimationModel> declareShape(String name, String type) {
      this.animationMap.put(name, new ArrayList<Animation>());
      if (type.equals("rectangle")) {
        this.model.createAnimationObject(name, new Rectangle());
      } else {
        this.model.createAnimationObject(name, new Oval());
      }
      return this;
    }

    @Override
    public AnimationBuilder<AnimationModel> addMotion(String name, int t1, int x1, int y1, int w1,
        int h1, int r1, int g1, int b1, int t2, int x2, int y2, int w2, int h2, int r2, int g2,
        int b2) {
      AnimationFrame start = new AnimationFrameImpl(model.getShapeMap(name).buildShape(
          new Position2D(x1, y1), w1, h1, new RGB(r1, g1, b1)), t1);
      AnimationFrame end = new AnimationFrameImpl(model.getShapeMap(name).buildShape(
          new Position2D(x2, y2), w2, h2, new RGB(r2, g2, b2)), t2);
      this.model.addAnimation(name, start, end);
      return this;
    }

    @Override
    public AnimationBuilder<AnimationModel> addKeyframe(String name, int t, int x, int y, int w,
        int h, int r, int g, int b) {
      try {
        this.model.addKeyframe(name, t, x, y, w, h, new String(r + "," + g + "," + b));
      }
      catch (IllegalArgumentException ignored) {
      }
      return this;
    }
  }
}
