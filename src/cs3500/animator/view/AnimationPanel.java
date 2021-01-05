package cs3500.animator.view;

import cs3500.animator.model.AShape;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;

/**
 * Represents the visual canvas that shapes will be painted onto for the user
 * to see. Override JPanel.
 */
public class AnimationPanel extends JPanel {
  private List<AShape> shapes;
  private int[] bounds;

  /**
   * creates a new animation panel by calling the JPanel constructor and setting
   * the visibility to true.
   */
  public AnimationPanel() {
    super();
    // whenever the user clicks on the panel, reassign focus
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        requestFocus();
      }
    });
    // make the panel visible
    setVisible(true);
  }

  /**
   * sets the bounds field of this animation panel object.
   * @param bounds the bounds of the canvas
   */
  public void setBounds(int [] bounds) {
    this.bounds = bounds;
  }

  /**
   * sets the shapes field of this animation panel object and updates the window to include
   * the new shapes.
   * @param shapes all of the shapes that will need to be painted on the window
   */
  public void animate(List<AShape> shapes) {
    // assign the shapes field
    this.shapes = shapes;
    // repaint will call paint which will call paintComponent which we override
    repaint();
  }

  @Override
  public void paintComponent(Graphics g) {
    // cast the graphics object to a Graphics2D
    Graphics2D g2 = (Graphics2D) g;
    if (this.shapes == null) {
      // undefined shapes list
      return;
    }
    // for each shape, draw it to the screen
    for (AShape shape: shapes) {
      // get the color from shape.getColor()
      Color c = new Color((int)shape.getColor().getR(), (int)shape.getColor().getG(),
          (int)shape.getColor().getB());
      // set the paint, will be used for later calls to fillRect and fillOval
      g2.setPaint(c);
      // draw the shape
      if (shape.getShapeName().equals("rectangle")) {
        g2.fillRect(shape.getPosn().getX() - this.bounds[0],
            shape.getPosn().getY() - this.bounds[1], shape.getWidth(), shape.getHeight());
      }
      else {
        g2.fillOval(shape.getPosn().getX() - this.bounds[0],
            shape.getPosn().getY() - this.bounds[1], shape.getWidth(), shape.getHeight());
      }
    }
  }
}
