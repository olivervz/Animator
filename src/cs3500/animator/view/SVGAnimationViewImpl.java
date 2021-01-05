package cs3500.animator.view;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.model.Animation;
import cs3500.animator.model.AnimationFrame;
import cs3500.animator.model.AnimationModel;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents an Implementation of AnimationView that allows the user to output
 * an SVG representation of their animation.  This can be output to a file, or to 
 * standard out.
 */
public class SVGAnimationViewImpl implements AnimationView {
  private BufferedWriter out; // writer for svg
  private boolean animationComplete; // flag to determine if animation complete
  private int speed; // speed multiplier for animation
  private int[] bounds; // bounds of the animation, [x, y, width, height]

  /**
   * Construct an SVGAnimationView using a BufferedWriter output stream, and 
   * a speed multiplier.
   * 
   * @param out A BufferedWriter connected to the desired output stream
   * @param speed A speed multiplier to apply to all SVG animation
   */
  public SVGAnimationViewImpl(BufferedWriter out, int speed) {
    this.out = out;
    this.speed = speed;
    // animation is initially not finished
    this.animationComplete = false;
  }

  @Override
  public void setBounds(int[] bounds) {
    // assign the bounds of the animation
    this.bounds = bounds;
  }

  @Override
  public void update(AnimationModel model, int tick) throws IOException {

    // retrieve model bounds
    int[] bounds = model.getBounds();
    // write the heading of the svg file
    out.write("<svg width=\"" + bounds[2] + "\" height=\"" + bounds[3] + "\" version=\"1.1\""
        + " xmlns=\"http://www.w3.org/2000/svg\">\n");

    // Add shapes IN ORDER
    for (String shape: model.getShapeDrawOrder()) {

      // for each shape, retrieve the list of animations
      ArrayList<Animation> animationList = model.getAnimationMap().get(shape);
      String shapeType;

      // open shape tag
      out.write("<");

      // Currently supports svg shapes rect and ellipse
      if (animationList.get(0).getStart().getShape().getShapeName().equals("rectangle")) {
        shapeType = "rect";
        out.write(shapeType);
        out.write(" id=\"" + shape + "\"");
        // subtract bounds[0] from all x values, and bounds[1] from all y values to account for 0,0
        out.write(" x=\"" + Integer.toString(
            animationList.get(0).getStart().getShape().getPosn().getX() - bounds[0]) + "\"");
        out.write(" y=\"" + Integer.toString(
            animationList.get(0).getStart().getShape().getPosn().getY() - bounds[1]) + "\"");
        out.write(" width=\"" + animationList.get(0).getStart().getShape().getWidth() + "\"");
        out.write(" height=\"" + animationList.get(0).getStart().getShape().getHeight() + "\"");
      }
      else {
        shapeType = "ellipse";
        out.write(shapeType);
        out.write(" id=\"" + shape + "\"");
        out.write(" cx=\"" + Integer.toString(
            animationList.get(0).getStart().getShape().getPosn().getX() - bounds[0]) + "\"");
        out.write(" cy=\"" + Integer.toString(
            animationList.get(0).getStart().getShape().getPosn().getY() - bounds[1]) + "\"");
        out.write(" rx=\"" + animationList.get(0).getStart().getShape().getWidth() / 2 + "\"");
        out.write(" ry=\"" + animationList.get(0).getStart().getShape().getHeight() / 2 + "\"");
      }
      // RGB values constant between shapes
      out.write(" fill=\"rgb(" + animationList.get(0).getStart().getShape().getColor().getR());
      out.write("," + animationList.get(0).getStart().getShape().getColor().getG());
      out.write("," + animationList.get(0).getStart().getShape().getColor().getB() + ")\"");
      // currently visibility is always set to "visible"
      out.write(" visibility=\"visible\">\n");

      // for each animation, call a helper method to describe the motions
      for (Animation animation : animationList) {
        handleAnimation(animation, shapeType);
      }
      // close shape tag
      out.write("</" + shapeType + ">\n");
    }
    // close svg tag
    out.write("</svg>");

    // animation is now finished
    animationComplete = true;
  }

