package cs3500.animator.view;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.model.AnimationModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Represents the view in which the user visually sees the animation happening on the screen
 * in real time. Holds methods to ensure the animation is updated as much as it needs to be until
 * it is over and can be ended.
 */
public class EditingAnimationViewImpl extends JFrame implements AnimationView {
  private boolean animationComplete;
  private boolean paused;
  private boolean looping;
  private boolean restart; // a flag set to true for only one call.
  private int speed;
  private AnimationPanel panel;
  /*
  //private PlaybackListener listener;
  private JPanel info;
  private JPanel keyframe;
  private JPanel keyframeAdd;
  private JPanel keyframeDelete;
  private JPanel keyframeHeading;
  private JPanel shapeAdd;
  private JPanel shapeDelete;
  private JPanel shapeAddDelete;
  private JPanel footer;
  private JPanel shapeAddDeleteHeading;
  private JPanel informationHeading;
  private JPanel keyframeFields;
  private JScrollPane scrollBars;
  private JLabel infoHeader;
  private JLabel instructions;
  //private JLabel keyframeHeader;
  //private JLabel shapeAddDeleteHeader;
  //private int[] bounds;
   */
  private JLabel currentValues;
  private JLabel keyframeError;
  private JLabel shapeAddDeleteError;
  private JLabel currentTick;
  private JTextField keyframeAddName;
  private JTextField keyframeAddTick;
  private JTextField keyframeAddXPosn;
  private JTextField keyframeAddYPosn;
  private JTextField keyframeAddWidth;
  private JTextField keyframeAddHeight;
  private JTextField keyframeAddRGB;
  private JTextField keyframeDeleteName;
  private JTextField keyframeDeleteTick;
  private JTextField shapeAddName;
  private JTextField shapeAddType;
  private JTextField shapeDeleteName;
  private JButton keyframeAddSubmit;
  private JButton keyframeDeleteSubmit;
  private JButton shapeAddSubmit;
  private JButton shapeDeleteSubmit;
  private JButton keyframeFieldUpdate;
  private JComboBox<String> keyframeSelector;

  private ArrayList<String> keyframeList;

