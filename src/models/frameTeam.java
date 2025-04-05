package models;
import java.io.FileNotFoundException;

import org.jogamp.java3d.*;
import org.jogamp.java3d.Clip;
import org.jogamp.java3d.loaders.*;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

import audio.SoundUtilityJOAL;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public abstract class frameTeam {
	private Alpha rotationAlpha;                           // NOTE: keep for future use
	protected BranchGroup objBG;                           // load external object to 'objBG'
	protected TransformGroup objTG;                        // use 'objTG' to position an object
	protected TransformGroup objRG;                        // use 'objRG' to rotate an object
	protected double scale;                                // use 'scale' to define scaling
	protected Vector3f post;                               // use 'post' to specify location
	protected Shape3D obj_shape, obj_shape2 , obj_shape3, obj_shape4, obj_shape5;	//i am here adding the extra objects shapes for the FanBlades and the front side of the cylinder connecting them
	public abstract TransformGroup position_Object();      // need to be defined in derived classes
	public abstract void add_Child(TransformGroup nextTG);
	
	private static String snd_bk = "clockTicking";                // specify the name of the background sound
	private static SoundUtilityJOAL soundJOAL;             // need for playing sound

	
	public Alpha get_Alpha() { return rotationAlpha; };    // NOTE: keep for future use 
	public void setAlpha(Alpha newAlpha) {				   
		rotationAlpha = newAlpha ;	//here ia am adding the set up vslue for the alpha when i use them
	}
	//a function to load and return object shape from the file named 'obj_name'
	private Scene loadShape(String obj_name) {
		ObjectFile f = new ObjectFile(ObjectFile.RESIZE, (float) (60 * Math.PI / 180.0));
		Scene s = null;
		try {                                              // load object's definition file to 's'
			s = f.load("objects/" + obj_name + ".obj");
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
	
	//function to set 'objTG' and attach object after loading the model from external file
	protected void transform_Object(String obj_name) {
	
	    
		Transform3D scaler = new Transform3D();
		scaler.setScale(scale);                            // set scale for the 4x4 matrix
		scaler.setTranslation(post);                       // set translations for the 4x4 matrix
		objTG = new TransformGroup(scaler);                // set the translation BG with the 4x4 matrix
		objBG = loadShape(obj_name).getSceneGroup();       // load external object to 'objBG'
		
											   
		obj_shape = (Shape3D) objBG.getChild(0);// extract only one object if we are not dealing with the Blades and here i am getting and cast the object to 'obj_shape'
		obj_shape.setName(obj_name);// use the name to identify the object 
			
	}//end of the method

	protected Appearance app = new Appearance();
	private int shine = 6000;                                // specify common values for object's appearance
	protected Color3f[] mtl_clr = {new Color3f(1.000000f, 1.000000f, 1.000000f),
			new Color3f(0.772500f, 0.654900f, 0.000000f),	
			new Color3f(0.175000f, 0.175000f, 0.175000f),
			new Color3f(0.000000f, 0.000000f, 0.000000f)};
	
    
	
	protected void obj_Appearance(String texturePath) {
	    // Load the texture image
	    TextureLoader textureLoader = new TextureLoader(texturePath, null);
	    Texture texture = textureLoader.getTexture();

	    // Define texture attributes
	    TextureAttributes textureAttributes = new TextureAttributes();
	    textureAttributes.setTextureMode(TextureAttributes.MODULATE);

	    // Define material's attributes with more controlled values
	    Material mtl = new Material();
	    mtl.setShininess(32.0f); // Reduced from 6000 to a more reasonable value
	    
	    // Set material colors with less extreme values
	    mtl.setAmbientColor(0.2f, 0.2f, 0.2f);
	    mtl.setDiffuseColor(0.8f, 0.8f, 0.8f);
	    mtl.setSpecularColor(0.3f, 0.3f, 0.3f); // Reduced specular intensity
	    mtl.setEmissiveColor(0.0f, 0.0f, 0.0f); // No emissive glow
	    
	    mtl.setLightingEnable(true);

	    // Set appearance's material and texture
	    app.setMaterial(mtl);
	    app.setTexture(texture);
	    app.setTextureAttributes(textureAttributes);

	    obj_shape.setAppearance(app);
	}

	
	
}//the end of the main code



class frame extends frameTeam {
	public frame() {
		scale = 1d;                                        // use to scale up/down original size
		post = new Vector3f(0f, 1.25f, 0.02f);                   // use to move object for positioning
		transform_Object("TeamFrame");                      // set transformation to 'objTG' and load object file
		//mtl_clr[1] = new Color3f(-0.58f, 0.69f, -0.11f);     // set "FanStand" to a different color than the common  		                                              
		obj_Appearance("textures/MyTeam1.jpg");                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {              // attach object BranchGroup "FanStand" to 'objTG'
		Transform3D r_axis = new Transform3D();            // default: rotate around Y-axis
		//r_axis.rotY(Math.PI);        
		objRG = new TransformGroup(r_axis);                // allow "FanBlades" to rotate
		objTG.addChild(objRG);                            // position "FanStand" by attaching 'objRG' to 'objTG'

		objRG.addChild(objBG);                             // rotate "FanStand" by attaching 'objBG' to 'objRG'
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}
