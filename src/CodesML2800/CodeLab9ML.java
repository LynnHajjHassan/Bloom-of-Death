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

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.Switch;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

public class CodeLab9ML extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	private static String frame_name = "ML's Lab #9";
	private static boolean r_tag = true;
	private static boolean object_tag = true;
	private static int select_key = 0;
	private static final String OBJECT_NAME = "Morphing Object";
	private static Alpha[] diskAlpha = new Alpha[2];
	private static Switch[] morphSwitch = new Switch[3];
	private static String[] cases = {"Stop Front", "Stop Back" ,"Resume Front", "Resume Back"};
	private static int c = 0;
	
	/* a function to create a switch of morphing behavior for (disk) side with different edges;
	 * the two switch children are two morphing objects, one with changing edges 4<>-64<->4 and
	 * the other with changing edges 4<->64<->8. */
	private static Switch morph_Switch(int i, TransformGroup priorTG, String s, Switch[] morph) {
		int n0 = 4, n1 = 16, n2 = 8;
		morph[i] = new Switch();
		morph[i].setCapability(Switch.ALLOW_SWITCH_WRITE);
		for (int j = 0; j < 4; j++)   {                     // add two morphing surfaces to switch 'morph[i]'
			morph[i].addChild(L9MorphShapeML.set_Morph(priorTG, s, n0, n1, n2));
			n0 = n0  * 2 ; 
			n2 = n2  * 2 ;  
			if ( n1 == 64 ) { 					
				n1 = 4 ;
			} else {
				n1 = n1  * 2 ; 
			}	
		}
		
		n0 = 4 ; 
		n1 = 16 ; 
		n2 = 8 ;
		
		
		morph[i].setWhichChild(0);                         // set default with changing edges 4<>-64<->8

		return morph[i];
	}
	
	/* a function to the disk's two side surface rotating  */
	public static void morph_Shapes(TransformGroup sceneTG, Alpha[] alpha, Switch[] morph) {
		String[] side_name = {"Top", "Side", "Bottom"};
		Transform3D side, slide, slide2, plate, plate2, rotate_axis, rotate2;
		TransformGroup sideTG, slideTG, slide2TG, plateTG, plate2TG, hingeTG, hinge2TG;
		RotationInterpolator rotationInterpol, rotat;
		
		slide = new Transform3D();
		slide.setTranslation(new Vector3f(-2.0f, 0, -0.1f));
		slideTG = new TransformGroup(slide);
		
		slide2 = new Transform3D();
		slide2.setTranslation(new Vector3f(-2.0f, 0, -0.1f));
		slide2TG = new TransformGroup(slide2);

		plate = new Transform3D();                         // shift the disk's far end to rotational origin
		plate.setTranslation(new Vector3f(2.0f, 0, 0));			
		plateTG = new TransformGroup(plate);               // need 'plateTG' to position disk for rotation
		plateTG.addChild(morph_Switch(0, plateTG, side_name[0], morph));
		
		plate2 = new Transform3D();                         // shift the disk's far end to rotational origin
		plate2.setTranslation(new Vector3f(2.0f, 0, 0.1f));			
		plate2TG = new TransformGroup(plate2);               // need 'plateTG' to position disk for rotation
		plate2TG.addChild(morph_Switch(2, plate2TG, side_name[2], morph));
		
		hingeTG = new TransformGroup();                    // use 'hingeTG' for rotation
		hingeTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotate_axis = new Transform3D();                   // rotate around 'hingeTG's y-axis
		alpha[0] = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 4000, 0000, 1000, 4000, 0000, 1000);
		rotationInterpol = new RotationInterpolator(alpha[0], hingeTG, rotate_axis, 0, -(float)(Math.PI / 2.0));               // rotate for +/-90 degrees
		rotationInterpol.setSchedulingBounds(CommonsML.twenty_BS);
		sceneTG.addChild(rotationInterpol);                // add rotation behavior to 'morphBG'
		hingeTG.addChild(plateTG);                         // attach translated disk for rotation
		
		hinge2TG = new TransformGroup();                    // use 'hingeTG' for rotation
		hinge2TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotate2 = new Transform3D();
		alpha[1] = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 4000, 0000, 1000, 4000, 0000, 1000);
		rotat = new RotationInterpolator(alpha[1], hinge2TG, rotate2, 0, (float) (Math.PI / 2.0)); 
		rotat.setSchedulingBounds(CommonsML.twenty_BS);
		slide2TG.addChild(rotat);
		hinge2TG.addChild(plate2TG);
		slide2TG.addChild(hinge2TG);
		
		side = new Transform3D();
		side.setTranslation(new Vector3f(0, 0, 0.1f));
		sideTG = new TransformGroup(side);                 // need 'sideTG' to move the two disks in place;
		sideTG.addChild(hingeTG);                          // adjust the rotating disk's height
		slideTG.addChild(sideTG);			               // center the adjusted rotating disk
		
		sceneTG.addChild(slideTG);                         // attach the non-rotating disk side
		sceneTG.addChild(slide2TG);
		sceneTG.addChild(morph_Switch(1, sceneTG, side_name[1], morph));
		
	}
	
	/* a function to build and return the content branch */
	private static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup(); 
		
		TransformGroup sceneTG = new TransformGroup();     // introduce a TransformGroup for rotation 
		sceneBG.addChild(CommonsML.rotate_Behavior(7500, sceneTG));
		
		morph_Shapes(sceneTG, diskAlpha, morphSwitch);
		
		CommonsML.control_Rotation(r_tag);                 // make 'sceneTG' rotating by default
		sceneBG.addChild(sceneTG);
		Alpha cubeAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE |
				Alpha.DECREASING_ENABLE, 0, 0, 3000, 0000, 750, 3000, 0000, 750);
		sceneBG.addChild(CodeLab8ML.move_Cube(cubeAlpha));

		return sceneBG;
	}

	/* a constructor to set up for the application */
	public CodeLab9ML(BranchGroup scene) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
		canvas3D.addKeyListener(this);                     // NOTE: enable key events 	
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
		frame = new JFrame(frame_name + ": Rotating Textured Disks");
		frame.getContentPane().add(new CodeLab9ML(create_Scene()));
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
		case OBJECT_NAME:
frame.setTitle(frame_name + ": "+ cases[c] );
			
			if (cases[c].equals("Stop Front")) {
				diskAlpha[0].pause() ; 
				diskAlpha[1].resume() ; 
			}
			else if (cases[c].equals("Stop Back"))
				diskAlpha[1].pause() ; 
			else if (cases[c].equals("Resume Front"))
				diskAlpha[0].resume() ; 
			else if (cases[c].equals("Resume Back"))
				diskAlpha[1].resume() ; 

			c = (c == 3) ? 0 : c + 1;
			break;
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
			morphSwitch[i].setWhichChild(k);               // use the selected switch child

	}

	public void keyReleased(KeyEvent arg0) { }
	public void keyTyped(KeyEvent e) { }	
}
