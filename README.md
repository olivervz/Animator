# Model Representation of an Animation:

## Changes from HW06
### Editing View
- A new Editing view was added.  This view can be selected through the -view edit flag.
- When using this view, several keystrokes can be used to control the playback.
- These keystrokes are included on the animation window, they are.
    - space : toggle pause/play
    - r     : restart the animation from the first frame
    - l     : toggle looping (when the animation finishes, should it restart, enabled by default)
    - k     : increase speed by 5 (ticks/second)
    - j     : decrease speed by 5 (ticks/second)
- The user is also able to add or delete shapes via the GUI.
    - An error message will be displayed if an attempt to add an existing shape, or delete a non-existant shape is made
    - only rectangle or oval is supported at the moment
- The user is also able to add or delete keyframes via the GUI.
    - An error message will be displayed if an attempt to add an existing keyframe, or delete a non-existant keyframe is made
- Once keyframes are added, the user is able to select from them via a dropdown menu
    - After selecting a keyframe, the user can customize its x position, y position, width, height, and RGB color (R,G,B)
    
### New Interface Methods

#### AnimationModel
- deleteKeyframe()
    - deletes the specified keyframe, throws an IllegalArgumentException if invalid tick or name
- addKeyframe()
    - add the specified keyframe, throws an IllegalArgumentException is invalid fields
- These two interface methods were necessary as my previous implementation was only able to handle animations that were represented by two frames.
- These allow for specific keyframes to be inserted before, during, or after any animations.

#### AnimationController
- Since my Editing view allows for command callbacks, any functionality I wanted to incorperate into my GUI was required to be in the controller interface.
- deleteKeyframe()
    - simply calls the corresponding method in the model
- updateKeyframe()
    - calls the model's addKeyframe() function, allows for updating already existing keyframes
- isValidName()
    - determines whether the name passed exists in the model's representation of the Animation
- addShape()
    - simply calls the corresponding method in the model
- removeShape()
    - simply calls the corresponding method in the model 
    
#### AnimationView
- Several methods needed to be included in the editing view's implementation
- Since it shared an interface with the other views that didn't need this functionality, those views will throw an UnsupportedOperationException if called

##### included for all views
- isComplete()
    - returns whether the animation is complete
- isPaused()
    - returns whether the animation is paused
- isLooping()
    - returns whether the animation is set to loop
- isRestarting()
    - returns whether the animation SHOULD restart (flag is true, animation is restarted, flag is set false)
- getSpeed()
    - returns the speed of the animation
    
##### specific to editor view
- togglePlay()
    - if paused, set to play, if playing, pause animation
- toggleLoop()
    - if set to loop, set to not loop, if set to not loop, set to loop
- restart()
    - set the restart flag, the next frame will detect this flag and reset the tick counter to 0.  Then the flag will be reset.
- changeSpeed()
    - change the speed by the specified factor
- addButtonFunctionality()
    - add the correct functionality to the JButtons included in this GUI.
    
## Running the JAR file
- When running the jar file, the path specified by the "-in" flag should be absolute, provide the full path.
- The file specified by out will be created in the folder that contains the .jar file.

## Changes from HW05
- Nothing from the model was specifically removed, however several methods were added to the interface, these are listed below.
- Several private methods were also implemented in the model.  The purpose of these were to handle tweening, which was an important
    part of the Visual view.  
    - A linear interpolation algorithm was implemented to aid in this process.
- Another consideration was the drawing order of the shapes.
    - In homework 5, no consideration was made about the order in which shapes were added.
    - Our representations mainly relied on Maps, which did not preserve this order.
    - A new datastructure was added to maintain this order


## Interface Methods
```
    createAnimationObject(String name, AShape shape) throws IllegalArgumentException
```
- Creates an object with a specific name, and shape.

```
    addAnimation(String name, AnimationFrame start, AnimationFrame end) throws IllegalArgumentException
```
- Adds an animation to an already implemented object, the ```Animation``` consists of a start and end keyframe.

```
    ArrayList<Animation> getAnimationObject(String name)
```
- Returns a list of all motions for any implemented object.

```
    AShape getShapeMap(String name)
```
- Return the ```AShape``` object corresponding to a given object's name.

