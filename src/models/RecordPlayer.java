package models;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Matrix3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class RecordPlayer extends JPanel {
	
	private static final long serialVersionUID = 1L;
//	private static JFrame frame;
//	private static String snd_bk = "Music";
//	private static SoundUtilityJOAL soundJOAL;
	
	private static final int OBJ_NUM = 9;
	private static BODObjects[] TLObjects = new BODObjects[OBJ_NUM];
	
	public static TransformGroup create_Player() {
		
		TransformGroup RecordPlayerTG = new TransformGroup(); // Main TransformGroup for the knife
	    
	    TLObjects[0] = new RecordBase();
	    TransformGroup Base = TLObjects[0].position_Object();
	    
	    TLObjects[1] = new Pivot();
	    TLObjects[0].add_Child(TLObjects[1].position_Object());
	    
	    TLObjects[2] = new Platter();
	    TLObjects[0].add_Child(TLObjects[2].position_Object());
	    
	    TLObjects[3] = new Rarm();
	    TLObjects[1].add_Child(TLObjects[3].position_Object());
	    
	    TLObjects[4] = new Rweight();
	    TLObjects[3].add_Child(TLObjects[4].position_Object());
	    
	    TLObjects[5] = new Rspeed();
	    TLObjects[6] = new Rcut();
	    TLObjects[7] = new Rcue();
	    TLObjects[0].add_Child(TLObjects[5].position_Object());
	    TLObjects[0].add_Child(TLObjects[6].position_Object());
	    TLObjects[0].add_Child(TLObjects[7].position_Object());
	    
	    TLObjects[8] = new Record();
	    TLObjects[0].add_Child(TLObjects[8].position_Object());
	    
	    //turn sideways
	 // Apply a 90-degree rotation around the Y-axis
	    Transform3D rotation = new Transform3D();
	    rotation.rotY(Math.PI / 2); // Rotate 90 degrees (π/2 radians)
	    
	    TransformGroup rotateTG = new TransformGroup();
	    rotateTG.setTransform(rotation);
	    rotateTG.addChild(Base); // Add the knife to the rotated TransformGroup
	    
	    RecordPlayerTG.addChild(rotateTG); // Add the rotated TransformGroup to the main TG
	    
	    return RecordPlayerTG;
	}
}

class Record extends BODObjects {
	private Alpha rotationAlpha;
    private RotationInterpolator rotator;
    private TransformGroup rotationTG;
	
	public Alpha get_Alpha() { return rotationAlpha; }
	public void setAlpha(Alpha newAlpha) { this.rotationAlpha = newAlpha; }
	
	public Record() {
		scale = 0.7d;                                        	// use to scale up/down original size
		post = new Vector3f(-0.23f, 0.05f, 0f);                   // use to move object for positioning
		transform_Object("record");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(0.5f, 0.5f, 0.5f) ;
		mtl_clr[3] = new Color3f(0.4f, 0.4f, 0.4f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 3f;
	    Transform3D transMap = new Transform3D();  			// to scale it 
      Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);							// apply scaling
      texAttr.setTextureTransform(transMap);
      
      // Set the Texture to the appearance 
      app.setTextureAttributes(texAttr);
	  app.setTexture(textured_App("record"));     		
	}
	
	
	public TransformGroup position_Object() {
        objRG = new TransformGroup();
        objRG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // Allow rotation

        objRG.addChild(objBG); // Attach the hour hand to the rotation group
        applySpin( 5000 ); // Rotate every 12 hours (43200 seconds * 1000 ms)
        objTG.addChild(objRG); // Attach the rotation group to the transform group
        return objTG;
    }
	
