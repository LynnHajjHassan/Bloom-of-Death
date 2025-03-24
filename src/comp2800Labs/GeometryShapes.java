package comp2800Labs;

/* For students to study and work on assignments and projects *
 * \\\\\ Copyright material (contact xyuan@uwindsor.ca) ///// */

import java.awt.GraphicsConfiguration;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

/* This is the super class of this group of demo examples. While the derived classes 
 * set individual 'shapeBG', the return of a BranchGroup can be detached at runtime */
public class GeometryShapes {
	protected static SimpleUniverse simpleU = null;
	protected BranchGroup shapeBG = new BranchGroup();
	
	public static SimpleUniverse simple_Universe() {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);

		canvas3D.setSize(800, 800);                        // set size of canvas

		simpleU = new SimpleUniverse(canvas3D);                // create a simple universe
		CommonsLH.define_Viewer(simpleU, new Point3d(1.35, -0.35, 2.0)); 		

		return simpleU;
	}
	
	/* a function to return 'shapeBG' as a BranchGroup */
	public BranchGroup get_ShapeBG() {
		shapeBG.setCapability(BranchGroup.ALLOW_DETACH);
		return shapeBG;
	}	
	
	protected TransformGroup scale_TransformGroup(float scale) {
		Transform3D scaleTF = new Transform3D();        // 4x4 matrix for translation
		scaleTF.setScale(scale);
		TransformGroup comp_TG = new TransformGroup(scaleTF);
		return comp_TG;
	}
	
	/* a function to set the appearance of polygons ('t'==true: polylines only) */
	protected static Appearance polygon_Appearance(Color3f clr, boolean t) {
		Appearance app = new Appearance();
		PolygonAttributes pa = new PolygonAttributes();
		if (t == true)
			pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);  // show only polylines
		pa.setCullFace(PolygonAttributes.CULL_NONE);       // show both sides
		app.setPolygonAttributes(pa);
		ColoringAttributes ca = new ColoringAttributes(clr,
				ColoringAttributes.FASTEST);
		app.setColoringAttributes(ca);

		return app;
	}
}