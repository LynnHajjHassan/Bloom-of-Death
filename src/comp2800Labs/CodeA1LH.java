package comp2800Labs;

/* Copyright material for students working on assignments */

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class CodeA1LH extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static final int OBJ_NUM = 6;

	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           // create the scene' BranchGroup
		TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup

		BaseShapesLH[] baseShapes = new BaseShapesLH[OBJ_NUM];
		baseShapes[0] = new SquareShape();
		String str = "LH's A1";
		/*baseShapes[1] = new ColorString(str, CommonsLH.Green, 0.1, 
				new Point3f(-str.length() / 4f, -5.75f, 5.0f));*/
		
		baseShapes[1] = new CylinderShape() ; // Added by Lynn : Create a cylinder 
		baseShapes[2] = new SphereShape() ;    // creating sphere shape
		baseShapes[3] = new CyanBox() ;			// create a cyan box
		baseShapes[4] = new Blades() ;    // create the blades
		baseShapes[5] = new ColorString(str, CommonsLH.White, 0.11,new Point3f(-str.length() / 4.5f, -0.3f, 1.1f));	// create text3D objTG
		
		/*for (int i = 0; i < OBJ_NUM; i++)
			sceneTG.addChild(baseShapes[i].position_Object());*/
		
		//Added by Lynn 
		sceneTG.addChild(baseShapes[0].position_Object()); // add the objTG of the base to scenceTG
		sceneTG.addChild(baseShapes[1].position_Object()); // add objTG of the cylinder to sceneTG
		(baseShapes[1].position_Object()).addChild(baseShapes[2].position_Object()); // add the yaw drive transfrormation group to the objTG of the tower 
		(baseShapes[2].position_Object()).addChild(baseShapes[3].position_Object());  // add the Nacelle transformation object to the objTG of the yaw drive 
		(baseShapes[3].position_Object()).addChild(baseShapes[4].position_Object());  // adding the blades objTG to the Nacelle objTG
		(baseShapes[3].position_Object()).addChild(baseShapes[5].position_Object());  // adding the text3D objTG to te Nacelle objTG

		

		sceneBG.addChild(CommonsLH.add_Lights(CommonsLH.White, 1));	
		sceneBG.addChild(CommonsLH.rotate_Behavior(7500, sceneTG));	
		sceneBG.addChild(sceneTG);                         // make 'sceneTG' continuous rotating

		//Added by Lynn,line 56 and 57 are for the optional part of the assignment 
		CoordinateSystem coordinateSystem = new CoordinateSystem();
		sceneBG.addChild(coordinateSystem.get_BranchGroup()); // Add the coordinate system to the main TransformGroup
		
		return sceneBG;
	}

	/* NOTE: Keep the constructor for each of the assignments */
	public CodeA1LH(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		CommonsLH.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));

		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame("LH's Assignment 1");            // NOTE: change XY to student's initials
		frame.getContentPane().add(new CodeA1LH(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
