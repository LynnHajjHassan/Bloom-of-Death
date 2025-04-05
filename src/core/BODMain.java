package core;

import java.awt.AWTException;

/* Copyright material for students working on assignments */

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GraphicsConfiguration;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;

import com.jogamp.nativewindow.util.Point;

import audio.SoundManager;
import effects.Lights;
import models.BloodPool;
import models.CactusPotScene;
import models.Carpet;
import models.CeilingLamp;
import models.ChairHS;
import models.ClockMain;
import models.Commons;
import models.Corpse;
import models.DoubleBass;
import models.FlatScreenTV;
import models.FloorPillow;
import models.FlowerScene;
import models.FlowerVaseScene;
import models.GarbageCan;
import models.HiddenLetter;
import models.Knife;
import models.Mirror;
import models.Pillow;
import models.Plant2PotScene;
import models.PlantPotScene;
import models.RecordPlayer;
import models.Room;
import models.SideTableHS;
import models.Sofa;
import models.TVGlitch;
import models.Table5;
import models.TableLamp;
import models.WindowMain;
import models.background;
import models.drugsObject3;
import models.frameMain;


public class BODMain extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	
	private static PickTool pickTool;	
	private Canvas3D canvas;                                // need for mouse picking
	//-------------------------------------------------------------------------------------------------------------------------
	// Needed for the user's movement
	private SimpleUniverse su;
    public static TransformGroup viewTransformGroup;
    private Transform3D viewTransform = new Transform3D();
    private double moveSpeed = 1; 							 // Speed of movement
    private double mouseSensitivity = 0.005; 			 // Sensitivity for mouse look
    private int lastMouseX, lastMouseY;						 // Store the last mouse position
    private double rotationX = 0; 							 // Rotation around the X-axis (up/down)
    private double rotationY = 0;							 // Rotation around the Y-axis (left/right)
    private double bounceTime = 0.0;						 // Tracks time for the bounce effect
    private final double bounceSpeed = 8.0; 				 // Controls the speed of the bounce
    private final double bounceAmplitude = 0.05;			 // Controls the height of the bounce
    private boolean isMoving = false; 						// Tracks if the user is moving
    public static ArrayList<Collidable> collisionObstacles = new ArrayList<>();
   
    //--------------------------------------------------------------
    // Used for the sounds 
    public  SoundManager soundManager = SoundManager.getInstance();  
    public static ClockMain   clockmain = ClockMain.getInstance();;
       
    // Used for making the game's flow 
 	private static GameState gameState = new GameState(); ;  // initializing and declaring game state
    
  // -------------------------------------------------------------------------------------------------
 	/* Objects Declarations : Declare all your object here :
 	   Format : static TransformGroup itemTG = Item.create_Item() 
 	   These models are accessible throughout the whole package Using the format : BODMain.itemTG 
 	   Example : I want to access my TableLampTg somewhere, I would use: BODMain.TablelampTG
 	*/

 	protected static TransformGroup TablelampTG = TableLamp.create_TableLamp() ;
 	protected static TransformGroup CeilingLampTG = CeilingLamp.create_CeilingLamp() ;
 	protected static TransformGroup MirrorTG = Mirror.create_Mirror();
 	protected static TransformGroup roomTG = Room.createEmptyRoom();
 	
 	static TransformGroup corpseTG = Corpse.create_Corpse() ; 
    static TransformGroup carpetTG = Carpet.create_Carpet();
    static TransformGroup sofaTG = Sofa.create_Sofa();
    static TransformGroup pillowTG = Pillow.create_Pillow();
    static TransformGroup table5TG = Table5.create_Table5();
    static TransformGroup doublebassTG = DoubleBass.create_DoubleBass();
 	
 	static TransformGroup chairCollisionTG = new TransformGroup();
 	static TransformGroup ChairTG = ChairHS.create_ChairHS();
 	static TransformGroup DrugsTG = drugsObject3.create_drugs();
 	static TransformGroup tableTG = SideTableHS.create_TableHS();
 	static TransformGroup tableCollisionTG = new TransformGroup();
 	static TransformGroup clockTG = ClockMain.create_ClockHS();
 	static TransformGroup frameTG = frameMain.create_FrameHS();
 	static TransformGroup windowTG = WindowMain.create_window();
 	static TransformGroup windowBackTG = background.createWindowBackground();
 		
 	static TransformGroup FloorPillowTG = FloorPillow.create_FloorPillow();
 	static TransformGroup TrashCanTG = GarbageCan.create_GarbageCan();
 	static TransformGroup KnifeTG = Knife.create_Knife();
 	static TransformGroup BloodPoolTG = BloodPool.create_BloodPool();
 	static TransformGroup recordTG = RecordPlayer.create_Player();
 	
 	static TransformGroup plantTG = PlantPotScene.getPlantPotTG();
 	static TransformGroup Plant2PotSceneTG = Plant2PotScene.getPlant2PotTG();
 	static BranchGroup CactusPotBG = CactusPotScene.createCactusPotBG();
 	static BranchGroup FlowerSceneBG =  FlowerScene.createFlowerBG();
 	static TransformGroup FlowerVaseSceneTG = FlowerVaseScene.getFlowerVaseTG();
    static BranchGroup FlatScreenTVBG = FlatScreenTV.createTvSceneBG();
    static TransformGroup mailSystem = HiddenLetter.createMailSystem();
    static BranchGroup mailSystemBG = new BranchGroup();
    static TransformGroup tvSystem = TVGlitch.createTVSystem();
    static BranchGroup tvSystemBG = new BranchGroup();
    
    
 	//-------------------------------------------------------------------------------------------------------------------------------    
	
    /* a function to build the content branch*/
	public static BranchGroup create_Scene() {
		
		BranchGroup sceneBG = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup();
			
	    // Add the room to the sceneTG
		roomTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		roomTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
	    sceneTG.addChild(roomTG);
	    
	    // Adding the room's collision 
	    // === Pillar 1 ===
	    TransformGroup collisionTG1 = new TransformGroup();
	    Transform3D shift1 = new Transform3D();
	    shift1.setTranslation(new Vector3f(50f, 4f, -96f)); 	// shift the center of the collision box
	    collisionTG1.setTransform(shift1);	    				//collisionTG.addChild(Room.extraWallTG); // retain visual
	    roomTG.addChild(collisionTG1);                          // add shifted visual
	    collisionObstacles.add(new Collidable(collisionTG1, 5.5f, 24.0f, 2.0f)); // correct box center    
	    // === Pillar 2 ===
	    TransformGroup collisionTG2 = new TransformGroup();
	    Transform3D shift2 = new Transform3D();
	    shift2.setTranslation(new Vector3f(8f, 4f, -96f));                         	  // shift the center of the collision box
	    collisionTG2.setTransform(shift2);
	    roomTG.addChild(collisionTG2); 										     	  // add shifted visual
	    collisionObstacles.add(new Collidable(collisionTG2, 3.5f, 24.0f, 2.0f));      // correct box center
	      
	    
	    // Adding  models  and their collision 
 	    roomTG.addChild(TablelampTG);
 	    roomTG.addChild(CeilingLampTG);
 	    roomTG.addChild(MirrorTG);
	    roomTG.addChild(corpseTG);
        collisionObstacles.add(new Collidable(corpseTG, 12.0f, 14.0f, 6.0f));
        roomTG.addChild(carpetTG);       
        roomTG.addChild(sofaTG);
        collisionObstacles.add(new Collidable(sofaTG, 8.00f, 14.0f, 16.0f));        
        roomTG.addChild(pillowTG);         
        roomTG.addChild(table5TG);
        collisionObstacles.add(new Collidable(table5TG, 6.0f, 12.0f, 12.0f));         
        roomTG.addChild(doublebassTG);
        collisionObstacles.add(new Collidable(doublebassTG, 3.0f, 12.0f, 3.0f));
            
	    Transform3D chairOffset = new Transform3D();
	    chairOffset.setTranslation(new Vector3f(-43f, -12.5f, 30.0f)); 			     // Match her objTG post
	    chairCollisionTG.setTransform(chairOffset);
	    roomTG.addChild(chairCollisionTG);
	    collisionObstacles.add(new Collidable(chairCollisionTG, 5.0f, 12.0f, 5.0f)); // Adjust dimensions to match the real chair size visually	         
        roomTG.addChild(ChairTG);
        roomTG.addChild(DrugsTG);     
	    roomTG.addChild(tableTG);	    
	    Transform3D tableOffset = new Transform3D();
	    tableOffset.setTranslation(new Vector3f(42f, -15f, 40f)); 				     // Replace with real position if different
	    tableCollisionTG.setTransform(tableOffset);
	    roomTG.addChild(tableCollisionTG);
	    collisionObstacles.add(new Collidable(tableCollisionTG, 6.0f, 12.0f, 6.0f)); // Adjust dimensions as needed               
        roomTG.addChild(clockTG);               
        roomTG.addChild(frameTG);				               
        roomTG.addChild(windowTG);               
        roomTG.addChild(windowBackTG);    	   
                
        roomTG.addChild(FloorPillowTG);
        collisionObstacles.add(new Collidable(FloorPillowTG, 3.0f, 14.0f, 3.0f));	    
        roomTG.addChild(TrashCanTG);
        collisionObstacles.add(new Collidable(TrashCanTG, 3.0f, 14.0f, 3.0f));           
        roomTG.addChild(KnifeTG);              
        roomTG.addChild(BloodPoolTG);	             
        roomTG.addChild(recordTG);	                    
        
        roomTG.addChild(plantTG);
        collisionObstacles.add(new Collidable(plantTG, 3.0f, 14.0f, 3.0f)); // adjust if needed
        roomTG.addChild(Plant2PotSceneTG);
        collisionObstacles.add(new Collidable(Plant2PotSceneTG, 3.0f, 14.0f, 3.0f));
        roomTG.addChild(CactusPotBG);
        roomTG.addChild(FlowerSceneBG);
        roomTG.addChild(FlowerVaseSceneTG);
        collisionObstacles.add(new Collidable(FlowerVaseSceneTG, 3.0f, 14.0f, 3.0f));                 
	    mailSystemBG.addChild(mailSystem);
	    roomTG.addChild(mailSystemBG);  	   
	    tvSystemBG.addChild(tvSystem);
	    roomTG.addChild(tvSystemBG);
	                   
        // Set up lighting for sceneTG 
        Lights.setupSceneEffects(sceneTG);
	    
        sceneBG.addChild(sceneTG);                            
	    
	    // make object(s) in 'sceneBG' pickable
	    pickTool = new PickTool(sceneBG);                 
	    pickTool.setMode(PickTool.GEOMETRY);    	    
 	
 	   // Play background sounds 
 	   SoundManager.getInstance().playSound("clockTicking");
 	   SoundManager.getInstance().playSound("song");

		return sceneBG;
	} 
 
	// Constructor 
	public BODMain(BranchGroup sceneBG) {
		
	    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	    canvas = new Canvas3D(config);
	    SimpleUniverse su = new SimpleUniverse(canvas); 				// Create a SimpleUniverse

	    // Adjust the clipping planes of the View
	    View view = su.getViewer().getView();
	    view.setBackClipDistance(100.0);							   // Set the far clipping plane to 100 units
	    view.setFrontClipDistance(0.1);  							   // Set the near clipping plane to 0.1 units
	    Commons.define_Viewer(su, new Point3d(0.25d, 0.25d, 90.0d));   // Set the viewer's location
	    sceneBG.compile(); 											   // Optimize the BranchGroup
	    su.addBranchGraph(sceneBG);                                    // Attach the scene to SimpleUniverse
	    setLayout(new BorderLayout());
	    add("Center", canvas);
	    frame.setSize(800, 800); 									   // Set the size of the JFrame
	    frame.setVisible(true);

	    // Part related to the user's movement in the room
	    // Get the ViewingPlatform and its TransformGroup
	    ViewingPlatform viewingPlatform = su.getViewingPlatform();
	    viewTransformGroup = viewingPlatform.getViewPlatformTransform();
	    viewTransformGroup.getTransform(viewTransform);

	    // Initialize last mouse position
	    lastMouseX = canvas.getWidth() / 2;
	    lastMouseY = canvas.getHeight() / 2;

	    // Add mouse and key listeners
	    canvas.addKeyListener(this);
	    canvas.addMouseListener(this);
	    canvas.addMouseMotionListener(this);

	    // Add focus listener to ensure the canvas retains focus
	    canvas.addFocusListener(new FocusListener() {
	        @Override
	        public void focusGained(FocusEvent e) {
	            // Do nothing
	        }

	        @Override
	        public void focusLost(FocusEvent e) {
	            // Request focus back to the canvas
	            canvas.requestFocus();
	        }
	    });

	    // Make the canvas focusable
	    canvas.setFocusable(true);
	    canvas.requestFocus();

	    // Hide the mouse cursor
	    canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

	    // Lock the mouse to the center
	    PointerInfo pointerInfo = MouseInfo.getPointerInfo();
	    Point centerPoint = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);
	    Robot robot = null;
	    try {
	        robot = new Robot();
	    } catch (AWTException e1) {
	        e1.printStackTrace();
	    }
	    robot.mouseMove(centerPoint.getX(), centerPoint.getY());
	    
	}
	
	
	public static void main(String[] args) {
		frame = new JFrame("Project Bloom of Death");                   
		frame.getContentPane().add(new BODMain(create_Scene())); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
    
	
// Mouse Listeners + Key Pressed Listeners	
//------------------------------------------------------------------------------------------	
	@Override	
	public void keyPressed(KeyEvent e) {
		
	    int keyCode = e.getKeyCode();

	    // Get the current view transformation
	    Transform3D transform = new Transform3D();
	    viewTransformGroup.getTransform(transform);

	    // Extract the current position
	    Vector3d currentPosition = new Vector3d();
	    transform.get(currentPosition);

	    // Define movement direction in local (view) space
	    Vector3d movement = new Vector3d();

	    switch (keyCode) {
	        case KeyEvent.VK_UP: 							// Move forward
	            movement.set(0, 0, -moveSpeed);
	            isMoving = true; 							// Start tracking movement
	            break;
	        case KeyEvent.VK_DOWN: 							// Move backward
	            movement.set(0, 0, moveSpeed);
	            isMoving = true; 							// Start tracking movement
	            break;
	        case KeyEvent.VK_LEFT:							 // Move left
	            movement.set(-moveSpeed, 0, 0);
	            break;
	        case KeyEvent.VK_RIGHT: 						// Move right
	            movement.set(moveSpeed, 0, 0);
	            break;
	        case KeyEvent.VK_PAGE_UP: 						// Move up (optional, jump)
	            movement.set(0, moveSpeed, 0);
	            break;
	        case KeyEvent.VK_PAGE_DOWN:						 // Move down, but prevent going too low
	            movement.set(0, -moveSpeed, 0);
	            break;
	        case KeyEvent.VK_R: 							// Reset position and orientation
	            resetCamera();
	            return;
	    }

	    // Apply rotation to movement direction
	    Matrix3d rotationMatrix = new Matrix3d();
	    transform.get(rotationMatrix);
	    rotationMatrix.transform(movement);

	   // Calculate next position and check for collisions
        Point3f nextPos = new Point3f(
            (float)(currentPosition.x + movement.x),
            (float)(currentPosition.y + movement.y),
            (float)(currentPosition.z + movement.z)
        );

        if (!isCollidingWithObstacles(nextPos, 3.0f)) {  // Adjust radius if needed
            currentPosition.add(movement);  			// Move normally
        }

	    
	    // Restrict movement: Prevent the user from going outside the room
	    double minY = 0.2; 							// Minimum walking height (adjust as needed)  Commented those to make noor's debugging works, we will remove the comments later on 
	    double maxY = 0.2;						    // Maximum walking height (adjust as needed)
	    double maxX = Room.roomWidth - 5;
	    double minX = - (Room.roomWidth - 5 ) ; 
	    double minZ = - ( Room.roomLength - 5 ) ;
	    double maxZ = Room.roomLength - 5 ;
	    
	    
	    if (currentPosition.y < minY) {
	        currentPosition.y = minY; 			// Keep the user above the ground
	    }
	    if (currentPosition.y > maxY) {
	        currentPosition.y = maxY; 		    // Keep the user under the ceiling
	    }
	    if (currentPosition.x >= maxX) {
	        currentPosition.x = maxX;
	    }
	    if (currentPosition.x <= minX) {
	        currentPosition.x = minX;
	    }
	    if (currentPosition.z >= maxZ) {
	        currentPosition.z = maxZ;
	    }
	    if (currentPosition.z <= minZ) {
	        currentPosition.z = minZ;
	    }
	   
	    
	    // Apply bouncy effect if the user is moving forward or backward
	    if (isMoving && (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN)) {
	        bounceTime += 0.1; // Increment time for the bounce effect
	        double bounceOffset = Math.sin(bounceTime * bounceSpeed) * bounceAmplitude;
	        currentPosition.y += bounceOffset; // Apply the bounce to the Y position
	    } else {
	        isMoving = false; // Stop tracking movement
	        bounceTime = 0.0; // Reset bounce time
	    }

	    // Apply new position while keeping the rotation
	    transform.setTranslation(currentPosition);
	    viewTransformGroup.setTransform(transform);
	}

	private void resetCamera() {
	    // Reset the viewTransform to the initial state
	    viewTransform.setIdentity(); // Reset the transform to the identity matrix
	    viewTransform.setTranslation(new Vector3d(0.25d, 0.25d, 4.0d)); // Set the initial position
	    rotationX = 0; // Reset X rotation
	    rotationY = 0; // Reset Y rotation

	    // Apply the reset transform to the viewTransformGroup
	    viewTransformGroup.setTransform(viewTransform);
	}
	
    @Override 
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Stop the bouncy effect when the user releases the movement keys
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
            isMoving = false;
            bounceTime = 0.0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but required by KeyListener
    }
    
    
 //-----------------------------------------------------------------------------------------------  
 // MouseListener implementation 
    @Override
    public void mouseClicked(MouseEvent event) {
        // Access the corpse TransformGroup
        
    	int x = event.getX(); 
        int y = event.getY(); 									// Get click position

        Point3d point3d = new Point3d(), center = new Point3d();
        canvas.getPixelLocationInImagePlate(x, y, point3d);	    // Get pixel location in ImagePlate
        canvas.getCenterEyeInImagePlate(center);                // Get eye's position in ImagePlate

        Transform3D transform3D = new Transform3D();
        canvas.getImagePlateToVworld(transform3D);              // Convert ImagePlate coordinates to Virtual World
        transform3D.transform(point3d);
        transform3D.transform(center);

        Vector3d mouseVec = new Vector3d();
        mouseVec.sub(point3d, center);
        mouseVec.normalize();

        pickTool.setShapeRay(point3d, mouseVec);                // Send a PickRay for intersection

        // Check if an object was picked
        PickResult pickResult = pickTool.pickClosest();    

        if (pickResult != null) {
            System.out.println("----- Picking Debug Info -----");
            System.out.println("Picked node: " + pickResult.getNode(PickResult.PRIMITIVE));
            System.out.println("Picked object: " + pickResult.getObject());
            Node pickedNode = pickResult.getNode(PickResult.SHAPE3D);
            System.out.println("Picked node class: " + (pickedNode != null ? pickedNode.getClass() : "null"));
		
            if (pickedNode instanceof Shape3D) {
                Shape3D pickedShape = (Shape3D) pickedNode;
                String objectName = pickedShape.getName();
                
                // Making the game works with keys  will be done here --------------------------------------------------------------
                
                if ("corpse".equals(objectName)) { 
                    if (!gameState.hasFound("CORPSE_EXAMINED")) {					
                        gameState.addClue("CORPSE_EXAMINED");						
                        TriggerEvents.triggerEvent("CORPSE_CLUE_FOUND",  x ,  y);    
                    }
                }
                if ("blade".equals(objectName)) { 
                    if (!gameState.hasFound("KNIFE_EXAMINED")) {                    
                        gameState.addClue("KNIFE_EXAMINED");                       
                        TriggerEvents.triggerEvent("KNIFE_CLUE_FOUND",  x ,  y);    
                    }
                }
                
                                             
                String[] requiredCluesForTV = {"CORPSE_EXAMINED", "KNIFE_EXAMINED", "PILLOW_FOUND", "WINDOW_FOUND", "CLOCK_FOUND", "RECORD_FOUND", "SOFA_FOUND", "BASS_FOUND", "FLOWER_CLUE_FOUND", "LETTER_EXAMINED"};
                boolean allCluesFound = true;
                for (String clue : requiredCluesForTV) {
                    if (!gameState.hasFound(clue)) {
                        allCluesFound = false;
                        break;
                    }
                }              
                
                if ("tv_screen".equals(objectName)) { 
                    if (!gameState.hasFound("TV_EXAMINED") && allCluesFound) {                 
                        gameState.addClue("TV_EXAMINED");
                        TriggerEvents.triggerEvent("TV_CLUE_FOUND",  x ,  y);
                    }
                }
                
                if ("FloorPillow".equals(objectName)) { 
                    if (!gameState.hasFound("PILLOW_FOUND")) {					
                        gameState.addClue("PILLOW_FOUND");						
                        TriggerEvents.triggerEvent("PILLOW_FOUND",  x ,  y);   
                    }
                }
                
                if ("windowFrame".equals(objectName)) { 
                    if (!gameState.hasFound("WINDOW_FOUND")) {					
                        gameState.addClue("WINDOW_FOUND");						
                        TriggerEvents.triggerEvent("WINDOW_FOUND",  x ,  y);   
                    }
                }
                
                if ("ClockCenter".equals(objectName)) { 
                    if (!gameState.hasFound("CLOCK_FOUND")) {					
                        gameState.addClue("CLOCK_FOUND");						
                        TriggerEvents.triggerEvent("CLOCK_FOUND",  x ,  y);   
                    }
                }
                
                if ("Rbase".equals(objectName)) { 
                    if (!gameState.hasFound("RECORD_FOUND")) {					
                        gameState.addClue("RECORD_FOUND");						
                        TriggerEvents.triggerEvent("RECORD_FOUND",  x ,  y);   
                    }
                }
                
                if ("sofa".equals(objectName)) { 
                    if (!gameState.hasFound("SOFA_FOUND")) {					
                        gameState.addClue("SOFA_FOUND");						
                        TriggerEvents.triggerEvent("SOFA_FOUND",  x ,  y);    
                    }
                }
                
                if ("doublebass".equals(objectName)) { 
                    if (!gameState.hasFound("BASS_FOUND")) {				
                        gameState.addClue("BASS_FOUND");						
                        TriggerEvents.triggerEvent("BASS_FOUND",  x ,  y);   
                    }
                }
                
                if ("petal".equals(objectName)) { 
                    if (!gameState.hasFound("FLOWER_CLUE_FOUND")) {					
                        gameState.addClue("FLOWER_CLUE_FOUND");						
                        TriggerEvents.triggerEvent("FLOWER_CLUE_FOUND",  x ,  y);    
                    }
                }
                
                if ("MirrorSurface".equals(objectName)) { 
                    if (!gameState.hasFound("MIRROR_EXAMINED") && gameState.hasFound("TV_EXAMINED")) {                  
                        gameState.addClue("MIRROR_EXAMINED");
                        TriggerEvents.triggerEvent("MIRROR_CLUE_FOUND", x, y);
                    }
                }

               if ("mail".equals(objectName)) { 
                    if (!gameState.hasFound("LETTER_EXAMINED")) {
                        SoundManager.getInstance().playSoundForDuration("paper", 1000);
                        HiddenLetter.toggleMailState(); // Will play paper sound
                        gameState.addClue("LETTER_EXAMINED");
                        TriggerEvents.triggerEvent("LETTER_CLUE_FOUND",  x ,  y);
                    }
                }
                
             //---------------------------------------------------------------------------------------------------------------
            }
        }
    }
  
    @Override
    public void mousePressed(MouseEvent e) {
        // Request focus when the mouse is pressed
        canvas.requestFocus();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Do nothing
    }

    // MouseMotionListener implementation
    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getX() - lastMouseX;
        int deltaY = e.getY() - lastMouseY;

        // Update rotation angles
        rotationY += -deltaX * mouseSensitivity; // Left/right rotation (around Y-axis)
        rotationX += -deltaY * mouseSensitivity; // Up/down rotation (around X-axis)

        // Clamp the up/down rotation to avoid flipping
        rotationX = Math.max(-Math.PI / 2, Math.min(Math.PI / 2, rotationX));

        // Get the current view transformation
        Transform3D transform = new Transform3D();
        viewTransformGroup.getTransform(transform);

        // Extract the current position (prevent resetting position!)
        Vector3d currentPosition = new Vector3d();
        transform.get(currentPosition);

        // Create a new rotation transform
        Transform3D rotationTransform = new Transform3D();
        rotationTransform.rotY(rotationY); // Apply Y-axis rotation (left/right)

        Transform3D tempRot = new Transform3D();
        tempRot.rotX(rotationX); // Apply X-axis rotation (up/down)
        rotationTransform.mul(tempRot);

        // Extract the rotation matrix
        Matrix3d rotationMatrix = new Matrix3d();
        rotationTransform.get(rotationMatrix);

        // Apply rotation while keeping the current position
        transform.setRotation(rotationMatrix); // Set only the rotation
        transform.setTranslation(currentPosition); // Restore position

        // Apply the final transform to the viewTransformGroup
        viewTransformGroup.setTransform(transform);

        // Update last mouse position
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        // Update last mouse position
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }
    
	
    // Collision Function 
    public static boolean isCollidingWithObstacles(Point3f futurePosition, float playerRadius) {
        for (Collidable obj : collisionObstacles) {
            Transform3D t3d = new Transform3D();
            obj.tg.getTransform(t3d);
            Vector3f objPos = new Vector3f();
            t3d.get(objPos);

            // Compute AABB boundaries
            float minX = objPos.x - obj.halfWidth;
            float maxX = objPos.x + obj.halfWidth;
            float minY = objPos.y - obj.halfHeight;
            float maxY = objPos.y + obj.halfHeight;
            float minZ = objPos.z - obj.halfDepth;
            float maxZ = objPos.z + obj.halfDepth;

            // Use a bounding sphere around the player, so we check if the sphere intersects the box
            float px = futurePosition.x;
            float py = futurePosition.y;
            float pz = futurePosition.z;

            // Clamp player's position to the closest point on the AABB
            float closestX = Math.max(minX, Math.min(px, maxX));
            float closestY = Math.max(minY, Math.min(py, maxY));
            float closestZ = Math.max(minZ, Math.min(pz, maxZ));

            float dx = px - closestX;
            float dy = py - closestY;
            float dz = pz - closestZ;

            float distanceSquared = dx * dx + dy * dy + dz * dz;

            if (distanceSquared < playerRadius * playerRadius) {
            	
            	
            	//remove this after 
            	System.out.println("Collision detected with object at (" + 
                        objPos.x + "," + objPos.y + "," + objPos.z + 
                        "). Player position: (" + px + "," + py + "," + pz + 
                        "). Collider dimensions: " + obj.halfWidth + "x" + 
                        obj.halfHeight + "x" + obj.halfDepth);
            	
            	
            	
                return true;
            }
        }
        return false;
    }
}
    
    
class Collidable {
    public TransformGroup tg;
    public float halfWidth;   // X direction
    public float halfHeight;  // Y direction
    public float halfDepth;   // Z direction

    public Collidable(TransformGroup tg, float halfWidth, float halfHeight, float halfDepth) {
        this.tg = tg;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
        this.halfDepth = halfDepth;
    }
}




