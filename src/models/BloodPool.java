package models;

import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class BloodPool {
	
	private static BODObjects blood;
	
	public static TransformGroup create_BloodPool() {
		TransformGroup BloodTG = new TransformGroup();
		
		blood = new BloodPoolObj();
		BloodTG = blood.position_Object();
		
		return BloodTG;
	}
}


class BloodPoolObj extends BODObjects {
	public BloodPoolObj() {
		scale = 15d;                                        	// use to scale up/down original size
		post = new Vector3f(17f, -17.5f, 18f);                   // use to move object for positioning
		transform_Object("BloodPool");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(0.5f, 0.5f, 0.5f) ;
		mtl_clr[3] = new Color3f(0.0f, 0.0f, 0.0f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 0.6f;
	    Transform3D transMap = new Transform3D();  			// to scale it 
      Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);							// apply scaling
      texAttr.setTextureTransform(transMap);
      
      // Set the Texture to the appearance 
      app.setTextureAttributes(texAttr);
	  app.setTexture(textured_App("bloodpool"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "PillowBase" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}