  /**
   * Creates a new animation panel with a preferred size, adds scroll bars, and ensures the
   * panel with scrolling is visible to the user.
   */
  public EditingAnimationViewImpl(int speed) {
    // Set Frame Attributes
    super("Excellence Animator Editor");

    PlaybackListener listener;
    JPanel info;
    JPanel keyframe;
    JPanel keyframeAdd;
    JPanel keyframeDelete;
    JPanel keyframeHeading;
    JPanel shapeAdd;
    JPanel shapeDelete;
    JPanel shapeAddDelete;
    JPanel footer;
    JPanel shapeAddDeleteHeading;
    JPanel informationHeading;
    JPanel keyframeFields;
    JScrollPane scrollBars;
    JLabel infoHeader;
    JLabel instructions;
    JLabel keyframeHeader;
    JLabel shapeAddDeleteHeader;
    int[] bounds;

    setLayout(new BorderLayout());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(new Dimension(1400, 800));

    // set private fields
    this.animationComplete = false;
    this.paused = false;
    this.looping = true;
    this.restart = false;
    this.speed = speed;
    this.keyframeList = new ArrayList<>();

    // Assign component fields
    listener = new PlaybackListener(this);
    this.panel = new AnimationPanel();
    scrollBars = new JScrollPane(panel);
    infoHeader = new JLabel("Playback Control");
    instructions = new JLabel("Keybindings: "
        + "Restart -r     Toggle Loop -l     Toggle play -space     "
        + "Increase Speed -k      Decrease Speed -j");
    this.currentValues = new JLabel("Looping : " + this.looping +
        "                    " + "Paused : " + this.paused + "        "
        + "            Animation Complete : " + this.animationComplete +
        "                    Speed : " + this.speed);
    this.keyframeError = new JLabel("");
    shapeAddDeleteHeader = new JLabel("Add / Delete Shapes");
    this.shapeAddDeleteError = new JLabel("");
    this.currentTick = new JLabel("Current Tick : ");
    info = new JPanel(new BorderLayout());
    keyframe = new JPanel(new BorderLayout());
    keyframeHeader = new JLabel("Add / Delete Keyframe");
    keyframeAdd = new JPanel(new FlowLayout());
    keyframeDelete = new JPanel(new FlowLayout());
    keyframeHeading = new JPanel(new FlowLayout());
    shapeAdd = new JPanel(new FlowLayout());
    shapeDelete = new JPanel(new FlowLayout());
    shapeAddDelete = new JPanel(new BorderLayout());
    footer = new JPanel(new FlowLayout());
    shapeAddDeleteHeading = new JPanel(new FlowLayout());
    informationHeading = new JPanel(new FlowLayout());
    keyframeFields = new JPanel(new FlowLayout());
    this.keyframeAddSubmit = new JButton("Add");
    this.keyframeDeleteSubmit = new JButton("Delete");
    this.shapeAddSubmit = new JButton("Add");
    this.shapeDeleteSubmit = new JButton("Delete");
    this.keyframeFieldUpdate = new JButton("Update");
    this.keyframeSelector = new JComboBox(keyframeList.toArray());

    // set panel fields
    this.panel.setPreferredSize(new Dimension(1300, 800));
    this.panel.setFocusable(true);
    this.panel.setLayout(new BorderLayout());

    // add listener to frame and all components
    this.addKeyListener(listener);
    this.panel.addKeyListener(listener);

    // set scrollbar fields
    scrollBars.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollBars.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollBars.getVerticalScrollBar().setBackground(Color.DARK_GRAY);
    scrollBars.getHorizontalScrollBar().setBackground(Color.DARK_GRAY);

    // set infoHeader fields
    infoHeader.setHorizontalAlignment(SwingConstants.CENTER);
    infoHeader.setVerticalAlignment(SwingConstants.CENTER);
    infoHeader.setFont(new Font("Helvetica", Font.PLAIN, 20));
    infoHeader.setBorder(new EmptyBorder(10, 10, 0, 10));
    infoHeader.setForeground(Color.WHITE);

    // set currentTick fields
    this.currentTick.setHorizontalAlignment(SwingConstants.CENTER);
    this.currentTick.setVerticalAlignment(SwingConstants.CENTER);
    this.currentTick.setFont(new Font("Helvetica", Font.PLAIN, 20));
    this.currentTick.setBorder(new EmptyBorder(10, 10, 0, 10));
    this.currentTick.setForeground(Color.YELLOW);

    // set informationHeading fields
    informationHeading.add(infoHeader);
    informationHeading.add(currentTick);
    informationHeading.setBackground(Color.DARK_GRAY);

    // set instructions fields
    instructions.setHorizontalAlignment(SwingConstants.CENTER);
    instructions.setVerticalAlignment(SwingConstants.CENTER);
    instructions.setFont(new Font("Helvetica", Font.PLAIN, 16));
    instructions.setBorder(new EmptyBorder(10, 10, 0, 10));
    instructions.setForeground(Color.WHITE);

    // set currentValue fields
    this.currentValues.setHorizontalAlignment(SwingConstants.CENTER);
    this.currentValues.setVerticalAlignment(SwingConstants.CENTER);
    this.currentValues.setFont(new Font("Helvetica", Font.PLAIN, 16));
    this.currentValues.setBorder(new EmptyBorder(10, 10, 10, 10));
    this.currentValues.setForeground(Color.WHITE);

    // set keyframeHeader fields
    keyframeHeader.setHorizontalAlignment(SwingConstants.CENTER);
    keyframeHeader.setVerticalAlignment(SwingConstants.CENTER);
    keyframeHeader.setFont(new Font("Helvetica", Font.PLAIN, 15));
    keyframeHeader.setBorder(new EmptyBorder(10, 10, 10, 10));
    keyframeHeader.setForeground(Color.WHITE);

    // set shapeAddDelete fields
    shapeAddDeleteHeader.setHorizontalAlignment(SwingConstants.CENTER);
    shapeAddDeleteHeader.setVerticalAlignment(SwingConstants.CENTER);
    shapeAddDeleteHeader.setFont(new Font("Helvetica", Font.PLAIN, 15));
    shapeAddDeleteHeader.setBorder(new EmptyBorder(10, 10, 10, 10));
    shapeAddDeleteHeader.setForeground(Color.WHITE);

    // set top BorderLayout
    info.add(informationHeading, BorderLayout.PAGE_START);
    info.add(instructions, BorderLayout.CENTER);
    info.add(currentValues, BorderLayout.PAGE_END);
    info.setBackground(Color.DARK_GRAY);

    // set keyframeAdd textfields
    this.keyframeAddName = new JTextField("Name", 5);
    this.keyframeAddTick = new JTextField("Tick", 5);
    this.keyframeAddXPosn = new JTextField("X-Posn", 5);
    this.keyframeAddYPosn = new JTextField("Y-Posn", 5);
    this.keyframeAddWidth = new JTextField("Width", 5);
    this.keyframeAddHeight = new JTextField("Height" ,5);
    this.keyframeAddRGB = new JTextField("R,G,B", 5);

    // set keyframeDel textfields
    this.keyframeDeleteName = new JTextField("Name", 5);
    this.keyframeDeleteTick = new JTextField("Tick", 5);

    // set shapeAdd textfields
    this.shapeAddName = new JTextField("Name", 5);
    this.shapeAddType = new JTextField("Type", 5);
    shapeAdd.add(this.shapeAddName);
    shapeAdd.add(this.shapeAddType);
    shapeAdd.add(this.shapeAddSubmit);
    shapeAdd.setBackground(Color.DARK_GRAY);

    // set shapeDelete textfields
    this.shapeDeleteName = new JTextField("Name", 5);
    shapeDelete.add(this.shapeDeleteName);
    shapeDelete.add(this.shapeDeleteSubmit);
    shapeDelete.setBackground(Color.DARK_GRAY);

    // set heading
    shapeAddDeleteHeading.add(shapeAddDeleteHeader);
    shapeAddDeleteHeading.add(this.shapeAddDeleteError);
    this.shapeAddDeleteError.setForeground(Color.RED);
    this.shapeAddDeleteError.setFont(new Font("Helvetica", Font.PLAIN, 16));
    shapeAddDeleteHeading.setBackground(Color.DARK_GRAY);

    // set add/delete component
    shapeAddDelete.add(shapeAddDeleteHeading, BorderLayout.PAGE_START);
    shapeAddDelete.add(shapeAdd, BorderLayout.CENTER);
    shapeAddDelete.add(shapeDelete, BorderLayout.PAGE_END);

    // set keyframeAdd
    keyframeAdd.add(this.keyframeAddName);
    keyframeAdd.add(this.keyframeAddTick);
    keyframeAdd.add(this.keyframeAddSubmit);
    keyframeAdd.setBackground(Color.DARK_GRAY);

    // set keyframeFields
    keyframeFields.add(this.keyframeSelector);
    keyframeFields.add(this.keyframeAddXPosn);
    keyframeFields.add(this.keyframeAddYPosn);
    keyframeFields.add(this.keyframeAddWidth);
    keyframeFields.add(this.keyframeAddHeight);
    keyframeFields.add(this.keyframeAddRGB);
    keyframeFields.add(this.keyframeFieldUpdate);
    keyframeFields.setBackground(Color.DARK_GRAY);

    // set keyframeDelete
    keyframeDelete.add(this.keyframeDeleteName);
    keyframeDelete.add(this.keyframeDeleteTick);
    keyframeDelete.add(this.keyframeDeleteSubmit);
    keyframeDelete.setBackground(Color.DARK_GRAY);

    // set bottom heading FlowLayout
    keyframeHeading.setBackground(Color.DARK_GRAY);
    keyframeHeading.add(keyframeHeader);
    this.keyframeError.setForeground(Color.RED);
    this.keyframeError.setFont(new Font("Helvetica", Font.PLAIN, 16));
    keyframeHeading.add(keyframeError);

    // set bottom BorderLayout
    keyframe.add(keyframeHeading, BorderLayout.PAGE_START);
    keyframe.setBackground(Color.DARK_GRAY);
    keyframe.add(keyframeAdd, BorderLayout.CENTER);
    keyframe.add(keyframeDelete, BorderLayout.PAGE_END);

    // set footer FlowLayout
    footer.add(keyframe);
    footer.add(keyframeFields);
    footer.add(shapeAddDelete);
    footer.setBackground(Color.DARK_GRAY);

    // add components to frame
    this.add(scrollBars, BorderLayout.CENTER);
    this.add(info, BorderLayout.PAGE_START);
    this.add(footer, BorderLayout.PAGE_END);

    // set the frame to be visible
    this.setVisible(true);
    this.pack();
  }

