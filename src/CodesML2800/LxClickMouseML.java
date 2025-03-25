package CodesML2800;

/* Copyright material by xyuan@uwindsor.ca,
 * for students working on assignments and projects */

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

public class LxClickMouseML extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static TransformGroup sceneTG;
	private Canvas3D canvas3D;                             // need for mouse picking
	private static PickTool pickTool;	

	/* a function to create and return the scene BranchGroup */
	private static BranchGroup setup_Scene() {
		BranchGroup sceneBG = new BranchGroup();		   // create 'sceneBG' as content branch
		sceneTG = new TransformGroup();                    // make 'sceneTG' changeable
		sceneBG.addChild(CommonsML.rotate_Behavior(7500, sceneTG));
		sceneTG.setCapability(Node.ENABLE_PICK_REPORTING); // need for mouse picking

		Appearance app = CommonsML.set_Appearance(CommonsML.Green);
		Box box = new Box(0.5f, 0.5f, 0.5f,                // create an appearance-modifiable box
				Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, app);
		box.setUserData(0);                                // 'UserData' retrievable at picking
		box.setName("box");                                // NOTE: 'Name' is also retrievabl		

		sceneTG.addChild(box);                             // attach 'box' to 'sceneTG'		
		sceneBG.addChild(sceneTG);                         // add 'sceneTG' to 'sceneBG'
		
		pickTool = new PickTool(sceneBG);                  // make object(s) in 'sceneBG' pickable
		pickTool.setMode(PickTool.GEOMETRY);               // set to pick by geometry

		return sceneBG;
	}
	
	/* a specialized constructor that enables sound and (mouse-based and keyboard-based) interaction  */
	public LxClickMouseML(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas3D = new Canvas3D(config);	
		canvas3D.addMouseListener(this);                   // NOTE: enable mouse clicking 
				
		SimpleUniverse su = new SimpleUniverse(canvas3D);  // create a SimpleUniverse		
		CommonsML.define_Viewer(su, new Point3d(1, 1, 4)); // position the viewer

		sceneBG.addChild(CommonsML.add_Lights(CommonsML.White, 2));
		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach 'sceneBG' to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas3D);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}

	/* the main entrance of the application with specified window dimension */
	public static void main(String[] args) {
		frame = new JFrame("Mouse Clicking Test");            // create an instance of the class
		frame.getContentPane().add(new LxClickMouseML(setup_Scene()));  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	

	@Override
	public void mouseClicked(MouseEvent event) {
		int x = event.getX(); int y = event.getY();        // mouse coordinates
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

		if (pickTool.pickClosest() != null) {
			PickResult pickResult = pickTool.pickClosest();// obtain the closest hit
			Box box = (Box)pickResult.getNode(PickResult.PRIMITIVE);
			Appearance app = new Appearance();             // originally a PRIMITIVE as a box
			if ((int) box.getUserData() == 0) {            // retrieve 'UserData'
				app = CommonsML.set_Appearance(CommonsML.Red);
				box.setUserData(1);                        // set 'UserData' to a new value
			}
			else {                                         // use 'UserData' as flag to switch color
				app = CommonsML.set_Appearance(CommonsML.Green);
				box.setUserData(0);                        // reset 'UserData'
			}
			box.setAppearance(app);                        // change box's appearance
		} 	
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
