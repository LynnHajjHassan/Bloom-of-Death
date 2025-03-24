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
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.PositionInterpolator;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3d;

public class CodeLab8LH extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	private static String frame_name = "Lh's Lab #8";
	private static boolean r_tag = true;
	private static boolean object_tag = true;
	private static final String OBJECT_NAME = "Textured Disk";
	private static Alpha[] alpha = new Alpha[2];
	
	private static String[] cases = {"Stop Front", "Stop Back" ,"Resume Front", "Resume Back" } ; // New: Cases added for the Textured Disk Option
	private static int cIndex = 0 ;  // New:  An index for the cases array 
	
	/* a function to make a ColorCube moving with transparency changed to indicate collision */ 
	public static TransformGroup move_Cube(Alpha alpha) {
		TransformGroup moveTG = new TransformGroup();      // need 'moveTG' to make cube moving
		moveTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		Transform3D axisPosition = new Transform3D();
		PositionInterpolator positionInterpol = new PositionInterpolator(alpha, moveTG, 
				axisPosition, -4f, 4f);		
		positionInterpol.setSchedulingBounds(CommonsLH.twenty_BS);
		moveTG.addChild(positionInterpol);	               // create the moving behavior
		
		ColorCube colorCube = new ColorCube(0.6);
		colorCube.setName("ColorCube");
		Appearance app = new Appearance();                 // allow appearance to change
		app.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
		TransparencyAttributes ta =                        // make cube fully opaque when start
				new TransparencyAttributes(TransparencyAttributes.NICEST, 0f);
		app.setTransparencyAttributes(ta);

		colorCube.setAppearance(app);                      // set appearance for the cub
		
		moveTG.addChild(colorCube);                        // make the cube moving	
		
		// New: Added collision behavior to the cube --------------------------------------------------
		// Used CollisionDetectShape but I edited it and made it CollisionDetectShapeL8 to accommodate this lab requirements
		CollisionDetectShapeL8 collision = new CollisionDetectShapeL8(colorCube);
		collision.setSchedulingBounds(CommonsLH.twenty_BS);        // detect column's collision
		moveTG.addChild(collision);								  // Adding collision to moveTG
		// -----------------------------------------------------
		
		return moveTG;
	}
	
	/* a function to build and return the content branch */
	private static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();

		TransformGroup sceneTG = new TransformGroup();     // introduce a TransformGroup for rotation 
		sceneBG.addChild(CommonsLH.rotate_Behavior(7500, sceneTG));

		CodeLab7LH.rotate_Side(sceneBG, sceneTG, alpha);   // make the two flipping (side) surfaces 
		CommonsLH.control_Rotation(r_tag);                 // make 'sceneTG' rotating by default
		sceneBG.addChild(sceneTG);
		
		Alpha cubeAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE |
				Alpha.DECREASING_ENABLE, 0, 0, 3000, 0000, 750, 3000, 0000, 750);
		sceneBG.addChild(move_Cube(cubeAlpha));            // add a moving ColorCube with collision detection
		
				
		return sceneBG;  
	}

	/* a constructor to set up for the application */
	public CodeLab8LH(BranchGroup scene) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
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
		frame.getContentPane().add(new CodeLab8LH(create_Scene()));
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
				alpha[0].pause() ; 
				alpha[1].resume() ; 
			}
			else if (cases[cIndex].equals("Stop Back"))
				alpha[1].pause() ; 
			else if (cases[cIndex].equals("Resume Front"))
				alpha[0].resume() ; 
			else if (cases[cIndex].equals("Resume Back"))
				alpha[1].resume() ; 

			cIndex = (cIndex == 3) ? 0 : cIndex + 1; // ------------------------------------
		default:
			return;
		}
	}	
}