	private void applySpin(int rduration) {
		Transform3D axisPosition = new Transform3D();
        axisPosition.rotY(-Math.PI / 2); // Rotate clockwise around the Z-axis
        setAlpha(new Alpha(-1, rduration)); // Set rotation duration
        RotationInterpolator rotationInterpolator = new RotationInterpolator(
            get_Alpha(), objRG, axisPosition, 0.0f, (float) Math.PI * 2.0f // Negative angle for clockwise rotation
        );
        rotationInterpolator.setSchedulingBounds(Commons.twenty_BS);
        objTG.addChild(rotationInterpolator); 
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class RecordBase extends BODObjects {
	public RecordBase() {
		scale = 4d;                                        	// use to scale up/down original size
		post = new Vector3f(-10f, -5.0f, -37f);                   // use to move object for positioning
		transform_Object("Rbase");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(0.5f, 0.5f, 0.5f) ;
		mtl_clr[3] = new Color3f(0.4f, 0.4f, 0.4f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 3f;
	    Transform3D transMap = new Transform3D();  			// to scale it 
      Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);							// apply scaling
      texAttr.setTextureTransform(transMap);
      
      // Set the Texture to the appearance 
      app.setTextureAttributes(texAttr);
	  app.setTexture(textured_App("silver"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "PillowBase" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class Pivot extends BODObjects {
	public Pivot() {
		scale = 0.3d;                                        	// use to scale up/down original size
		post = new Vector3f(0.48f, 0.45f, -1.74f);                   // use to move object for positioning
		transform_Object("Rpivot");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
		
		Transform3D scaleTransform = new Transform3D();
        scaleTransform.setScale(scale); // Apply scale
        
		// Apply 45-degree rotation neg
        Transform3D rotationTransform = new Transform3D();
        rotationTransform.rotY(-Math.PI / 4); // Rotate 45 degrees

        scaleTransform.mul(rotationTransform);
        
        //apply postition
        Transform3D translationTransform = new Transform3D();
        translationTransform.setTranslation(post);
        
        scaleTransform.mul(translationTransform);
        
        //apply scale
        TransformGroup transformTG = new TransformGroup();
        transformTG.setTransform(scaleTransform);
        transformTG.addChild(objTG);

        objTG = transformTG; // Replace objTG with the rotated one
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(0.5f, 0.5f, 0.5f) ;
		mtl_clr[3] = new Color3f(1f, 1f, 1f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 3f;
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

class Rarm extends BODObjects {
	public Rarm() {
		scale = 2d;                                        	// use to scale up/down original size
		post = new Vector3f(0.1f, -0.01f, 0.35f);                   // use to move object for positioning
		transform_Object("Rarm");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(0.5f, 0.5f, 0.5f) ;
		mtl_clr[3] = new Color3f(1f, 1f, 1f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 3f;
	    Transform3D transMap = new Transform3D();  			// to scale it 
      Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);							// apply scaling
      texAttr.setTextureTransform(transMap);
      
      // Set the Texture to the appearance 
      app.setTextureAttributes(texAttr);
	  app.setTexture(textured_App("silver"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "PillowBase" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class Rweight extends BODObjects {
	public Rweight() {
		scale = 0.6d;                                        	// use to scale up/down original size
		post = new Vector3f(-0.045f, 0f, -0.22f);                // use to move object for positioning
		transform_Object("Rweight");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(0.5f, 0.5f, 0.5f) ;
		mtl_clr[3] = new Color3f(1f, 1f, 1f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 3f;
	    Transform3D transMap = new Transform3D();  			// to scale it 
      Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);							// apply scaling
      texAttr.setTextureTransform(transMap);
      
      // Set the Texture to the appearance 
      app.setTextureAttributes(texAttr);
	  app.setTexture(textured_App("silver"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "PillowBase" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class Platter extends BODObjects {
	public Platter() {
		scale = 0.72d;                                        	// use to scale up/down original size
		post = new Vector3f(-0.23f, 0f, 0f);                   // use to move object for positioning
		transform_Object("Rplatter");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(0.5f, 0.5f, 0.5f) ;
		mtl_clr[3] = new Color3f(1f, 1f, 1f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 3f;
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

class Rspeed extends BODObjects {
	public Rspeed() {
		scale = 0.4d;                                        	// use to scale up/down original size
		post = new Vector3f(-0.5f, -0.01f, 0.81f);                   // use to move object for positioning
		transform_Object("Rspeed");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(0.5f, 0.5f, 0.5f) ;
		mtl_clr[3] = new Color3f(1f, 1f, 1f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 3f;
	    Transform3D transMap = new Transform3D();  			// to scale it 
      Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);							// apply scaling
      texAttr.setTextureTransform(transMap);
      
      // Set the Texture to the appearance 
      app.setTextureAttributes(texAttr);
	  app.setTexture(textured_App("red-button"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "PillowBase" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class Rcue extends BODObjects {
	public Rcue() {
		scale = 0.1d;                                        	// use to scale up/down original size
		post = new Vector3f(0.41f, -0.01f, 0.81f);                   // use to move object for positioning
		transform_Object("Rcue");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(0.5f, 0.5f, 0.5f) ;
		mtl_clr[3] = new Color3f(1f, 1f, 1f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 3f;
	    Transform3D transMap = new Transform3D();  			// to scale it 
      Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);							// apply scaling
      texAttr.setTextureTransform(transMap);
      
      // Set the Texture to the appearance 
      app.setTextureAttributes(texAttr);
	  app.setTexture(textured_App("red-button"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "PillowBase" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class Rcut extends BODObjects {
	public Rcut() {
		scale = 0.1d;                                        	// use to scale up/down original size
		post = new Vector3f(0.74f, -0.01f, 0.81f);                   // use to move object for positioning
		transform_Object("Rcut");                      	// set transformation to 'objTG' and load object file
		create_Appearance();								// Create the Full appearance of the body, including the colors, and texture
	}
	
	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(0.5f, 0.5f, 0.5f) ;
		mtl_clr[3] = new Color3f(1f, 1f, 1f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 3f;
	    Transform3D transMap = new Transform3D();  			// to scale it 
      Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);							// apply scaling
      texAttr.setTextureTransform(transMap);
      
      // Set the Texture to the appearance 
      app.setTextureAttributes(texAttr);
	  app.setTexture(textured_App("red-button"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "PillowBase" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}