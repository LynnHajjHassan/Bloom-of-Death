package CodesML2800;

/* Copyright material for students working on assignments */

import java.awt.Font;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.vecmath.*;

public abstract class BaseShapesML {
	protected TransformGroup objTG = new TransformGroup(); // use 'objTG' to position an object

	protected abstract Node create_Object();               // allow derived classes to create different objects
	
	public TransformGroup position_Object() {	           // retrieve 'objTG' to which 'obj_shape' is attached
		return objTG;   
	}
	
	protected Appearance app;                              // allow each object to define its own appearance
	protected Appearance app2;
	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // A3: attach the next transformGroup to 'objTG'
	}
}

class SquareShape extends BaseShapesML {
	public SquareShape() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(0.0, -0.54, 0));
		objTG = new TransformGroup(translator);            // down half of the tower and base's heights

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = CommonsML.set_Appearance(CommonsML.White);   // set the appearance for the base
		return new Box(0.5f, 0.04f, 0.5f, Primitive.GENERATE_NORMALS, app);
	}
}

class CylinderShape extends BaseShapesML {
	public CylinderShape() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(0.0, 0, 0));
		objTG = new TransformGroup(translator);            // down half of the tower and base's heights

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = CommonsML.set_Appearance(CommonsML.Orange);   // set the appearance for the base
		return new Cylinder(0.12f, 1.0f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 30, 30, app);
	}
}

class SphereShape extends BaseShapesML {
	public SphereShape() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(0.0, 0.5, 0));
		objTG = new TransformGroup(translator);            // down half of the tower and base's heights

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = CommonsML.set_Appearance(CommonsML.Red);   // set the appearance for the base
		return new Sphere(0.12f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 30, app);
	}
}

class SquareShape2 extends BaseShapesML {
	public SquareShape2() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(0.12, 0.67, 0));
		objTG = new TransformGroup(translator);            // down half of the tower and base's heights

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = CommonsML.set_Appearance(CommonsML.Cyan);   // set the appearance for the base
		return new Box(0.26f, 0.06f, 0.12f, Primitive.GENERATE_NORMALS, app);
	}
}

class RotorBlades extends BaseShapesML {
    public RotorBlades() {
        TransformGroup sphereTG = new TransformGroup();
        Transform3D sphereTrans = new Transform3D();
        sphereTrans.setTranslation(new Vector3d(0.38, 0.67, 0.0)); // Position the rotor sphere
        sphereTG.setTransform(sphereTrans);

        Appearance sphereApp = CommonsML.set_Appearance(CommonsML.Red);
        Sphere rotorSphere = new Sphere(0.06f, Primitive.GENERATE_NORMALS, sphereApp);
        sphereTG.addChild(rotorSphere);

        TransformGroup bladeTG = new TransformGroup();
        Transform3D bladeTrans = new Transform3D();
        bladeTrans.setTranslation(new Vector3d(0.44, 0.67, 0)); // Position the blade
        bladeTG.setTransform(bladeTrans);

        Appearance bladeApp = CommonsML.set_Appearance(CommonsML.Magenta);
        Box rotorBlade = new Box(0.01f, 0.06f , 0.5f, Primitive.GENERATE_NORMALS, bladeApp);
        bladeTG.addChild(rotorBlade);

        objTG.addChild(sphereTG);
        objTG.addChild(bladeTG);
    }

    protected Node create_Object() {
        return objTG;
    }
}

class CoordinateSystem extends BaseShapesML {
	private BranchGroup axisGroup ;
	
    public CoordinateSystem() {
    	axisGroup = new BranchGroup();
        objTG.addChild(create_Object());
    }
    
    protected Node create_Object() {
        TransformGroup coordTG = new TransformGroup();

        // X-axis (Red)
        Appearance xApp = new Appearance();
        ColoringAttributes xColor = new ColoringAttributes(CommonsML.Red, ColoringAttributes.SHADE_FLAT);
        xApp.setColoringAttributes(xColor);
        LineArray xLine = new LineArray(2, LineArray.COORDINATES);
        xLine.setCoordinate(0, new Point3f(-1.0f, 0.0f, 0.0f));
        xLine.setCoordinate(1, new Point3f(1.0f, 0.0f, 0.0f));
        Shape3D xAxis = new Shape3D(xLine, xApp);
        coordTG.addChild(xAxis);

        // Y-axis (Green)
        Appearance yApp = new Appearance();
        ColoringAttributes yColor = new ColoringAttributes(CommonsML.Green, ColoringAttributes.SHADE_FLAT);
        yApp.setColoringAttributes(yColor);
        LineArray yLine = new LineArray(2, LineArray.COORDINATES);
        yLine.setCoordinate(0, new Point3f(0.0f, -1.0f, 0.0f));
        yLine.setCoordinate(1, new Point3f(0.0f, 1.0f, 0.0f));
        Shape3D yAxis = new Shape3D(yLine, yApp);
        coordTG.addChild(yAxis);

        // Z-axis (Blue)
        Appearance zApp = new Appearance();
        ColoringAttributes zColor = new ColoringAttributes(CommonsML.Blue, ColoringAttributes.SHADE_FLAT);
        zApp.setColoringAttributes(zColor);
        LineArray zLine = new LineArray(2, LineArray.COORDINATES);
        zLine.setCoordinate(0, new Point3f(0.0f, 0.0f, -1.0f));
        zLine.setCoordinate(1, new Point3f(0.0f, 0.0f, 1.0f));
        Shape3D zAxis = new Shape3D(zLine, zApp);
        coordTG.addChild(zAxis);

        return coordTG;
    }
    
    public BranchGroup get_BranchGroup() {
    	return axisGroup ; 
    }
}

/* a derived class to create a string label and place it to the bottom of the self-made cone */
class ColorString extends BaseShapesML {
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
		
		Appearance app = CommonsML.set_Appearance(clr);    // use appearance to specify the string color
		return new Shape3D(text3D, app);                   // return a string label with the appearance
	}
}


