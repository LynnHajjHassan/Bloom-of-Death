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
import org.jogamp.java3d.PositionInterpolator;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3d;

public class CodeLab6LH extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	private static String frame_name = "LH's Lab #6";
	private static boolean r_tag = true;			// used to control when the sides should move
	
	// NEW, added for lab6
	private static String[] cases = {"Stop Front", "Stop Back" ,"Resume Front", "Resume Back" } ; 
	private static int cIndex = 0 ;  // An index for the cases array 
	static Alpha alphaT ,alphaB ; // NEW: added for lab6, had to declare it at the top to be used in action performed class 
	
	private static final String OBJECT_NAME = "Textured Disk";
	
	/* a function to build and return the content branch */
	private static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();

		TransformGroup sceneTG = new TransformGroup();     // introduce a TransformGroup for rotation 
		sceneBG.addChild(CommonsLH.rotate_Behavior(7500, sceneTG));

		String[] side_name = {"Top", "Side", "Bottom"};              // create disk sides, added the Bottom option for this lab 
		
		//Created the side surface
		sceneTG.addChild(L5TextureSurfaceLH.ring_Shape(side_name[1], 60));

		TransformGroup topTG = new TransformGroup();      // need 'topTG' to move the (top) surface
		topTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		topTG.addChild(L5TextureSurfaceLH.ring_Shape(side_name[0], 60));  //creates the top surface
		
		// NEW!! Creating the Bottom Part :
		TransformGroup bottomTG = new TransformGroup();      // need 'bottomTG' to move the (top) surface
		bottomTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bottomTG.addChild(L5TextureSurfaceLH.ring_Shape(side_name[2], 60));  //creates the top surface
		
		// ِAdd an animation for the Top side
		Transform3D axisPosition = new Transform3D();
		axisPosition.rotY(-Math.PI / 2.0);                 // need to move along X-axis
		alphaT = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 4000, 0000, 1000, 4000, 0000, 1000);
		PositionInterpolator positionInterpol = 
				new PositionInterpolator(alphaT, topTG, axisPosition, 0.6f, 0.0f);
		positionInterpol.setSchedulingBounds(CommonsLH.twenty_BS);
		
		// NEW for the Bottom surface, add an animation for it
		Transform3D axisPosition2 = new Transform3D();
		axisPosition2.rotY(-Math.PI / 2.0);                 // need to move along X-axis
		alphaB = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 
				0, 0, 4000, 0000, 1000, 4000, 0000, 1000);
		PositionInterpolator positionInterpol2 = new PositionInterpolator(alphaB, bottomTG, axisPosition2, -0.6f, 0.0f); // NEW Changed to -0.6 
		positionInterpol2.setSchedulingBounds(CommonsLH.twenty_BS);
		
		
		// Add topTG to sceneTG
		sceneTG.addChild(topTG);
		sceneTG.addChild(positionInterpol);
		
		// NEW: Add bottomTG to sceneTG
		sceneTG.addChild(bottomTG);
		sceneTG.addChild(positionInterpol2);

		
		CommonsLH.control_Rotation(r_tag);                 // make 'sceneBG' rotating by default
		sceneBG.addChild(sceneTG);
		
		return sceneBG;  
	}

	/* a constructor to set up for the application */
	public CodeLab6LH(BranchGroup scene) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
		canvas3D.setSize(800, 800);                        // set size of canvas
		SimpleUniverse su = new SimpleUniverse(canvas3D);  // create a SimpleUniverse
		                                                   // set the viewer's location
		CommonsLH.define_Viewer(su, new Point3d(1.35, -0.35, 10.0)); 		
		scene.addChild(CommonsLH.add_Lights(CommonsLH.White, 2));
		
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
		frame = new JFrame(frame_name + ": Moving Textured Disks");
		frame.getContentPane().add(new CodeLab6LH(create_Scene()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	

	@Override
	public void actionPerformed(ActionEvent e) {		
		switch(e.getActionCommand()) {                     // handle different menu items
		case "Exit": 
			System.exit(0);                                // quit the application
		case "Pause/Rotate":
			r_tag = (r_tag == true)? false : true;
			CommonsLH.control_Rotation(r_tag);
			return;
		case "Textured Disk":
			
			frame.setTitle(frame_name + ": "+ cases[cIndex] );
			
			if (cases[cIndex].equals("Stop Front")) {
				alphaT.pause() ; 
				alphaB.resume() ; 
			}
			else if (cases[cIndex].equals("Stop Back"))
				alphaB.pause() ; 
			else if (cases[cIndex].equals("Resume Front"))
				alphaT.resume() ; 
			else if (cases[cIndex].equals("Resume Back"))
				alphaB.resume() ; 
			
			cIndex = (cIndex == 3) ? 0 : cIndex + 1;
			
		default:
			return;
		}
	}	
}
