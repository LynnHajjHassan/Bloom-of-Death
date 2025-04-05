package models;

import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class GarbageCan {
	
	private static BODObjects[] TLObjects = new BODObjects[2];
	
	public static TransformGroup create_GarbageCan() {
		TransformGroup TrashCanTG = new TransformGroup();
		
		TLObjects[0] = new CanBase();
		TrashCanTG = TLObjects[0].position_Object();
		
		TLObjects[1] = new Trash();
		TLObjects[0].add_Child(TLObjects[1].position_Object());
		
		return TrashCanTG;
	}
}


class CanBase extends BODObjects {
	public CanBase() {
		scale = 3.5d;                                        	// use to scale up/down original size
		post = new Vector3f(30f, -15f, 90f);                   // use to move object for positioning
		transform_Object("Trash_Can");                      	// set transformation to 'objTG' and load object file
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
	  app.setTexture(textured_App("matte-black"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "PillowBase" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class Trash extends BODObjects {
	public Trash() {
		scale = 0.3d;                                        	// use to scale up/down original size
		post = new Vector3f(0f, -0.5f, 0.5f);                   // use to move object for positioning
		transform_Object("crumple_paper");                      	// set transformation to 'objTG' and load object file
		
	    // Set up collision bounding box
	    BoundingBox bounds = new BoundingBox(
	        new Point3d(-1.2, -1.2, -1.2),  // tweak these bounds as needed for better fit
	        new Point3d(1.2, 1.2, 1.2)
	    );
	    objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objTG.setCapability(Node.ENABLE_PICK_REPORTING);
	    objTG.setPickable(true);
	    objTG.setBounds(bounds);
		
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
	  app.setTexture(textured_App("paper"));     		
	}
	
	public TransformGroup position_Object() {              // attach object BranchGroup "PillowBase" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}