  /**
   * Set the currentValues JLabel with the updated info.
   * @param loop looping boolean flag
   * @param pause paused boolean flag
   * @param complete animation complete boolean flag
   * @param speed speed int value
   */
  private void setCurrentValues(boolean loop, boolean pause, boolean complete, int speed) {
    this.currentValues.setText("Looping : " + loop + "                    "
        + "Paused : " + pause + "                    Animation Complete : " + complete +
        "                    Speed : " + speed);
  }

  @Override
  public void setBounds(int [] bounds) {
    // set field bounds
    this.panel.setBounds(bounds);
    // set preferred and minimum size of the JFrame
    this.setPreferredSize(new Dimension(bounds[2], bounds[3]));
    this.setMinimumSize(new Dimension(bounds[2], bounds[3]));
  }

  @Override
  public void update(AnimationModel model, int tick) throws IOException {
    this.currentTick.setText("Current Tick : " + tick);
    // Draw the model's state at a specified tick
    this.panel.animate(model.animationStateAtTick(tick));
    // if last tick, set the animation to complete
    if (model.getLastTick() <= tick) {
      this.animationComplete = true;
    }
    // check to see if restarted this update()
    if (this.restart) {
      this.restart = false;
    }
    setCurrentValues(this.looping, this.paused, this.animationComplete, this.speed);
  }

