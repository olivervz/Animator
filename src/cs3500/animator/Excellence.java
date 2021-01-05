package cs3500.animator;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.controller.AnimationControllerImpl;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.AnimationView;
import cs3500.animator.view.EditingAnimationViewImpl;
import cs3500.animator.view.SVGAnimationViewImpl;
import cs3500.animator.view.TextualAnimationViewImpl;
import cs3500.animator.view.VisualAnimationViewImpl;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 * The class that is used to let the user create a new animation with all possible options
 * adjustable by the command line through the main method. The user can set the input and output
 * file names, speed of the view, and type of the view.
 */
public final class Excellence {

  /**
   * This method acts as the entry point to the game system and uses the command line strings
   * provided by the user to create a model and view with the type and parameters specified.
   *
   * @param args is the array of strings that represent commands on what to set each field to.
   */
  public static void main(String[] args) {

    //variables for each possible argument from the command line
    String in = null; //represents the name of the input file
    BufferedReader inStream = null;
    String out = "system"; //represents the name of the output file
    BufferedWriter outStream = new BufferedWriter(new OutputStreamWriter(System.out));
    String viewType = null; //represents the type of view: text, svg, or visual
    int speed = 1; //represents the ticks per second for the view
    int timerDelay; // the timer delay, how many ms between each tick

    //switch statement loops through all strings in commandline array and keeps
    //track of what should be set for creating the new board
    for (int idx = 0; idx < args.length; idx++) {
      switch (args[idx]) {
        //sets the speed field if it is given an int value after -speed
        case "-speed":
          idx++;
          speed = Integer.parseInt(args[idx]);
          if (speed < 1) {
            JOptionPane.showMessageDialog(null,
                "The speed cannot be less than 1.",
                "Error!",
                JOptionPane.ERROR_MESSAGE);
            return;
          }
          break;
        //sets the out field if given -out
        case "-out":
          idx++;
          out = args[idx];
          try {
            outStream = new BufferedWriter(new FileWriter(out));
          } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "The output file must exist.",
                "Error!",
                JOptionPane.ERROR_MESSAGE);
            return;

          } catch (IOException e) {
            e.printStackTrace();
          }
          break;
        //sets the in field if given -in
        case "-in":
          idx++;
          in = args[idx];
          try {
            // provide full path to file for -in flag
            inStream = new BufferedReader(new FileReader(new File(in)));
          } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "The input file must exist.",
                "Error!",
                JOptionPane.ERROR_MESSAGE);
            return;
          }
          break;
        //sets the viewType field if given -view
        case "-view":
          idx++;
          viewType = args[idx];
          if (!(viewType.equals("svg")
              || viewType.equals("visual")
              || viewType.equals("edit")
              || viewType.equals("text"))) {
            JOptionPane.showMessageDialog(null,
                "View must be svg, visual, text, or edit.",
                "Error!",
                JOptionPane.ERROR_MESSAGE);
            return;
          }
          break;
        default:
          break;
      }
    }

    //shows the user an error if they did not specify a view type
    if (viewType == null) {
      JOptionPane.showMessageDialog(null,
          "The type of view must be specified.",
          "Error!",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    //shows the user an error if they did not specify and input file to read from
    if (in == null) {
      JOptionPane.showMessageDialog(null,
          "The input file must be specified.",
          "Error!",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    //creates a view variable to be used in
    AnimationView view;

    //factory method is called to set the view type depending on view case
    view = viewFactory(viewType, outStream, speed);

    AnimationModel model = AnimationReader.parseFile(inStream, new AnimationModelImpl.Builder());
    view.setBounds(model.getBounds());

    // speed is a value provided by user
    // 1 -> 1 tick per second
    // 2 -> 2 ticks per second
    // timerdelay by default is 1000 ms (1 second)
    // speed of 2, means 2 ticks per second, timerdelay 0.5 s
    // timerDelay should be 1000 / speed
    if (1000 / speed < 1) {
      // minimum timeDelay is 1ms
      timerDelay = 1;
    }
    else {
      timerDelay = 1000 / speed;
    }
    // create controller with model, view, and timerDelay
    // (only used for visual (timer), and svg(multiplier))
    AnimationController controller = new AnimationControllerImpl(model, view, timerDelay);
    // start the timer for the controller
    // for the SVG and Text view, this will send a single "pulse" to write the output 
    // for the Visual and Edit view, this will send a series of calls to update()
    // with the next set of shapes these calls will be separated by timerDelay ms
    if (!view.isComplete()) {
      controller.start();
    }

    // This is a mandatory sleep to ensure that the file isn't closed before the Text and SVG view
    // has an opportunity to write
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    // close the BufferedWriter
    try {
      outStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return;
  }

  /**
   * is called to determine what view type should be created based
   * on the passed String, then returns it.
   * 
   * @param v the type of view that should be created
   * @param out the output file to be written to
   * @param speed how many frames per tick the animation should play at
   * @return a new animation view with params specified by given input,
   *         returns null if no case is met
   */
  private static AnimationView viewFactory(String v, BufferedWriter out, int speed) {
    switch (v) {
      case "text":
        return new TextualAnimationViewImpl(out);
      case "svg":
        return new SVGAnimationViewImpl(out, speed);
      case "visual":
        return new VisualAnimationViewImpl(speed);
      case "edit":
        return new EditingAnimationViewImpl(speed);
      default:
        JOptionPane.showMessageDialog(null,
            "View type not supported.",
            "Error!",
            JOptionPane.ERROR_MESSAGE);
        return null;
    }
  }
}