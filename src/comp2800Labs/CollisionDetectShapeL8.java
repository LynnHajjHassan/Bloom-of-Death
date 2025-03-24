package comp2800Labs;

/* For students to study and work on assignments and projects *
 * \\\\\ Copyright material (contact xyuan@uwindsor.ca) ///// */

import java.util.Iterator;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.ColoringAttributes;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnCollisionEntry;
import org.jogamp.java3d.WakeupOnCollisionExit;
import org.jogamp.vecmath.Color3f;

public class CollisionDetectShapeL8 extends Behavior {
	private boolean inCollision;
	private Shape3D shape;
	//private ColoringAttributes shapeColoring;		// New: Commented 
	private Appearance shapeAppearance;
	private WakeupOnCollisionEntry wEnter;
	private WakeupOnCollisionExit wExit;
	
	private TransparencyAttributes shapeTransparency; // New 


	public CollisionDetectShapeL8(Shape3D s) {
		shape = s;                                         // save the original color of 'shape"
		shapeAppearance = shape.getAppearance();
		shapeTransparency = shapeAppearance.getTransparencyAttributes();	// New
		//shapeColoring = shapeAppearance.getColoringAttributes();			// New: Commented 
		inCollision = false;
	}

	@Override
	public void initialize() {                             // USE_GEOMETRY USE_BOUNDS
		wEnter = new WakeupOnCollisionEntry(shape, WakeupOnCollisionEntry.USE_GEOMETRY);
		wExit = new WakeupOnCollisionExit(shape, WakeupOnCollisionExit.USE_GEOMETRY);
		wakeupOn(wEnter);                                  // initialize the behavior
	}

	@Override
	public void processStimulus(Iterator<WakeupCriterion> criteria) {
		TransparencyAttributes ta =                        // make cube fully opaque when start
				new TransparencyAttributes(TransparencyAttributes.NICEST, 0.5f);
		
		
		inCollision = !inCollision;                        // collision has taken place

		if (inCollision) {                                 // change color to highlight 'shape'
			shapeAppearance.setTransparencyAttributes(ta);

			wakeupOn(wExit);                               // keep the color until no collision
		} else {                                           // change color back to its original
			shapeAppearance.setTransparencyAttributes(shapeTransparency);
			wakeupOn(wEnter);                              // wait for collision happens
		}
	}
}