package CodesML2800;

/* Copyright material, for students to work on assignments and projects */

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.picking.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class CodeA4ML extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static TransformGroup sceneTG;
	private static String snd_bk = "Fan";                // specify the name of the background sound
	private static SoundUtilityJOAL soundJOAL;             // need for playing sound

	private static Switch objectSwitch = new Switch();     // allow the two buttons responding to interaction
	private Canvas3D canvas3D;                             // need for mouse picking
	private static PickTool pickTool;	
	
	private static final int OBJ_NUM = 8;
	private static A3ObjectsML[] object3D = new A3ObjectsML[OBJ_NUM];
	private static boolean on = true;
	private static boolean rotate = true;
	
	
	public static TransformGroup create_Base(String str) {
		BaseShape baseShape = new BaseShape();
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(new Vector3d(4d, 2d, 4d));         // set scale for the 4x4 matrix
		
		TransformGroup baseTG = new TransformGroup(scaler); 
		baseTG.addChild(baseShape.position_Object());

		ColorString clr_str = new ColorString(str, CommonsML.Red, 0.06, new Point3f(-str.length() / 4f, -9.4f, 8.2f));
		Transform3D r_axis = new Transform3D();            // default: rotate around Y-axis
		r_axis.rotY(Math.PI);                              
		TransformGroup objRG = new TransformGroup(r_axis); 
		objRG.addChild(clr_str.position_Object());         // move string to baseShape's other side
		baseTG.addChild(objRG);

		return baseTG;
	}
	
	public static TransformGroup create_Fan() {
		TransformGroup fanTG = new TransformGroup();

		object3D[0] = new StandObject();                   // create "FanStand"
		fanTG = object3D[0].position_Object();             // set 'fan_baseTG' to FanStand's 'objTG'  
		object3D[1] = new SwitchObject();                  // create and attach "Switch" to "Stand"
		object3D[0].add_Child(object3D[1].position_Object());
		
		//on off buttons
		Vector3f post = new Vector3f(0.5f, 0.25f, 0f);    
		object3D[6] = new SwitchObjectA4(post, "RotateButton");	
		object3D[1].add_Child(object3D[6].position_Object());
		
		Vector3f post2 = new Vector3f(-0.5f, 0.25f, 0f); 
		object3D[7] = new SwitchObjectA4(post2, "PowerButton");
		object3D[1].add_Child(object3D[7].position_Object());
		
		
		//create fanshaft
		object3D[2] = new ShaftObject();
		object3D[0].add_Child(object3D[2].position_Object());
		
		//create motor
		object3D[3] = new MotorObject();
		object3D[2].add_Child(object3D[3].position_Object());
		
		//create blades
		object3D[4] = new BladeObject();
		object3D[3].add_Child(object3D[4].position_Object());
		
		//create blades
		object3D[5] = new GuardObject();
		object3D[3].add_Child(object3D[5].position_Object());
		
		CoordinateSystem coordinateSystem = new CoordinateSystem();	
		Transform3D coordinateTransform = new Transform3D();	
		coordinateTransform.setTranslation(new Vector3f(0.0f, 1.0f, 0.0f)); 
		TransformGroup coordSystemTG = new TransformGroup(coordinateTransform);
		coordSystemTG.addChild(coordinateSystem.get_BranchGroup()); 
		fanTG.addChild(coordSystemTG);
		fanTG.addChild(create_Base("ML's Desk Fan"));  // create and attach "Base" to "FanStand"
		return fanTG;
	}
	
	/* a function to create and return the scene BranchGroup */
	private static BranchGroup create_Scene() {
		
		BranchGroup sceneBG = new BranchGroup();		   // create 'sceneBG' as content branch
		sceneTG = new TransformGroup();                    // make 'sceneTG' changeable

		sceneTG.addChild(create_Fan());
		
		sceneBG.addChild(sceneTG);
		sceneBG.addChild(CommonsML.add_Lights(CommonsML.White, 1));
		
		pickTool = new PickTool(sceneBG);                  // make object(s) in 'sceneBG' pickable
		pickTool.setMode(PickTool.GEOMETRY);               // set to pick by geometry
		
		soundJOAL = new SoundUtilityJOAL();
		if (!soundJOAL.load(snd_bk, 0f, 0f, 10f, true)) 
			System.out.println("Could not load " + snd_bk);
		else
			soundJOAL.play(snd_bk);

		return sceneBG;
	}
	
	/* a specialized constructor that enables sound and (mouse-based and keyboard-based) interaction  */
	public CodeA4ML(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas3D = new Canvas3D(config);
				
		SimpleUniverse su = new SimpleUniverse(canvas3D);  // create a SimpleUniverse		
		CommonsML.define_Viewer(su, new Point3d(0.25d, 0.25d, 10.0d));

		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach 'sceneBG' to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas3D);
		
		KeyHandler keyHandler = new KeyHandler() ; 
		canvas3D.addKeyListener(keyHandler);
        canvas3D.setFocusable(true);  // Make sure the canvas can receive key events
        
		canvas3D.addMouseListener(this); 
		
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}

	/* the main entrance of the application with specified window dimension */
	public static void main(String[] args) {
		frame = new JFrame("ML Assignment 4");                // create an instance of the class
		frame.getContentPane().add(new CodeA4ML(create_Scene()));  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	
	
	static class KeyHandler extends KeyAdapter {	
	    @Override
	    public void keyTyped(KeyEvent e) {
	        char keyChar = e.getKeyChar(); 

	        // Access the Switch objects for the buttons
	        Switch RotateButton = ((SwitchObjectA4) object3D[6]).get_objectSwitch(); 
	        Switch PowerButton = ((SwitchObjectA4) object3D[7]).get_objectSwitch(); 

	        if ((keyChar == 'z' || keyChar == 'Z') && on) {                          
	            if (rotate) {								  
	                object3D[2].get_Alpha().pause();  
	                RotateButton.setWhichChild(0);  
	            } else {
	                object3D[2].get_Alpha().resume(); 
	                RotateButton.setWhichChild(1);   
	            }
	            rotate = !rotate; 
	        } 
	        else if (keyChar == 'x' || keyChar == 'X') {                
	            if (on) { 
	                object3D[4].get_Alpha().pause();         
	                PowerButton.setWhichChild(0);    
                    soundJOAL.stop(snd_bk);                     
	                if (rotate) {
	                    object3D[2].get_Alpha().pause();
	                    RotateButton.setWhichChild(0);  
	                }
	            } else {
	                if (rotate) {
	                    object3D[2].get_Alpha().resume(); 
	                    RotateButton.setWhichChild(1);  
	                }
	                object3D[4].get_Alpha().resume();
	                PowerButton.setWhichChild(1); 
                    soundJOAL.play(snd_bk);                
	            }
	            on = !on; 
	        }     
	    }
	}

//	@Override
//	public void keyPressed(KeyEvent e) {
//		Transform3D tmp = new Transform3D();
//		int key_code = e.getKeyCode();
//		if ((key_code == KeyEvent.VK_R)) {
//			Matrix4d mat = new Matrix4d();
//			sceneTG.getTransform(tmp);                     // retrieve from working BG
//			tmp.get(mat);                                  // save current matrix to 'mat'
//			Matrix4d m4d = new Matrix4d();                 // define an identity matrix
//			m4d.rotY(0.1);
//			mat.mul(m4d);                                  // define and perform rotation
//			tmp.set(mat);
//			sceneTG.setTransform(tmp);                     // set back to the working BG
//		}
//		if (key_code == KeyEvent.VK_O) {
//			tmp = new Transform3D();                       // the same matrix as L.46
//			sceneTG.setTransform(tmp);                     // reset to the original
//		}
//	}
	
	public void mouseClicked(MouseEvent event) {
		int x = event.getX(); int y = event.getY();        // mouse coordinates
		
		Switch RotateButton = ((SwitchObjectA4) object3D[6]).get_objectSwitch(); 
        Switch PowerButton = ((SwitchObjectA4) object3D[7]).get_objectSwitch(); 
        
		Point3d point3d = new Point3d(), center = new Point3d();
		canvas3D.getPixelLocationInImagePlate(x, y, point3d); // obtain AWT pixel in ImagePlate coordinates
		canvas3D.getCenterEyeInImagePlate(center);         // obtain eye's position in IP coordinates
		
		Transform3D transform3D = new Transform3D();       // matrix to relate ImagePlate coordinates~
		canvas3D.getImagePlateToVworld(transform3D);       // to Virtual World coordinates
		transform3D.transform(point3d);                    // transform 'point3d' with 'transform3D'
		transform3D.transform(center);                     // transform 'center' with 'transform3D'

		Vector3d mouseVec;
		mouseVec = new Vector3d();
		mouseVec.sub(point3d, center);
		mouseVec.normalize();

		pickTool.setShapeRay(point3d, mouseVec);           // send a PickRay for intersection

		PickResult pickResult = pickTool.pickClosest();	
		
		if (pickResult != null) {
	    	
	        Node pickedNode = pickResult.getNode(PickResult.PRIMITIVE);
	        
	        if (pickedNode instanceof Box) {
	        	
	            Box pickedBox = (Box) pickedNode;
	            String objectName = pickedBox.getName(); // Get the name of the picked object shape

	            // Check if the picked object is a button
	            if ("RotateButton".equals(objectName) && on) {	  //  and the Fan is On, means this button won't work unless the Fan is On  
	                int currentState = (int) pickedBox.getUserData(); // Get current state (0 or 1)	                               
	                if (currentState == 0 ) {		        		  // if the button is red 
	                	RotateButton.setWhichChild(1);  			  // Switch to Green 
						object3D[2].get_Alpha().resume();			  // resume rotating 
						rotate = true ; 
	                } else {										  // if the button is green 
	                	RotateButton.setWhichChild(0);      		  // Switch to Red (OFF)
						object3D[2].get_Alpha().pause(); 
						rotate = false ;                     
	                }
	            } else  if ("PowerButton".equals(objectName)) {
	                int currentState2 = (int) pickedBox.getUserData(); // Get current state (0 or 1)	           
	                if (currentState2 == 0) {				  			// If red ( OFF ) we need to turn it ON   
	                	// Switch both buttons to Green (ON)
	                	PowerButton.setWhichChild(1);  	  	  			
	                    soundJOAL.play(snd_bk);       		  			// Play sound	                    
	                    if (rotate) {
							object3D[2].get_Alpha().resume(); 			// Resume Shaft rotation 
		                	RotateButton.setWhichChild(1);  
						}
						object3D[4].get_Alpha().resume();	  			// Resume Blades rotation
						on = true ; 
	                } else {							 				// If green, fan is ON and we need to turn it OFF
	                	// Switch both to Red (OFF)
	                	PowerButton.setWhichChild(0);   				
	                	RotateButton.setWhichChild(0); 
	                    soundJOAL.pause(snd_bk);        	    		// Stop sound
	                    object3D[4].get_Alpha().pause();				// Stop blades				
						if (rotate) {
							object3D[2].get_Alpha().pause();			// Pause the rotation of the shaft
						}
						on = false ; 	                                       
	                }
	            	              	
	            }
	        }
	    }
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}

class SwitchObjectA4 extends A3ObjectsML {
	private Switch objectSwitch;
	public SwitchObjectA4( Vector3f post , String name) {
		objectSwitch = new Switch();
		objectSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		scale = 0.5d;  
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(scale);                           // set scale for the 4x4 matrix
		scaler.setTranslation(post);                      // set translations for the 4x4 matrix
		objTG = new TransformGroup(scaler);               // set the translation BG with the 4x4 matrix and created objTG of the button
		objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTG.setCapability(Node.ENABLE_PICK_REPORTING); // need for mouse picking
		
		// Set up objSwitch with the two boxes, red and white 
		objectSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		for (int i = 0; i < 2; i++) { 
			Color3f clr = (i == 0) ? CommonsML.Red : CommonsML.Green;
			Appearance app = CommonsML.set_Appearance(clr);
			Box box = new Box(0.5f, 0.5f, 0.5f, Primitive.GENERATE_NORMALS, app);
			box.setUserData(i);                            // 'UserData' retrievable at picking
			box.setName(name);                            // NOTE: 'Name' is also retrievabl		
			objectSwitch.addChild(box);
		}
		objectSwitch.setWhichChild(1);
		
		// Attach objSwitch to objTG
		objTG.addChild(objectSwitch);     
		
	}

	public TransformGroup position_Object() {			// Used to return objTG
		return objTG;                                     
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
	public Switch get_objectSwitch() {			          // used to return the Buttons objectSwitch
		return objectSwitch;                                     
	}
}


