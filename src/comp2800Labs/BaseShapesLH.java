package comp2800Labs;

/* Copyright material for students working on assignments */

import java.awt.Font;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;  // to create the tower 
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.vecmath.*;

public abstract class BaseShapesLH {
	protected TransformGroup objTG = new TransformGroup(); // use 'objTG' to position an object

	protected abstract Node create_Object();               // allow derived classes to create different objects
	
	public TransformGroup position_Object() {	           // retrieve 'objTG' to which 'obj_shape' is attached
		return objTG;   
	}
	
	protected Appearance app;                              // allow each object to define its own appearance
	protected Appearance app2 ; // another app2 added by lynn, I needed this to create the blader 
	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // A3: attach the next transformGroup to 'objTG'
	}
}

class SquareShape extends BaseShapesLH {
	public SquareShape() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(0.0, -0.54, 0));
		objTG = new TransformGroup(translator);            // down half of the tower and base's heights

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = CommonsLH.set_Appearance(CommonsLH.White);   // set the appearance for the base
		return new Box(0.5f, 0.04f, 0.5f, Primitive.GENERATE_NORMALS, app);
	}
}

/* a derived class to create a string label and place it to the bottom of the self-made cone */
/*class ColorString extends BaseShapesLH {
	private String str;
	private Color3f clr;
	private double scl;
	private Point3f pos;                                           // make the label adjustable with parameters
	public ColorString(String str_ltrs, Color3f str_clr, double s, Point3f p) {
		str = str_ltrs;	
		clr = str_clr;
		scl = s;
		pos = p;

		Transform3D scaler = new Transform3D();
		scaler.setScale(scl);                              // scaling 4x4 matrix 
		Transform3D rotator = new Transform3D();           // 4x4 matrix for rotation
		rotator.rotY(Math.PI);
		Transform3D trfm = new Transform3D();              // 4x4 matrix for composition
		trfm.mul(rotator);                                 // apply rotation second
		trfm.mul(scaler);                                  // apply scaling first
		objTG = new TransformGroup(trfm);                  // set the combined transformation
		objTG.addChild(create_Object());                   // attach the object to 'objTG'		
	}
	
	protected Node create_Object() {
		Font my2DFont = new Font("Arial", Font.PLAIN, 1);  // font's name, style, size
		FontExtrusion myExtrude = new FontExtrusion();
		Font3D font3D = new Font3D(my2DFont, myExtrude);	
		Text3D text3D = new Text3D(font3D, str, pos);      // create 'text3D' for 'str' at position of 'pos'
		
		Appearance app = CommonsLH.set_Appearance(clr);    // use appearance to specify the string color
		return new Shape3D(text3D, app);                   // return a string label with the appearance
	}
}
*/

//Added by LYNN: class to create the cylinder ( tower )
class CylinderShape extends BaseShapesLH {
	public CylinderShape() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(0.0, 0 , 0));  // set y to 0 to lift the cylinder up 
		objTG = new TransformGroup(translator);            // down half of the tower and base's heights

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = CommonsLH.set_Appearance(CommonsLH.Orange);   // set the appearance for the base
		// To create a cylinder we should use this :
		//Cylinder(float radius, float height, int primflags, int xdivisions, int ydivisions, Appearance appearance)
		return new Cylinder(0.12f, 1.0f, Primitive.GENERATE_NORMALS ,30, 30, app);
	}
}

// Added by LYNN: To create s sphere
class SphereShape extends BaseShapesLH {
	public SphereShape() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(0.0,0.5, 0));
		objTG = new TransformGroup(translator);           

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = CommonsLH.set_Appearance(CommonsLH.Red);   
		//To do as sphere:
		//Sphere sphere = new Sphere(float radius, int primflags, int divisions, Appearance appearance);
		return new Sphere(0.12f , Primitive.GENERATE_NORMALS ,30,app);
	}
}

//Added by Lynn to create a smaller cyan box 
class CyanBox extends BaseShapesLH {
	public CyanBox() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(-0.15, 0.68, 0));
		objTG = new TransformGroup(translator);            

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = CommonsLH.set_Appearance(CommonsLH.Cyan);   
		return new Box(0.26f, 0.06f, 0.12f, Primitive.GENERATE_NORMALS, app);
	}
}

