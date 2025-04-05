package models;
// Copyright material for students working on assignments
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

 public class frameMain extends JPanel {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static final int OBJ_NUM = 20;
	private static frameTeam[] object3D = new frameTeam[OBJ_NUM];

	public static TransformGroup create_FrameHS() {
		
		Transform3D scale = new Transform3D();
	    scale.setScale(1.3); // Scale up 1.5x

	    Transform3D rotation = new Transform3D();
	    rotation.setRotation(new AxisAngle4d(0, 1, 0, 0)); // Rotate 45° around Y-axis

	    Transform3D translation = new Transform3D();
	    translation.setTranslation(new Vector3f(-28.0f, -4.9f, 1.9f)); // Move to (x=2, y=1, z=-3)

	    // Combine transforms: scale → rotate → translate
	    Transform3D combined = new Transform3D();
	    combined.mul(scale);         // combined = scale
	    combined.mul(rotation);      // combined = scale * rotation
	    combined.mul(translation);   // combined = scale * rotation * translation

	    // Apply to root TransformGroup
	    TransformGroup rootTG = new TransformGroup(combined);
	    rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);		
		
		
		
	    TransformGroup fanTG = new TransformGroup();

	    // Create and attach ClockBase
	    object3D[0] = new frame();
	    fanTG = object3D[0].position_Object();  // Set 'fanTG' to ClockBase's 'objTG	   
	    
	    rootTG.addChild(fanTG);

	    return rootTG;
	}
	

	
	
	public static BranchGroup create_Scene() {	//a function to build the content branch, including the fan and other environmental settings
		BranchGroup sceneBG = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup();	   // make 'sceneTG' continuously rotating
		
	//	sceneTG.addChild(CommonsHS.rotate_Behavior(7500, sceneTG));
		sceneTG.addChild(create_FrameHS());                    // add the fan to the rotating 'sceneTG'
		sceneBG.addChild(sceneTG);                         // keep the following stationary
		
		
	//	sceneBG.addChild(CommonsHS.add_Lights(CommonsHS.White, 1));
		
		return sceneBG;
	}

	public frameMain(BranchGroup sceneBG) {//NOTE: Keep the constructor for each of the labs and assignments
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		Commons.define_Viewer(su, new Point3d(0.25d, 0.25d, 10.0d));   // set the viewer's location
		
		//CommonsHS.define_Viewer(su, new Point3d(1.25d, -1.5d, 3.0d));   // set the viewer's location from down

		
		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse
		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
		
	}
	
	
	public static void main(String[] args) {

		frame = new JFrame("HS's Assignment");                   // NOTE: change XY to student's initials
		frame.getContentPane().add(new frameMain(create_Scene()));  // start the program
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}