  @Override
  public boolean isComplete() {
    return this.animationComplete;
  }

  @Override
  public boolean isPaused() {
    return this.paused;
  }

  @Override
  public boolean isLooping() {
    return this.looping;
  }

  @Override
  public boolean isRestarting() {
    return this.restart;
  }

  @Override
  public int getSpeed() {
    return this.speed;
  }

  @Override
  public void togglePlay() {
    if (this.isPaused()) {
      this.paused = false;
    }
    else {
      this.paused = true;
    }
  }

  @Override
  public void toggleLoop() {
    if (this.isLooping()) {
      this.looping = false;
    }
    else {
      this.looping = true;
    }
  }

  @Override
  public void restart() throws UnsupportedOperationException {
    this.restart = true;
    this.paused = false;
  }

  @Override
  public void changeSpeed(int factor) {
    if (this.speed + factor < 1) {
      this.speed = 1;
    }
    else {
      this.speed += factor;
    }
  }

  @Override
  public void addButtonFunctionality(AnimationController controller) {

    keyframeAddSubmit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          int tick = Integer.parseInt(keyframeAddTick.getText());
          String name = keyframeAddName.getText();
          if (!controller.isValidName(name)) {
            throw new IllegalArgumentException("\"" + name + "\" is not a valid name");
          }
          keyframeError.setText("");
          if (!keyframeList.contains(name + "-" + tick)) {
            keyframeList.add(name + "-" + tick);
            keyframeSelector.addItem(name + "-" + tick);
          }
        }
        catch (NumberFormatException nfe) {
          keyframeError.setText("Invalid Input: Add");
        }
      }
    });
    keyframeDeleteSubmit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (keyframeList.contains(keyframeDeleteName.getText()
            + "-" + keyframeDeleteTick.getText())) {
          keyframeList.remove(keyframeDeleteName.getText()
              + "-" + keyframeDeleteTick.getText());
          keyframeSelector.removeItem(keyframeDeleteName.getText()
              + "-" + keyframeDeleteTick.getText());
        }
        try {
          controller.deleteKeyframe(
              keyframeDeleteName.getText(),
              Integer.parseInt(keyframeDeleteTick.getText()));
          keyframeError.setText("");
        }
        catch (NumberFormatException nfe) {
          keyframeError.setText("Invalid Input: Delete");
        }
      }
    });
    shapeAddSubmit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          controller.addShape(
              shapeAddName.getText(),
              shapeAddType.getText());
          shapeAddDeleteError.setText("");
        }
        catch (IllegalArgumentException iae) {
          shapeAddDeleteError.setText(iae.getMessage());
        }
      }
    });
    shapeDeleteSubmit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          controller.removeShape(
              shapeDeleteName.getText());
          shapeAddDeleteError.setText("");
        }
        catch (IllegalArgumentException iae) {
          shapeAddDeleteError.setText(iae.getMessage());
        }
      }
    });
    keyframeFieldUpdate.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String [] nameTick = keyframeSelector.getSelectedItem().toString().split("-");
        try {
          int xPosn = Integer.parseInt(keyframeAddXPosn.getText());
          int yPosn = Integer.parseInt(keyframeAddYPosn.getText());
          int width = Integer.parseInt(keyframeAddWidth.getText());
          int height = Integer.parseInt(keyframeAddHeight.getText());
          String rgb = keyframeAddRGB.getText();

          controller.updateKeyframe(nameTick[0], Integer.parseInt(nameTick[1]),
              xPosn, yPosn, width, height, rgb);
          keyframeError.setText("");
        }
        catch (NumberFormatException nfe) {
          keyframeError.setText("Invalid Update");
        }
        catch (IllegalArgumentException iae) {
          keyframeError.setText(iae.getMessage());
        }
        resetTextFields();
      }
    });
  }

  /**
   * Reset all keyframe fields to their label.
   */
  private void resetTextFields() {
    this.keyframeAddXPosn.setText("X-Posn");
    this.keyframeAddYPosn.setText("Y-Posn");
    this.keyframeAddWidth.setText("Width");
    this.keyframeAddHeight.setText("Height");
    this.keyframeAddRGB.setText("R,G,B");
  }
}
