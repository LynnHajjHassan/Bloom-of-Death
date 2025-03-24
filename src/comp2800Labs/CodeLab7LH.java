package comp2800Labs;

/* Copyright material by xyuan@uwindsor.ca,
 * for students working on assignments and projects */

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

public class CodeLab7LH extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	private static String frame_name = "LH's Lab #7";
	private static boolean r_tag = true;
	private static final String OBJECT_NAME = "Textured Disk";
	private static Alpha[] alpha = new Alpha[2];		// New: Changed Alpha to contain two instead of one 
	
	private static String[] cases = {"Stop Front", "Stop Back" ,"Resume Front", "Resume Back" } ; // New: Cases added for the Textured Disk Option
	private static int cIndex = 0 ;  // New:  An index for the cases array 

	
	// New: added on 3/3/2025, added for lab08, to manipulate surfaces'alphas, so we can pause it and resume it in Lab08's menu 
	public static void stopOrResume(String surface, String mode) {
		if ( mode.equals("stop")) {
			if ( surface.equals("Front")) {
				alpha[0].pause();
			}else if ( surface.equals("Back")) {
				alpha[1].pause();
			}
		}else if (mode.equals("resume")) {
			if ( surface.equals("Front")) {
				alpha[0].resume();
			}else if ( surface.equals("Back")) {
				alpha[1].resume();
			}
		}	
	}
	
	public static Alpha[] get_Alphas() {
	    return new Alpha[]{alpha[0], alpha[1]};
	}
	
	/* a function to make the disk's side surface rotating  */
	public static void rotate_Side(BranchGroup snBG, TransformGroup snTG, Alpha[] aph) {

		String[] side_name = {"Top", "Side", "Bottom"};   						// NEW: Added Bottom
		Transform3D slide, plate, rotate_axis, slide2, plate2, rotate_axis2 ;	// NEW: Added slide2, plate2, rotate_axis2 for the Bottom Surface
		TransformGroup slideTG, plateTG, hingeTG,slide2TG, plate2TG, hinge2TG;  // New: Added slide2TG, plate2TG, hinge2TG for the Bottom surface  
		RotationInterpolator rotationInterpol,rotationInterpol2 ; 				//	New: Added rotationInterpol2 for the Bottom Surface
		
		slide = new Transform3D();
		slide.setTranslation(new Vector3f(-2.0f, 0, 0.1f));
		slideTG = new TransformGroup(slide);

		plate = new Transform3D();                         // shift the circular surface's far end to rotational origin
		plate.setTranslation(new Vector3f(2.0f, 0, -0.1f));
		plateTG = new TransformGroup(plate);               // need 'plateTG' to position circular surface for rotation
		plateTG.addChild(L5TextureSurfaceLH.ring_Shape(side_name[0], 60));

		hingeTG = new TransformGroup();                    // use 'hingeTG' for rotation
		hingeTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotate_axis = new Transform3D();                   // rotate around 'hingeTG's y-axis
		aph[0] = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 
				4000, 0000, 1000, 4000, 0000, 1000);
		rotationInterpol = new RotationInterpolator(aph[0], hingeTG, rotate_axis, 0, 
				-(float) (Math.PI / 2.0)); 
		rotationInterpol.setSchedulingBounds(CommonsLH.twenty_BS);
		slideTG.addChild(rotationInterpol);                // add rotation behavior to 'slideTG'

		hingeTG.addChild(plateTG);                         // attach translated circular surface for rotation
		slideTG.addChild(hingeTG);                         // attach the rotating circular surface

		snTG.addChild(slideTG);                            // attach the non-rotating disk side
		snTG.addChild(L5TextureSurfaceLH.ring_Shape(side_name[1], 60));
		
		// NEW: Added for the Bottom surface --------------------------------
		slide2 = new Transform3D();
		slide2.setTranslation(new Vector3f( -2.0f, 0, -0.1f));
		slide2TG = new TransformGroup(slide2);

		plate2 = new Transform3D();                         // shift the circular Bottom surface's far end to rotational origin
		plate2.setTranslation(new Vector3f( 2.0f, 0, 0.1f));
		plate2TG = new TransformGroup(plate2);               // need 'plate2TG' to position circular surface for rotation
		plate2TG.addChild(L5TextureSurfaceLH.ring_Shape(side_name[2], 60));		// Create the Bottom Object 

		hinge2TG = new TransformGroup();                    // use 'hingeTG' for rotation
		hinge2TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotate_axis2 = new Transform3D();                   // rotate around 'hingeTG's y-axis
		aph[1] = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 
				4000, 0000, 1000, 4000, 0000, 1000);
		rotationInterpol2 = new RotationInterpolator(aph[1], hinge2TG, rotate_axis2, 0,(float) (Math.PI / 2.0)); 
		rotationInterpol2.setSchedulingBounds(CommonsLH.twenty_BS);
		slide2TG.addChild(rotationInterpol2);                // add rotation behavior to 'slideTG'

		hinge2TG.addChild(plate2TG);                         // attach translated circular surface for rotation
		slide2TG.addChild(hinge2TG);                         // attach the rotating circular surface
		
		snTG.addChild(slide2TG);                            // attach the non-rotating disk Bottom side
		
		// End of my new edits for the Bottom surface--------------------------------		
		
	}
	
	/* a function to build and return the content branch */
	private static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();

		TransformGroup sceneTG = new TransformGroup();     // introduce a TransformGroup for rotation 
		sceneBG.addChild(CommonsLH.rotate_Behavior(7500, sceneTG));

		rotate_Side(sceneBG, sceneTG, alpha);              // make the two side surface rotating
		CommonsLH.control_Rotation(r_tag);                 // make 'sceneTG' rotating by default
		sceneBG.addChild(sceneTG);
		
		return sceneBG;  
	}

	/* a constructor to set up for the application */
	public CodeLab7LH(BranchGroup scene) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
		canvas3D.setSize(800, 800);                        // set size of canvas
		SimpleUniverse su = new SimpleUniverse(canvas3D);  // create a SimpleUniverse
		                                                   // set the viewer's location
		CommonsLH.define_Viewer(su, new Point3d(1.35, -0.35, 10.0)); 		
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
		frame.getContentPane().add(new CodeLab7LH(create_Scene()));
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
		case OBJECT_NAME :
			
			frame.setTitle(frame_name + ": "+ cases[cIndex] );
			
			if (cases[cIndex].equals("Stop Front")) {
				alpha[0].pause() ; 
				alpha[1].resume() ; 
			}
			else if (cases[cIndex].equals("Stop Back"))
				alpha[1].pause() ; 
			else if (cases[cIndex].equals("Resume Front"))
				alpha[0].resume() ; 
			else if (cases[cIndex].equals("Resume Back"))
				alpha[1].resume() ; 

			cIndex = (cIndex == 3) ? 0 : cIndex + 1;
			
		default:
			return;
		}
	}	
}
