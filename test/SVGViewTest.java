import static org.junit.Assert.assertEquals;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.controller.AnimationControllerImpl;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationModelImpl;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.AnimationView;
import cs3500.animator.view.SVGAnimationViewImpl;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the SVG View Implementation.  This class includes a
 * test that outputs the SVG data to a rerouted standard out,
 * as well as a test that reroutes the SVG data directly to a file.
 */
public class SVGViewTest {

  private AnimationModel m;
  private AnimationView v;
  private AnimationController c;
  private BufferedWriter outfile;

  @Before
  public void init() throws FileNotFoundException {
    Readable infile;
    infile = new BufferedReader(new FileReader(
        new File("src/simpler-example.txt")));
    m = AnimationReader.parseFile(infile, new AnimationModelImpl.Builder());
  }

  @Test
  public void testStandardOut() throws IOException {
    File newSTDout = new File("SVGTestLog.txt");
    PrintStream replaceSTDout = new PrintStream(newSTDout);
    PrintStream resetSTDout = new PrintStream(System.out);
    System.setOut(replaceSTDout);
    outfile = new BufferedWriter(new OutputStreamWriter(System.out));
    v = new SVGAnimationViewImpl(outfile, 20);
    v.setBounds(m.getBounds());
    c = new AnimationControllerImpl(m, v, 1000);
    c.start();
    outfile.close();
    System.setOut(resetSTDout);
    Scanner sc = new Scanner(newSTDout);
    String svgOutput = "";
    while (sc.hasNextLine()) {
      svgOutput += sc.nextLine();
      svgOutput += "\n";
    }

    assertEquals(svgOutput, "<svg width=\"800\" height=\"800\" "
        + "version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n<rect id=\"R\" x=\"10\" "
        + "y=\"10\" width=\"30\" "
        + "height=\"30\" fill=\"rgb(255.0,0.0,0.0)\" visibility=\"visible\">\n\t"
        + "<animate attributeType=\"xml\" "
        + "begin=\"0ms\" dur=\"2000ms\" attributeName=\"x\" from=\"10\" to=\"50\""
        + " fill=\"freeze\" />\n\t<animate "
        + "attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" attributeName=\"y\" "
        + "from=\"10\" to=\"50\" "
        + "fill=\"freeze\" />\n\t<animate attributeType=\"xml\" begin=\"0ms\" dur=\""
        + "2000ms\" attributeName=\"fill\" "
        + "from=\"rgb(255.0,0.0,0.0)\" to=\"rgb(0.0,0.0,255.0)\" fill=\"freeze\" />\n\t"
        + "<animate attributeType=\"xml\" "
        + "begin=\"0ms\" dur=\"2000ms\" attributeName=\"width\" from=\"30\" to=\"70\" "
        + "fill=\"freeze\" />\n\t<animate attributeType=\"xml\" "
        + "begin=\"0ms\" dur=\"2000ms\" attributeName=\"height\" from=\"30\" to=\"70\" "
        + "fill=\"freeze\" />\n</rect>\n</svg>\n");
  }

  @Test
  public void testFileOut() throws IOException {
    File logfile = new File("SVGTestLog.txt");
    outfile = new BufferedWriter(new FileWriter(logfile));
    v = new SVGAnimationViewImpl(outfile, 20);
    v.setBounds(m.getBounds());
    c = new AnimationControllerImpl(m, v, 1000);
    c.start();
    outfile.close();
    Scanner sc = new Scanner(logfile);
    String svgOutput = "";
    while (sc.hasNextLine()) {
      svgOutput += sc.nextLine();
      svgOutput += "\n";
    }

    assertEquals(svgOutput, "<svg width=\"800\" height=\"800\" "
        + "version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n<rect id=\"R\" x=\"10\" "
        + "y=\"10\" width=\"30\" "
        + "height=\"30\" fill=\"rgb(255.0,0.0,0.0)\" visibility=\"visible\">\n\t"
        + "<animate attributeType=\"xml\" "
        + "begin=\"0ms\" dur=\"2000ms\" attributeName=\"x\" from=\"10\" to=\"50\""
        + " fill=\"freeze\" />\n\t<animate "
        + "attributeType=\"xml\" begin=\"0ms\" dur=\"2000ms\" attributeName=\"y\" "
        + "from=\"10\" to=\"50\" "
        + "fill=\"freeze\" />\n\t<animate attributeType=\"xml\" begin=\"0ms\" dur=\""
        + "2000ms\" attributeName=\"fill\" "
        + "from=\"rgb(255.0,0.0,0.0)\" to=\"rgb(0.0,0.0,255.0)\" fill=\"freeze\" />\n\t"
        + "<animate attributeType=\"xml\" "
        + "begin=\"0ms\" dur=\"2000ms\" attributeName=\"width\" from=\"30\" to=\"70\" "
        + "fill=\"freeze\" />\n\t<animate attributeType=\"xml\" "
        + "begin=\"0ms\" dur=\"2000ms\" attributeName=\"height\" from=\"30\" to=\"70\" "
        + "fill=\"freeze\" />\n</rect>\n</svg>\n");
  }
}