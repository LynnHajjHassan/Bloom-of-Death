package comp2800Labs;

/* Copyright material by xyuan@uwindsor.ca,
 * for students working on assignments and projects */

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.Switch;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

public class CodeLab9LH extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	private static String frame_name = "LH's Lab #9";
	private static boolean r_tag = true;
	private static boolean object_tag = true;
	private static int select_key = 0;
	private static final String OBJECT_NAME = "Morphing Object";
	private static Alpha[] diskAlpha = new Alpha[2];			// NEW: Changed 1 to 2, since we need one alpha for the top surface and another one for the bottom surface 
	private static Switch[] morphSwitch = new Switch[3];		// New: Changed 2 to 3, to accommodate three surfaces 
	
	private static String[] cases = {"Stop Front", "Stop Back" ,"Resume Front", "Resume Back" } ; // New: Cases added for the Textured Disk Option
	private static int cIndex = 0 ;  // New:  An index for the cases array 
	
	/* a function to create a switch of morphing behavior for (disk) side with different edges;
	 * the two switch children are two morphing objects, one with changing edges 4<>-64<->4 and
	 * the other with changing edges 4<->64<->8. */
	private static Switch morph_Switch(int i, TransformGroup priorTG, String s, Switch[] morph) {
		// NEW : declared the points that we need to create morph transitions
		// Initialized with 4, 16, and 8 assuming the scene will starts with Key 1's morph transition 4 <-> 16 <-> 8 
		int n0 = 4 ; 
		int n1 = 16 ; 
		int n2 = 8 ; 
		morph[i] = new Switch();
		morph[i].setCapability(Switch.ALLOW_SWITCH_WRITE);
		// NEW : edited the loop to create the 4  morphs transitions that we need 
		for (int j = 0; j < 4; j++){                      // add two morphing surfaces to switch 'morph[i]' // NEW: Changed 2 to 4, since we need 4 transition 
			morph[i].addChild(L9MorphShapeLH.set_Morph( priorTG, s, n0 , n1, n2 ) );
			n0 = n0  * 2 ; 
			n2 = n2  * 2 ;  
			if ( n1 == 64 ) { 								// if n1 is equal to 64, bring it back to 4 							
				n1 = 4 ;
			} else {
				n1 = n1  * 2 ; 
			}				
		}
		// Reset initial values of the points
		n0 = 4 ; 
		n1 = 16 ; 
		n2 = 8 ; 
		
		morph[i].setWhichChild(0);                         // set default with changing edges 4<>-64<->8

		return morph[i];
	}
	
	/* a function to the disk's two side surface rotating  */
	public static void morph_Shapes(TransformGroup sceneTG, Alpha[] alpha, Switch[] morph) {
		String[] side_name = {"Top", "Side", "Bottom"};
		Transform3D side, slide, plate, rotate_axis;
		TransformGroup sideTG, slideTG, plateTG, hingeTG;
		RotationInterpolator rotationInterpol;
		
		// New: Define new variables for the bottom surface-------------------
		Transform3D slide2, plate2, rotate_axis2, side2 ;
		TransformGroup slide2TG, plate2TG, hinge2TG, side2TG ; 
		RotationInterpolator rotationInterpol2;
		//---------------------------------------------------------------------
		
		slide = new Transform3D();
		slide.setTranslation(new Vector3f(-2.0f, 0, -0.1f));
		slideTG = new TransformGroup(slide);

		plate = new Transform3D();                         // shift the disk's far end to rotational origin
		plate.setTranslation(new Vector3f(2.0f, 0, 0));			
		plateTG = new TransformGroup(plate);               // need 'plateTG' to position disk for rotation
		plateTG.addChild(morph_Switch(0, plateTG, side_name[0], morph));
			
		hingeTG = new TransformGroup();                    // use 'hingeTG' for rotation
		hingeTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotate_axis = new Transform3D();                   // rotate around 'hingeTG's y-axis
		alpha[0] = new Alpha(-1, Alpha.INCREASING_ENABLE |
				Alpha.DECREASING_ENABLE, 0, 0, 4000, 0000, 1000, 4000, 0000, 1000);
		rotationInterpol = new RotationInterpolator(alpha[0], hingeTG, rotate_axis, 
				0, -(float)(Math.PI / 2.0));               // rotate for +/-90 degrees
		rotationInterpol.setSchedulingBounds(CommonsLH.twenty_BS);
		slideTG.addChild(rotationInterpol);                // add rotation behavior to 'morphBG'
		hingeTG.addChild(plateTG);                         // attach translated disk for rotation

		side = new Transform3D();
		side.setTranslation(new Vector3f(0, 0, 0.1f));
		sideTG = new TransformGroup(side);                 // need 'sideTG' to move the two disks in place;
		sideTG.addChild(hingeTG);                          // adjust the rotating disk's height
		slideTG.addChild(sideTG);			               // center the adjusted rotating disk
		sceneTG.addChild(slideTG);                         // attach the non-rotating disk side
		
		
		// New: Part made for the bottom surface ---------------------------------------------------------------------------------------------
		slide2 = new Transform3D();
		slide2.setTranslation(new Vector3f( -2.0f, 0, -0.1f));
		slide2TG = new TransformGroup(slide2);

		plate2 = new Transform3D();                         // shift the circular Bottom surface's far end to rotational origin
		plate2.setTranslation(new Vector3f( 2.0f, 0, 0.1f));
		plate2TG = new TransformGroup(plate2);               // need 'plate2TG' to position circular surface for rotation
		plate2TG.addChild(morph_Switch(2, plate2TG, side_name[2], morph));		//Create the Bottom Object's morph_switch


		hinge2TG = new TransformGroup();                    // use 'hingeTG' for rotation
		hinge2TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotate_axis2 = new Transform3D();                   // rotate around 'hingeTG's y-axis
		alpha[1] = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 
				4000, 0000, 1000, 4000, 0000, 1000);
		rotationInterpol2 = new RotationInterpolator(alpha[1], hinge2TG, rotate_axis2, 0,(float) (Math.PI / 2.0)); 
		rotationInterpol2.setSchedulingBounds(CommonsLH.twenty_BS);
		slide2TG.addChild(rotationInterpol2);                // add rotation behavior to 'slideTG'

		hinge2TG.addChild(plate2TG);                         // attach translated circular surface for rotation				
		
		// Note to myself : 
		// tried to do the same here as for the Top surface, however seems like the following commented lines has no effect on the bottom surface 
		// Therefore I removed them to keep the code simple
		//side2 = new Transform3D();
		//side2.setTranslation(new Vector3f(0, 0, 0.0f));
		//side2TG = new TransformGroup(side2);                 // need 'sideTG' to move the two disks in place;
		//side2TG.addChild(hinge2TG);                          // adjust the rotating disk's height
		//slide2TG.addChild(side2TG);
		
		slide2TG.addChild(hinge2TG);
		sceneTG.addChild(slide2TG);                          // attach the non-rotating disk Bottom side
		
		// End of my new edits for the Bottom surface------------------------------------------------------------------------------------------------
		
		// Part related to the side object 
		sceneTG.addChild(morph_Switch(1, sceneTG, side_name[1], morph));
		
	}
	
	/* a function to build and return the content branch */
	private static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup(); 
		
		TransformGroup sceneTG = new TransformGroup();     // introduce a TransformGroup for rotation 
		sceneBG.addChild(CommonsLH.rotate_Behavior(7500, sceneTG));
		
		morph_Shapes(sceneTG, diskAlpha, morphSwitch);
		
		CommonsLH.control_Rotation(r_tag);                 // make 'sceneTG' rotating by default
		sceneBG.addChild(sceneTG);
		Alpha cubeAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE |
				Alpha.DECREASING_ENABLE, 0, 0, 3000, 0000, 750, 3000, 0000, 750);
		sceneBG.addChild(CodeLab8LH.move_Cube(cubeAlpha));

		return sceneBG;
	}

	/* a constructor to set up for the application */
	public CodeLab9LH(BranchGroup scene) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
		canvas3D.addKeyListener(this);                     // NOTE: enable key events 	
		canvas3D.setSize(800, 800);                        // set size of canvas
		SimpleUniverse su = new SimpleUniverse(canvas3D);  // create a SimpleUniverse
		                                                   // set the viewer's location
		CommonsLH.define_Viewer(su, new Point3d(1.35, -0.35, 12.0)); 		
		scene.addChild(CommonsLH.add_Lights(CommonsLH.White, 1));
		
		scene.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(scene);                          // attach 'scene' to 'su'

		Menu m = new Menu("Menu");                         // set menu's label
		m.addActionListener(this);
		MenuBar menuBar = CodeLab2LH.build_MenuBar(m, OBJECT_NAME);
		frame.setMenuBar(menuBar);                         // build and set the menu bar

		setLayout(new BorderLayout());
		add("Center", canvas3D);
		frame.setSize(810, 800);                           // set the size of the frame
		frame.setVisible(true);
	}

	public static void main(String[] args) {               // NOTE: copyright material
		frame = new JFrame(frame_name + ": Rotating Textured Disks");
		frame.getContentPane().add(new CodeLab9LH(create_Scene()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		switch(e.getActionCommand()) {                     // handle the selected menu item
		case "Exit": 
			System.exit(0);                                // quit the application
		case "Pause/Rotate":
			r_tag = (r_tag == true)? false : true;
			CommonsLH.control_Rotation(r_tag);
			return;
		case OBJECT_NAME:					//  New ------------------------------------			
			frame.setTitle(frame_name + ": "+ cases[cIndex] );
			
			if (cases[cIndex].equals("Stop Front")) {
				diskAlpha[0].pause() ; 
				diskAlpha[1].resume() ; 
			}
			else if (cases[cIndex].equals("Stop Back"))
				diskAlpha[1].pause() ; 
			else if (cases[cIndex].equals("Resume Front"))
				diskAlpha[0].resume() ; 
			else if (cases[cIndex].equals("Resume Back"))
				diskAlpha[1].resume() ; 

			cIndex = (cIndex == 3) ? 0 : cIndex + 1; // ------------------------------------
		default:
			return;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int k;

		switch(e.getKeyCode()) {							
		case KeyEvent.VK_1:
			k = 0; break;
		case KeyEvent.VK_2:			
			k = 1; break;
		case KeyEvent.VK_3:									// NEW : Added new case for the key 3 
			k = 2; break;
		case KeyEvent.VK_4:									// NEW : Added new case for the key 4 
			k = 3; break;
		default: 
			return;                                        // accept only the specific keys as inputs
		}
		
		if (select_key == k)
			return;                                        // make the change only when different from current
		else
			select_key = k;                                // update the selected switch child
		
		for (int i = 0; i < 3; i++)							// NEW:  Changed 2 to 3 since we are working with three surfaces now ( additional Bottom surface ) 
			morphSwitch[i].setWhichChild(k);               // use the selected switch child

	}

	public void keyReleased(KeyEvent arg0) { }
	public void keyTyped(KeyEvent e) { }	
}