  /**
   * Helper method to handle writing svg animations.
   * 
   * @param animation an Animation object
   * @param shapeType a String representing the shape type
   * @throws IOException if output stream is closed
   */
  private void handleAnimation(Animation animation, String shapeType)
      throws IOException {

    // SVG animations care only about the start and end state,
    // tweening is handled internally.
    AnimationFrame end = animation.getEnd();
    AnimationFrame start = animation.getStart();

    // Determine which values are changing during the given animation
    if (end.getShape().getPosn().getX() != start.getShape().getPosn().getX()) {
      out.write("\t");
      out.write("<animate attributeType=\"xml\" begin=\"");
      // each begin, an dur value is multipled by 1000 to get from tick to ms
      // it is then divided by speed.  A speed value = 2 means 2 ticks per second,
      // or one tick per 500 ms
      out.write(Integer.toString(animation.getStart().getTick() * 1000 / speed));
      out.write("ms\" dur=\"");
      out.write(Integer.toString((animation.getEnd().getTick() -
          animation.getStart().getTick()) * 1000 / speed));
      out.write("ms\"");
      out.write(" attributeName=\"");
      if (shapeType.equals("rect")) {
        out.write("x");
      }
      else {
        out.write("cx");
      }
      out.write("\" from=\"" + Integer.toString(
          start.getShape().getPosn().getX() - bounds[0]) + "\" to=\"" +
          Integer.toString(end.getShape().getPosn().getX() - bounds[0]) + "\"");
      out.write(" fill=\"freeze\" ");
      out.write("/>\n");
    }
    if (end.getShape().getPosn().getY() != start.getShape().getPosn().getY()) {
      out.write("\t");
      out.write("<animate attributeType=\"xml\" begin=\"");
      out.write(Integer.toString(animation.getStart().getTick() * 1000 / speed));
      out.write("ms\" dur=\"");
      out.write(Integer.toString((animation.getEnd().getTick() -
          animation.getStart().getTick()) * 1000 / speed));
      out.write("ms\"");
      out.write(" attributeName=\"");
      if (shapeType.equals("rect")) {
        out.write("y");
      }
      else {
        out.write("cy");
      }
      out.write("\" from=\"" + Integer.toString(
          start.getShape().getPosn().getY() - bounds[1]) + "\" to=\"" +
          Integer.toString(end.getShape().getPosn().getY() - bounds[1]) + "\"");
      out.write(" fill=\"freeze\" ");
      out.write("/>\n");
    }
    // Red Blue and Green grouped together.
    if (end.getShape().getColor().getR() != start.getShape().getColor().getR() ||
        end.getShape().getColor().getG() != start.getShape().getColor().getG() ||
        end.getShape().getColor().getB() != start.getShape().getColor().getB()) {
      out.write("\t");
      out.write("<animate attributeType=\"xml\" begin=\"");
      out.write(Integer.toString(animation.getStart().getTick() * 1000 / speed));
      out.write("ms\" dur=\"");
      out.write(Integer.toString((animation.getEnd().getTick() -
          animation.getStart().getTick()) * 1000 / speed));
      out.write("ms\"");
      out.write(" attributeName=\"fill\" from=\"");
      out.write("rgb(" + start.getShape().getColor().getR());
      out.write("," + start.getShape().getColor().getG());
      out.write("," + start.getShape().getColor().getB());
      out.write(")\" to=\"");
      out.write("rgb(" + end.getShape().getColor().getR());
      out.write("," + end.getShape().getColor().getG());
      out.write("," + end.getShape().getColor().getB());
      out.write(")\"");
      out.write(" fill=\"freeze\" ");
      out.write("/>\n");
    }
    if (end.getShape().getWidth() != start.getShape().getWidth()) {
      out.write("\t");
      out.write("<animate attributeType=\"xml\" begin=\"");
      out.write(Integer.toString(animation.getStart().getTick() * 1000 / speed));
      out.write("ms\" dur=\"");
      out.write(Integer.toString((animation.getEnd().getTick() -
          animation.getStart().getTick()) * 1000 / speed));
      out.write("ms\"");
      out.write(" attributeName=\"");
      if (shapeType.equals("rect")) {
        out.write("width");
      }
      else {
        out.write("rx");
      }
      out.write("\" from=\"" + start.getShape().getWidth() + "\" to=\"" +
          end.getShape().getWidth() + "\"");
      out.write(" fill=\"freeze\" ");
      out.write("/>\n");
    }
    if (end.getShape().getHeight() != start.getShape().getHeight()) {
      out.write("\t");
      out.write("<animate attributeType=\"xml\" begin=\"");
      out.write(Integer.toString(animation.getStart().getTick() * 1000 / speed));
      out.write("ms\" dur=\"");
      out.write(Integer.toString((animation.getEnd().getTick() -
          animation.getStart().getTick()) * 1000 / speed));
      out.write("ms\"");
      out.write(" attributeName=\"");
      if (shapeType.equals("rect")) {
        out.write("height");
        out.write("\" from=\"" + start.getShape().getHeight() + "\" to=\"" +
            end.getShape().getWidth() + "\"");
      }
      else {
        out.write("ry");
        out.write("\" from=\"" + start.getShape().getHeight() / 2 + "\" to=\"" +
            end.getShape().getWidth() / 2 + "\"");
      }
      out.write(" fill=\"freeze\" ");
      out.write("/>\n");
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
    throw new UnsupportedOperationException("Cannot call togglePlay on an SVG view");
  }

  @Override
  public void toggleLoop() {
    throw new UnsupportedOperationException("Cannot call toggleLoop on an SVG view");
  }

  @Override
  public void restart() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Cannot call restart on an SVG view");
  }

  @Override
  public void changeSpeed(int factor) {
    throw new UnsupportedOperationException("Cannot call changeSpeed on an SVG view");
  }

  @Override
  public void addButtonFunctionality(AnimationController controller) {
    throw new UnsupportedOperationException("Cannot call addButtonFunctionality on a SVG view");
  }
}