//Steps to do the bladers shape
//--------------------------------
class Blades extends BaseShapesLH {
	public Blades() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(-0.33, 0.01, 0));
		objTG = new TransformGroup(translator);            

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = CommonsLH.set_Appearance(CommonsLH.Magenta);  // the blades box appearance 
		app2 = CommonsLH.set_Appearance(CommonsLH.Red);     /// the blades sphere appearance
		
		Group combinedGroup = new Group();  // this object will handle both the sphere and the box
		
		Box box = new Box(0.01f, 0.06f, 0.5f, Primitive.GENERATE_NORMALS, app); // create the box
		Sphere sphere = new Sphere(0.06f , Primitive.GENERATE_NORMALS ,30,app2);  // create the sphere 
		
		Transform3D sphereTransform = new Transform3D();   // make a transformation for the sphere to change it's position relative to the box
        sphereTransform.setTranslation(new Vector3d(0.06, 0.0, 0.0)); // moving the sphere 0.05 t the right of x relative to the box
        
        TransformGroup sphereTG = new TransformGroup(sphereTransform);
        sphereTG.addChild(sphere); // Add the sphere to its TransformGroup
        
        combinedGroup.addChild(box);
        combinedGroup.addChild(sphereTG);
        
        return combinedGroup ; // return the combined objTG made out of the box and the sphere 
	}
}

//The function resposible for creating the 3d string, added by LYNN
//--------------------------------------------------------------------------
class ColorString extends BaseShapesLH {
	private String str;
	private Color3f clr;
	private double scl;
	private Point3f pos;                                           // make the label adjustable with parameters
	public ColorString(String str_ltrs, Color3f str_clr, double s, Point3f p) {
		str = str_ltrs;	
		clr = str_clr;
		scl = s;
		pos = p;

		Transform3D scaler = new Transform3D();
		scaler.setScale(scl);                              // scaling 4x4 matrix 
		//Transform3D rotator = new Transform3D();           // 4x4 matrix for rotation
		//rotator.rotY(Math.PI);
		Transform3D trfm = new Transform3D();              // 4x4 matrix for composition
		//trfm.mul(rotator);                                 // apply rotation second
		trfm.mul(scaler);                                  // apply scaling first
		objTG = new TransformGroup(trfm);                  // set the combined transformation
		objTG.addChild(create_Object());                   // attach the object to 'objTG'		
	}
	
	protected Node create_Object() {
		Font my2DFont = new Font("Arial", Font.PLAIN, 1);  // font's name, style, size
		FontExtrusion myExtrude = new FontExtrusion();
		Font3D font3D = new Font3D(my2DFont, myExtrude);	
		Text3D text3D = new Text3D(font3D, str, pos);      // create 'text3D' for 'str' at position of 'pos'
		
		Appearance app = CommonsLH.set_Appearance(clr);    // use appearance to specify the string color
		return new Shape3D(text3D, app);                   // return a string label with the appearance
	}
}

// seperate class created for the  stationary coordinate system,this class will create a branch group that has the coordinate
class CoordinateSystem   {
	private BranchGroup axisGroup ;
	
    public CoordinateSystem() {
        axisGroup = new BranchGroup(); 
        axisGroup.addChild(createAxes());
    }

    public Shape3D createAxes() {

        // Create the LineArray for the axes
        LineArray axisLines = new LineArray(6, LineArray.COORDINATES | LineArray.COLOR_3);

        // Define coordinates for the axes
        // X-axis:(0, 0, 0) to (1, 0, 0)
        axisLines.setCoordinate(0, new Point3f(0.0f, 0.0f, 0.0f));
        axisLines.setCoordinate(1, new Point3f(1.0f, 0.0f, 0.0f));
        // Y-axis:(0, -1, 0) to (0, 1, 0)
        axisLines.setCoordinate(2, new Point3f(0.0f, 0.0f, 0.0f));
        axisLines.setCoordinate(3, new Point3f(0.0f, 1.0f, 0.0f));
        // Z-axis:(0, 0, -1) to (0, 0, 1)
        axisLines.setCoordinate(4, new Point3f(0.0f, 0.0f, -1.0f));
        axisLines.setCoordinate(5, new Point3f(0.0f, 0.0f, 1.0f));

        // give colores to axes 
        axisLines.setColor(0, new Color3f(1.0f, 0.0f, 0.0f)); // Red for X-axis
        axisLines.setColor(1, new Color3f(1.0f, 0.0f, 0.0f)); // Red for X-axis
        axisLines.setColor(2, new Color3f(0.0f, 1.0f, 0.0f)); // Green for Y-axis
        axisLines.setColor(3, new Color3f(0.0f, 1.0f, 0.0f)); // Green for Y-axis
        axisLines.setColor(4, new Color3f(0.0f, 0.0f, 1.0f)); // Blue for Z-axis
        axisLines.setColor(5, new Color3f(0.0f, 0.0f, 1.0f)); // Blue for Z-axis

        // Create a Shape3D object for the axes
        return new Shape3D(axisLines);
    }  
    public BranchGroup get_BranchGroup() {
    	return axisGroup ; 
    }
    
}
