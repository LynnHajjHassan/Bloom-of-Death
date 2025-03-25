package CodesML2800;

/* Copyright material for students working on assignments */

import java.io.FileNotFoundException;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.*;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

public abstract class A3ObjectsML {
	private Alpha rotationAlpha;                           // NOTE: keep for future use
	protected BranchGroup objBG;                           // load external object to 'objBG'
	protected TransformGroup objTG;                        // use 'objTG' to position an object
	protected TransformGroup objRG;                        // use 'objRG' to rotate an object
	protected double scale;                                // use 'scale' to define scaling
	protected Vector3f post;                               // use 'post' to specify location
	protected Shape3D obj_shape, obj_shape2 , obj_shape3, obj_shape4, obj_shape5;

	public abstract TransformGroup position_Object();      // need to be defined in derived classes
	public abstract void add_Child(TransformGroup nextTG);
	
	public Alpha get_Alpha() { return rotationAlpha; };    // NOTE: keep for future use 
	
	public void set_Alpha(Alpha newa) { rotationAlpha = newa; };

	/* a function to load and return object shape from the file named 'obj_name' */
	private Scene loadShape(String obj_name) {
		ObjectFile f = new ObjectFile(ObjectFile.RESIZE, (float) (60 * Math.PI / 180.0));
		Scene s = null;
		try {                                              // load object's definition file to 's'
			s = f.load("images/" + obj_name + ".obj");
		} catch (FileNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (ParsingErrorException e) {
			System.err.println(e);
			System.exit(1);
		} catch (IncorrectFormatException e) {
			System.err.println(e);
			System.exit(1);
		}
		return s;                                          // return the object shape in 's'
	}
	
	/* function to set 'objTG' and attach object after loading the model from external file */
	protected void transform_Object(String obj_name) {
		Transform3D scaler = new Transform3D();
		scaler.setScale(scale);                            // set scale for the 4x4 matrix
		scaler.setTranslation(post);                       // set translations for the 4x4 matrix
		objTG = new TransformGroup(scaler);                // set the translation BG with the 4x4 matrix
		objBG = loadShape(obj_name).getSceneGroup();       // load external object to 'objBG'
		
		//fan blades
		if ( obj_name.equals("FanBlades")) {			  // Blades has multiple objects
			obj_shape = (Shape3D) objBG.getChild(0);      
			obj_shape.setName(obj_name);   
			obj_shape2 = (Shape3D) objBG.getChild(1);	  
			obj_shape3 = (Shape3D) objBG.getChild(2);	 
			obj_shape4 = (Shape3D) objBG.getChild(3);   
			obj_shape5 = (Shape3D) objBG.getChild(4);	  
		}else {
			obj_shape = (Shape3D) objBG.getChild(0);           // get and cast the object to 'obj_shape'
			obj_shape.setName(obj_name);                       // use the name to identify the object 
		}
		
		
	}
	
	protected Appearance app = new Appearance();
	private int shine = 32;                                // specify common values for object's appearance
	protected Color3f[] mtl_clr = {new Color3f(1.000000f, 1.000000f, 1.000000f),
			new Color3f(0.772500f, 0.654900f, 0.000000f),	
			new Color3f(0.175000f, 0.175000f, 0.175000f),
			new Color3f(0.000000f, 0.000000f, 0.000000f)};
	
    /* a function to define object's material and use it to set object's appearance */
	protected void obj_Appearance() {		
		Material mtl = new Material();                     // define material's attributes
		mtl.setShininess(shine);
		mtl.setAmbientColor(mtl_clr[0]);                   // use them to define different materials
		mtl.setDiffuseColor(mtl_clr[1]);
		mtl.setSpecularColor(mtl_clr[2]);
		mtl.setEmissiveColor(mtl_clr[3]);                  // use it to enlighten a button
		mtl.setLightingEnable(true);

		app.setMaterial(mtl);                              // set appearance's material
		
		//individually color every blade
		if ( obj_shape.getName().equals("FanBlades")) {	 
			obj_shape.setAppearance(app);                     
			obj_shape2.setAppearance(app);
			obj_shape3.setAppearance(app);
			obj_shape4.setAppearance(app);
			obj_shape5.setAppearance(app);
		} 
		
		if ( obj_shape.getName().equals("FanGuard")) {
			for (int i = 0; i < objBG.numChildren(); i++) {
				obj_shape =(Shape3D)objBG.getChild(i);
				obj_shape.setAppearance(app);    
		    }	
		} else { 
			obj_shape.setAppearance(app); 
		}

	}	
}

class GuardObject extends A3ObjectsML {
	public GuardObject() {
		scale = 3.3d;                                        // use to scale up/down original size
		post = new Vector3f(0f, 0f, -1.6f);                   // use to move object for positioning
		transform_Object("FanGuard");                      // set transformation to 'objTG' and load object file
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() { 
		objTG.addChild(objBG);										// Attach objBG to objTG                           
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class BladeObject extends A3ObjectsML {
	public BladeObject() {
		scale = 2.9d;                                        // use to scale up/down original size
		post = new Vector3f(0f, 0f, -1.2f);                   // use to move object for positioning
		transform_Object("FanBlades");                      // set transformation to 'objTG' and load object file
		mtl_clr[1] = new Color3f(0.58f, 0.69f, 0.11f);     // set "FanStand" to a different color than the common  		                                              
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() { 
		objRG = new TransformGroup();							
		objRG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);	// Allow objRG to have animation 	
		objRG.addChild(objBG);										// Attach objBG that has Blades object to objRG                          
		rotate(500) ;										// Apply rotation of 500 ms to the blades  	
		objTG.addChild(objRG);										// Attach objBG to objTG                           
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
	public void rotate(int rnum) {
		Transform3D axisPosition = new Transform3D();
		axisPosition.rotX(Math.PI/2);						  						               
		set_Alpha( new Alpha(-1, rnum) ) ; 
		RotationInterpolator positionInterpol = new RotationInterpolator(get_Alpha(), objRG, axisPosition, 0.0f, -(float) Math.PI * 2.0f);
		positionInterpol.setSchedulingBounds(CommonsML.twenty_BS); 
		objTG.addChild(positionInterpol);
	}
}

class MotorObject extends A3ObjectsML {
	public MotorObject() {
		scale = 3.5d;                                        // use to scale up/down original size
		post = new Vector3f(0f, 2.5f, 0f);                   // use to move object for positioning
		transform_Object("FanMotor");                      // set transformation to 'objTG' and load object file
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() { 
		objRG = new TransformGroup(); // Initialize objRG
	    objTG.addChild(objRG);
		objTG.addChild(objBG);										// Attach objBG to objTG                           
		return objTG;	                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class ShaftObject extends A3ObjectsML {
	public ShaftObject() {
		scale = 0.18d;                                        // use to scale up/down original size
		post = new Vector3f(0f, 1f, 0.60f);                   // use to move object for positioning
		transform_Object("FanShaft");                      // set transformation to 'objTG' and load object file
		mtl_clr[1] = new Color3f(CommonsML.Red);     // set "FanStand" to a different color than the common  		                                              
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {              // attach object BranchGroup "FanStand" to 'objTG'
		objRG = new TransformGroup();								// Initialize objRG of the FanShaft
		objRG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);	// Allow objRG to have animation 	
		objRG.addChild(objBG);										// Attach objBG that has Shaft object to objRG                          
		rotate() ; 											// Apply rotation 		
		objTG.addChild(objRG);										// Attach objRG to objTG                           
		return objTG;                                       
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
	//rotate
	public void rotate() {
		Transform3D axisPosition = new Transform3D();
		axisPosition.rotY(Math.PI);						  // Rotate around Y axis, rotation of 180 degree  						               
		set_Alpha( new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 5000, 2500, 200, 5000, 2500, 200) ) ; 
		RotationInterpolator positionInterpol = new RotationInterpolator(get_Alpha(), objRG, axisPosition, -(float) Math.PI/2 , (float) Math.PI/2);
		positionInterpol.setSchedulingBounds(CommonsML.twenty_BS); 
		objTG.addChild(positionInterpol);
	}
}

class StandObject extends A3ObjectsML {
	public StandObject() {
		scale = 1d;                                        // use to scale up/down original size
		post = new Vector3f(0f, 0f, 0f);                   // use to move object for positioning
		transform_Object("FanStand");                      // set transformation to 'objTG' and load object file
		mtl_clr[1] = new Color3f(0.58f, 0.69f, 0.11f);     // set "FanStand" to a different color than the common  		                                              
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {              // attach object BranchGroup "FanStand" to 'objTG'
		Transform3D r_axis = new Transform3D();            // default: rotate around Y-axis
		r_axis.rotY(Math.PI);                              // rotate around y-axis for 180 degrees
		objRG = new TransformGroup(r_axis);                // allow "FanBlades" to rotate
		objTG.addChild(objRG);                             // position "FanStand" by attaching 'objRG' to 'objTG'
		objRG.addChild(objBG);                             // rotate "FanStand" by attaching 'objBG' to 'objRG'
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class SwitchObject extends A3ObjectsML {
	public SwitchObject() {
		scale = 0.3d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.02f, -0.77f, -0.8f);         // location to connect "FanSwitch" with "FanStand"
		transform_Object("FanSwitch");                     // set transformation to 'objTG' and load object file
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class BaseShape extends A3ObjectsML {
	public BaseShape() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(0.0, -0.54, 0));
		objTG = new TransformGroup(translator);            // down half of the tower and base's heights

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = CommonsML.set_Appearance(CommonsML.White);   // set the appearance for the base
		app.setTexture(textured_App("MarbleTexture"));     // set texture for the base
		TransparencyAttributes ta =                        // value: FASTEST NICEST SCREEN_DOOR BLENDED NONE
				new TransparencyAttributes(TransparencyAttributes.SCREEN_DOOR, 0.5f);
		app.setTransparencyAttributes(ta);                 // set transparency for the base
		return new Box(0.5f, 0.04f, 0.5f, Box.GENERATE_NORMALS | Box.GENERATE_TEXTURE_COORDS, app);
	}
	
	private static Texture textured_App(String name) {
		String filename = "images/" + name + ".jpg";       // tell the folder of the image
		TextureLoader loader = new TextureLoader(filename, null);
		ImageComponent2D image = loader.getImage();        // load the image
		if (image == null)
			System.out.println("Cannot load file: " + filename);

		Texture2D texture = new Texture2D(Texture.BASE_LEVEL,
				Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);                        // set image for the texture

		return texture;
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "BaseShapeA" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "BaseShapeA" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