```
    String show()
```
- Temporary function to represent an animation by printing it. Will eventually be replaced by a View implementation.

```
    AnimationFrame getFrame(String name, int tick)
```
- Retrieves the keyframe, at ```tick``` for the Animation Object corresponding to ```name```, return null if not found.

## New for HW06
```
  void removeShape(String name)
```
- Remove the shape corresponding to the provided name.

```
  void removeAnimation(String name, int startTick)
```
- Remove the animation corresponding to the startFrame of the given name.

```
  void setBounds(int x, int y, int width, int height)
```
- Set the bounding parameters of the animation, defined as an array of integers

```
  int[] getBounds()
```
- Retrieve the bounding parameters of the animation, defined as an array of integers

```
  int getLastTick()
```
- Retrieve the last tick of the entire animation

```
  List<AShape> animationStateAtTick(int tick)
```
- Return a list of shapes indicating every shape's position at the provided tick 

```
  Map<String, ArrayList<Animation>> getAnimationMap()
```
- Return the full animation map

```
  ArrayList<String> getShapeDrawOrder()
```
- Return a list indicating the correct draw order for shapes

## Data Structures
```
    private HashMap<String, ArrayList<Animation>> animationMap;
```
- Main data representation for the animations.  The keys are the names specified by the user, and the values are 
ArrayLists of ```Animation``` objects.

```
    private HashMap<String, AShape> shapeMap;
```
- Datastructure used to keep track of the shape of each animation object.  The keys are the names specified by the user, and the values
are shapes extending the Abstract Class ```AShape```.

```
  private int[] bounds;
```
- Array that stores the canvas bounds for the animation [xPosition, yPosition, canvasWidth, canvasHeight].

```
  private Map<Integer, ArrayList<AShape>> stateMap; // contains the object's state at every tick
```
- HashMap that stores a list of all shapes at a given tick.  Since Animations can be added non-chronologically,
  using a list was less convenient than using a HashMap with indexes as the key.

```
  private ArrayList<String> shapeDrawOrder;
```
- List of shape names in their correct draw order

## Class Structure.
### AnimationModel
- This is the main model used to represent an animation.
- It includes a HashMap that stores lists of ```Animation``` objects

### Animation
- This represents a given motion.
- It currently includes two ```AnimationFrame``` objects, which represent the keyframes at the start and end of the motion.
    - However, these fields could be replaced with a ArrayList of ```AnimationFrame``` and calculations could be
    done to find each frame between the start and end tick.
    
### AnimationFrame
- This represents any given animation object at any point during a motion.  
- It includes a field ```tick``` representing its point in the animation, and a shape indicating the position and characteristics of the object.
- This could be further expanded to include a speed or any additional necessary fields.

### AShape
- This is an Abstract class that represents any given shape.  It includes a custom X, Y position field ```Position2D```, 
and a custom Color field ```RGB```, it also includes a height and a width.
- Specific implementations for different shapes can extend this class, currently we support Rectangle and Oval.

## View implementation.
### AnimationView
- This is an interface that all views implement.
- It contains a method setBounds(int [] bounds), to set the bounds of the animation.
    - This method is only useful for the SVG and Visual implementations, as the Textual has no use for bounds
- It contains a method isComplete(), that is used to determine whether the animation is finished
    - This method could be replaced in Textual and SVG since they only need to "display" the view once since they
    print data to a stream representing keyframes.
- It contains a method update(), this passes information from the model to the view
    - In the SVG and Textual views, update is called once, and it retrieves all information from the model,
    and writes it in the correct format to the specified output stream.
    - In the Visual view, update is called model.lastTick - 1 times.  Each call is separated by a user-specified amount
    of time.  Each call to update increments the current tick, and paints the panel with the shapes obtained by animationStateAtTick(); 
    
## Controller Implementation
### AnimationController
- This is an interface that the controller implements.
    - It contains a method start() that begins passing information from the model to the view.
    - For the SVG and Textual views, start() only passes the model's information once.
    - For the Visual view, start() passes the models information many times with pauses inbetween.

### AnimationControllerImpl
- This is an implementation of  AnimationController
- The constructor accepts a model, view, and timerDelay
    - The timerDelay is only relevant to the Visual implementation of view, and it
    is the time in ms between consecutive updates to the shapes position