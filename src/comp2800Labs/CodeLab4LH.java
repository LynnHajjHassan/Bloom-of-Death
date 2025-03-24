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

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

public class CodeLab4LH extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	private static String frame_name = "LH's Lab #4";
	private static boolean r_tag = true;
	private static final String OBJECT_NAME = "Disk";
	private static TransformGroup sideTG , sideTG2 ; 
	private static int gap_tag = 1;
	
	public static float[] gapsArray = { 0, 0.25f , 0.5f, 0.75f } ;  // added by lynn, this array will helps identify how much the gap between sufaces will be
	public static int gaps_index = 1 ; // an index to transfer throughout the array 
	
	/* a function to build and return the content branch */
	private static BranchGroup create_Scene() {

		BranchGroup sceneBG = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup();     // introduce a TransformGroup for rotation 
	                                                       // specify color for the two sides of a disk
		Color3f[] side_color = {CommonsLH.Orange, CommonsLH.Green , CommonsLH.Purple};  // I added a a color for the new surface here 
		                                                   // create and attach the (stationary) side surface
		sceneTG.addChild(L4TransformLH.ring_Side(1, side_color[1]));

		sideTG = new TransformGroup();                     // make the (top) flat surface translatable
		sideTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sideTG.addChild(L4TransformLH.ring_Side(0, side_color[0]));
		sceneTG.addChild(sideTG);
		
		//making the bottom flat surface 
		sideTG2 = new TransformGroup();                     
		sideTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sideTG2.addChild(L4TransformLH.ring_Side(2, side_color[2]));  // 2 indicating that we need to generate the bottom surface 
		sceneTG.addChild(sideTG2);	 // adding the bottom surface sideTG2 to the sceneTG
		
		sceneBG.addChild(CommonsLH.rotate_Behavior(7500, sceneTG));
		CommonsLH.control_Rotation(r_tag);                 // make 'alterableBG' rotating by default
		sceneBG.addChild(sceneTG);
		
		return sceneBG;  
	}

	/* a constructor to set up for the application */
	public CodeLab4LH(BranchGroup scene) {
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

	public static void main(String[] args) {
		frame = new JFrame(frame_name + ": No Gap");       // NOTE: copyright material
		frame.getContentPane().add(new CodeLab4LH(create_Scene()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	

	@Override
	public void actionPerformed(ActionEvent e) {
		String chosen_item = e.getActionCommand();		

		frame.setTitle(frame_name + ": " + chosen_item);
		switch(chosen_item) {                              // handle different menu items
		case "Exit": 
			System.exit(0);                                // quit the application
		case "Pause/Rotate":
			r_tag = (r_tag == true)? false : true;
			CommonsLH.control_Rotation(r_tag);             // alter between rotation and pause
			return;
		case OBJECT_NAME:
			//gap_tag = (gap_tag == 0)? 1: 0;                // alter between gap and no gap
			if (gapsArray[gaps_index]==0 ) frame.setTitle(frame_name +": No Gap");  
			else frame.setTitle(frame_name +": "+ gapsArray[gaps_index] +" Gap");
						
			// Transformation part for the top surface 
			Transform3D gapTF = new Transform3D();         // 4x4 matrix for translation
			gapTF.setTranslation(new Vector3f(0f, 0f, (float)( gapsArray[gaps_index] * gap_tag)));
			sideTG.setTransform(gapTF);                    // update 'sideTG' to move the surface
			
			//Transformation part for the bottom surface 
			Transform3D gapTF2 = new Transform3D();         // 4x4 matrix for translation
			gapTF2.setTranslation(new Vector3f(0f, 0f, (float)( -(gapsArray[gaps_index]) * gap_tag)));  //z value is negative since we need to push the bottom surface backwards to create the gap
			sideTG2.setTransform(gapTF2);  
			
			if ( gaps_index == 3) {  gaps_index = 0 ;} // if we reached the size 0.75 in the array, set the index to 0(the start again)
			else { 
				 gaps_index++ ; // if we did not reach the end of the array yet, keep increment the index, accessing other gaps  
			}
			
			break;
		default:
			return;
		}
	}	
}