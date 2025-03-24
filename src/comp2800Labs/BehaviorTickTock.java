package comp2800Labs;

/* For students to study and work on assignments and projects *
 * \\\\\ Copyright material (contact xyuan@uwindsor.ca) ///// */

import org.jdesktop.j3d.examples.collision.Box;
import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.ColoringAttributes;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.*;

public class BehaviorTickTock extends GeometryShapes {
	private TransformGroup create_Column(double scale, Vector3d pos) {
		Transform3D transM = new Transform3D();
		transM.set(scale, pos);                            // Create base TG with 'scale' and 'position'
		TransformGroup baseTG = new TransformGroup(transM);

		Shape3D shape = new Box(0.5, 5.0, 1.0);
		baseTG.addChild(shape);                            // Create a column as a box and add to 'baseTG'

		Appearance app = shape.getAppearance();
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(0.6f, 0.3f, 0.0f);                     // set column's color and make changeable
		app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		app.setColoringAttributes(ca);

		CollisionDetectShape cd = new CollisionDetectShape(shape);
		cd.setSchedulingBounds(CommonsLH.twenty_BS);        // detect column's collision

		baseTG.addChild(cd);                               // add column with behavior of CollisionDector
		return baseTG;
	}

	private TransformGroup create_Box() {
		TransformGroup transfmTG[] = new TransformGroup[2];
		for (int i = 0; i < 2; i++) {                      // two TGs: 0-self and 1-orbit
			transfmTG[i] = new TransformGroup();
			transfmTG[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		}

		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3d(0.0, -0.6, 0.0));
		trans.setScale(0.12f);
		TransformGroup transCube = new TransformGroup(trans);
		
		transCube.addChild(GeoIndexedQuad.index_QuadShape(3f)); // new ColorCube()
		transfmTG[0].addChild(transCube);                  // add a unit cube to 3rd TG

		Transform3D yAxis1 = new Transform3D();
		yAxis1.rotX(Math.PI / 2.0);                        // define animation along orbit
		yAxis1.setTranslation(new Vector3d(0.0, -0.20, 0.0));
		Alpha alphaOrbit = new Alpha(-1, Alpha.INCREASING_ENABLE |
				Alpha.DECREASING_ENABLE, 0, 0, 5000, 2500, 200,	5000, 2500, 200);
		RotationInterpolator tickTock = new RotationInterpolator(alphaOrbit,
				transfmTG[1], yAxis1, -(float)Math.PI/ 2.0f, (float)Math.PI/ 2.0f);
		tickTock.setSchedulingBounds(CommonsLH.hundred_BS);
		transfmTG[1].addChild(tickTock);                   // add orbit animation to scene graph

		Transform3D yAxis2 = new Transform3D();
		Alpha alphaSelf = new Alpha(-1, Alpha.INCREASING_ENABLE,
				0, 0, 4000, 0, 0, 0, 0, 0);                // define self-rotating animation
		RotationInterpolator rotatorSelf = new RotationInterpolator(alphaSelf,
				transfmTG[0], yAxis2, 0.0f,	(float) Math.PI * 2.0f);
		rotatorSelf.setSchedulingBounds(CommonsLH.hundred_BS);
		transfmTG[0].addChild(rotatorSelf);
		transfmTG[1].addChild(transfmTG[0]);               // add self-rotation to orbit

		return transfmTG[1];
	}
	
	public BehaviorTickTock() {
		Vector3d[] pos = {new Vector3d(-0.52, 0.0, -0.20),
				new Vector3d(0.52, 0.0, -0.20)};           // shift columns backwards off x-axis 
		for (int i = 0; i < 2; i++)
			shapeBG.addChild(create_Column(0.12, pos[i]));
		shapeBG.addChild(create_Box());
	}
}
