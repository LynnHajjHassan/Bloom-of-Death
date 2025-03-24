package comp2800Labs;

/* Copyright material for students working on assignments and projects */

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
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3d;

public class CodeLab3LH extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	private static String frame_name = "LH's Lab #3";
	private static BranchGroup alterableBG, shapeBG;
	private static GroupObjects groupObject;
	private static boolean r_tag = true;
	private static boolean object_tag = true;
	private static final String OBJECT_NAME = "Disk";
	
	//New addition 
	private static float[] radiusArray = { 1.5f , 1.0f, 0.5f } ; //an array that cointains all disk sizes
	private int index = 0 ; // an index for the array
	
	/* a function to build and return the content branch */
	private static BranchGroup create_Scene() {
		alterableBG = new BranchGroup();                   // allow 'alterableBG' to change children
		groupObject = new GroupObjects(L3DiskLH.ring_Side(2.0f)); 
		shapeBG = groupObject.get_ShapeBG();               // get the BranchGroup with a partial disk
		
		return GroupObjects.scene_Group(alterableBG, shapeBG);
	}

	/* a constructor to set up for the application */
	public CodeLab3LH(BranchGroup scene) {
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

	public static void main(String[] args) {
		frame = new JFrame(frame_name + ": Disk Size: 2.0");         // NOTE: copyright material
		frame.getContentPane().add(new CodeLab3LH(create_Scene()));
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
			if (object_tag) {                              // create a small (partial) disk
					groupObject = new GroupObjects(L3DiskLH.ring_Side(radiusArray[index]));
					BranchGroup tmpBG = groupObject.get_ShapeBG();     // save the new (shape) group
					shapeBG.detach();                                  // detach the previous shape
					shapeBG = tmpBG;
					shapeBG.setCapability(BranchGroup.ALLOW_DETACH);   // make the new shape detachable
					alterableBG.addChild(shapeBG);    
					frame.setTitle(frame_name + ": " +" Disk Size: "+ radiusArray[index]) ;     // changing the frame name once "Circle" is selected
					index++ ; // increment the index to access the next radius next time	
				if( index == 3) { // if the smallest size of the disk was reached in the array 
					index = 0 ; // reset the index 
					object_tag = false;
				}				
			}
			else {                                         
				groupObject = new GroupObjects(L3DiskLH.ring_Side(2.0f)); // restore the original size of the coin
				frame.setTitle(frame_name + ": " +" Disk Size: 2.0") ;
				object_tag = true;
			}
			break;
		default:
			return;
		}
		
		BranchGroup tmpBG = groupObject.get_ShapeBG();     // save the new (shape) group
		shapeBG.detach();                                  // detach the previous shape
		shapeBG = tmpBG;
		shapeBG.setCapability(BranchGroup.ALLOW_DETACH);   // make the new shape detachable
		alterableBG.addChild(shapeBG);                     // update 'alterableBG'
	}	
}