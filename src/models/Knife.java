package models;

import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class Knife {
	
	private static BODObjects[] TLObjects = new BODObjects[2];
	
	public static TransformGroup create_Knife() {
		TransformGroup knifeTG = new TransformGroup(); // Main TransformGroup for the knife
	    
	    TLObjects[0] = new Handle();  // Handle object
	    TransformGroup handleTG = TLObjects[0].position_Object();
	    
	    TLObjects[1] = new Blade();   // Blade object
	    TLObjects[0].add_Child(TLObjects[1].position_Object());
	    
	    // Apply a 90-degree rotation around the Y-axis
	    Transform3D rotation = new Transform3D();
	    rotation.rotX(Math.PI / 2); // Rotate 90 degrees (π/2 radians)
	    
	    TransformGroup rotateTG = new TransformGroup();
	    rotateTG.setTransform(rotation);
	    rotateTG.addChild(handleTG); // Add the knife to the rotated TransformGroup
	    
	    knifeTG.addChild(rotateTG); // Add the rotated TransformGroup to the main TG
	    
	    return knifeTG;
	}
}


class Blade extends BODObjects {
	public Blade() {
		scale = 2d;                                        	// use to scale up/down original size
		post = new Vector3f(-1.65f, 0.2f, 0f);                   // use to move object for positioning
		transform_Object("blade");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f) ;
		mtl_clr[3] = new Color3f(1.0f, 1.0f, 1.0f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 0.1f;
	    Transform3D transMap = new Transform3D();  			// to scale it 
      Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);							// apply scaling
      texAttr.setTextureTransform(transMap);
      
      // Set the Texture to the appearance 
      app.setTextureAttributes(texAttr);
	  app.setTexture(textured_App("blood_metal"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "PillowBase" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class Handle extends BODObjects {
	public Handle() {
		scale = 1d;                                        	// use to scale up/down original size
		post = new Vector3f(20f, 24f, 17.5f);                   // main object use to move for positioning
		transform_Object("handle");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f) ;
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
	  app.setTexture(textured_App("handle"));     		
	}
	
	public TransformGroup position_Object() {              // attach object BranchGroup "PillowBase" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}