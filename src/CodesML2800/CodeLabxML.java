package CodesML2800;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Background;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.Switch;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

public class CodeLabxML extends JPanel implements ActionListener, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	private static String frame_name = "ML's Lab #10";
	private static boolean r_tag = true;
	private static boolean object_tag = true;
	private static int select_key = 0;
	private static final String OBJECT_NAME = "Picking Object";
	private static Alpha[] alpha = new Alpha[2];
	private static Switch[] morphSwitch = new Switch[3];

	private Canvas3D canvas3D;                             // need for mouse picking
	private static PickTool pickTool;
	
	private static String[] cases = {"Stop Front", "Stop Back" ,"Resume Front", "Resume Back" } ;
	private static int c = 0 ;  
	private static Alpha cubeAlpha ;
	private static boolean cubeIsMoving = true ;
	
	/* a function to build and return the content branch */
	private static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup(); 
		
		TransformGroup sceneTG = new TransformGroup();     // introduce a TransformGroup for rotation 
		sceneBG.addChild(CommonsML.rotate_Behavior(7500, sceneTG));
		                                                   // add switchable morphing object
		CodeLab9ML.morph_Shapes(sceneTG, alpha, morphSwitch);
		
		CommonsML.control_Rotation(r_tag);                 // make 'sceneTG' rotating by default
		sceneBG.addChild(sceneTG);
		
		BranchGroup cubeBG = new BranchGroup();            // add a moving cube
		cubeAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE |
				Alpha.DECREASING_ENABLE, 0, 0, 3000, 0000, 750, 3000, 0000, 750);
		cubeBG.addChild(CodeLab8ML.move_Cube(cubeAlpha));
		pickTool = new PickTool( cubeBG );                 // initialize 'pickTool' and allow 'cubeBG' pickable
		pickTool.setMode(PickTool.GEOMETRY);               // set to pick by geometry

		sceneBG.addChild(cubeBG);
		
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE) ;
		sceneBG.addChild(createBackground(new Color3f(0.0f, 0.5f, 1.0f), bounds)) ;

		return sceneBG;
	}

	/* a constructor to set up for the application */
	public CodeLabxML(BranchGroup scene) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas3D = new Canvas3D(config);
		canvas3D.addKeyListener(this);                     // NOTE: enable key events 	
		canvas3D.addMouseListener(this);                   // NOTE: enable mouse picking 	
		canvas3D.setSize(800, 800);                        // set size of canvas
		SimpleUniverse su = new SimpleUniverse(canvas3D);  // create a SimpleUniverse
		                                                   // set the viewer's location
		CommonsML.define_Viewer(su, new Point3d(1.35, -0.35, 12.0)); 		
		scene.addChild(CommonsML.add_Lights(CommonsML.White, 1));
		
		scene.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(scene);                          // attach 'scene' to 'su'

		Menu m = new Menu("Menu");                         // set menu's label
		m.addActionListener(this);
		MenuBar menuBar = CodeLab2ML.build_MenuBar(m, OBJECT_NAME);
		frame.setMenuBar(menuBar);                         // build and set the menu bar

		setLayout(new BorderLayout());
		add("Center", canvas3D);
		frame.setSize(810, 800);                           // set the size of the frame
		frame.setVisible(true);
	}

	public static void main(String[] args) {               // NOTE: copyright material
		frame = new JFrame(frame_name + ": Picking ColorCube");
		frame.getContentPane().add(new CodeLabxML(create_Scene()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {                     // handle the selected menu item
		case "Exit": 
			System.exit(0);                                // quit the application
		case "Pause/Rotate":
			r_tag = (r_tag == true)? false : true;
			CommonsML.control_Rotation(r_tag);
			return;
		case OBJECT_NAME:					//  New ------------------------------------			
			frame.setTitle(frame_name + ": "+ cases[c] );
			
			if (cases[c].equals("Stop Front")) {
				alpha[0].pause() ; 
				alpha[1].resume() ; 
			}
			else if (cases[c].equals("Stop Back"))
				alpha[1].pause() ; 
			else if (cases[c].equals("Resume Front"))
				alpha[0].resume() ; 
			else if (cases[c].equals("Resume Back"))
				alpha[1].resume() ; 

			c = (c == 3) ? 0 : c + 1; // ------------------------------------
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
		case KeyEvent.VK_3:
			k = 2; break;
		case KeyEvent.VK_4:
			k = 3; break;
		default: 
			return;                                        // accept only the specific keys as inputs
		}
		
		if (select_key == k)
			return;                                        // make the change only when different from current
		else
			select_key = k;                                // update the selected switch child
		
		for (int i = 0; i < 3; i++)
			morphSwitch[i].setWhichChild(k);
	}

	public void keyReleased(KeyEvent arg0) { }
	public void keyTyped(KeyEvent e) { }

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX(); int y = e.getY();        // mouse coordinates
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
			PickResult pickResult = pickTool.pickClosest();
			Box box = (Box)pickResult.getNode(PickResult.PRIMITIVE);
			Appearance app = new Appearance(); 
			if (cubeIsMoving) {	 
				cubeAlpha.pause() ;	
				frame.setTitle(frame_name + ": Stopping ColorCube" );
			}
			else {	
				cubeAlpha.resume();
				frame.setTitle(frame_name + ": Moving ColorCube" );

			}
			cubeIsMoving = !cubeIsMoving ;	
		}else {
			frame.setTitle(frame_name + ": Missed Clicking ColorCube" );
		}
	}
	
	private static Background createBackground( Color3f clr, BoundingSphere bounds){
		Background bg = new Background();
		bg.setImage(new TextureLoader("images/back.jpg", null).getImage());
		bg.setImageScaleMode(Background.SCALE_FIT_MAX);
		bg.setApplicationBounds(bounds);
		bg.setColor(clr);
		return bg;
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}	
}
