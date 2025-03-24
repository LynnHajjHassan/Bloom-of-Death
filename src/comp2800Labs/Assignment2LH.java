package comp2800Labs;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;

// import new classes made for assignment 2 
import comp2800Labs.A2ShapesLH.*;

public class Assignment2LH extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static final int OBJ_NUM = 3;
	private static final int OBJ_NUM2 = 3;  // used for the new yaw and the rotating blades and the new base 


	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           // create the scene' BranchGroup
		TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup

		BaseShapesLH[] baseShapes = new BaseShapesLH[OBJ_NUM];
		RotateObject[] rotatedShapes = new RotateObject[OBJ_NUM2] ;  // allocated for the new Yaw and the rotating blades 
		String str = "LH's A2";
		
		rotatedShapes[0] = new NewYaw() ;    // creating sphere shape
		rotatedShapes[1] = new NewBlades() ;    // create the blades
		rotatedShapes[2] = new Base() ; // make the new base 
		//baseShapes[0] = new SquareShape(); removed since we created a new base for this assig
		baseShapes[0] = new CylinderShape() ; // Added by Lynn : Create a cylinder 
		baseShapes[1] = new CyanBox() ;			// create a cyan box
		baseShapes[2] = new ColorString(str, CommonsLH.White, 0.11,new Point3f(-str.length() / 4.5f, -0.3f, 1.1f));	// create text3D objTG
		
		/*for (int i = 0; i < OBJ_NUM; i++)
			sceneTG.addChild(baseShapes[i].position_Object());*/
		
		//Added by Lynn 
		TransformGroup base_objTG = rotatedShapes[2].position_Object() ; 
		TransformGroup tower_objTG = baseShapes[0].position_Object() ; 
		TransformGroup yaw_objTG = rotatedShapes[0].position_Object() ; 
		TransformGroup yaw_objRG = rotatedShapes[0].RG_Object() ; 
		TransformGroup nacelle_objTG = baseShapes[1].position_Object() ; 
		TransformGroup text3D_objTG = baseShapes[2].position_Object() ; 
		TransformGroup blades_objTG = rotatedShapes[1].position_Object() ; 
		
		sceneTG.addChild(base_objTG); // add the objTG of the base to scenceTG
		sceneTG.addChild(tower_objTG); // add objTG of the cylinder to sceneTG
		tower_objTG.addChild(yaw_objTG) ; // add the yaw drive objTG to the objTG of the tower
		nacelle_objTG.addChild(text3D_objTG) ; // adding the text3D objTG to the Nacelle objTG
		nacelle_objTG.addChild(blades_objTG) ; // adding the blades objTG to the Nacelle objTG
		yaw_objRG.addChild(nacelle_objTG) ; // adding the Nacelle objTG to the objRG of the Yaw
	
		

		sceneBG.addChild(CommonsLH.add_Lights(CommonsLH.White, 1));	
		//sceneBG.addChild(CommonsLH.rotate_Behavior(7500, sceneTG));  //Assig2 : removed this line 	
		sceneBG.addChild(sceneTG);                         // make 'sceneTG' continuous rotating

		//Added by Lynn,line 56 and 57 are for the optional part of the assignment 
		CoordinateSystem coordinateSystem = new CoordinateSystem();
		sceneBG.addChild(coordinateSystem.get_BranchGroup()); // Add the coordinate system to the main TransformGroup
		
		return sceneBG;
	}

	/* NOTE: Keep the constructor for each of the assignments */
	public Assignment2LH(BranchGroup sceneBG) {
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
		frame = new JFrame("LH's Assignment 2");            // NOTE: change XY to student's initials
		frame.getContentPane().add(new Assignment2LH(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
