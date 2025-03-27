package BOD;

import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PointLight;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Switch;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class CeilingLamp {
	
	private static BODObjects[] CLObjects = new BODObjects[6];	     // an array to store the ceiling Lamp's parts
	
	/* a function to create the desk fan */
	protected static TransformGroup create_CeilingLamp() {
		
		TransformGroup ceilingLampTG = new TransformGroup();		 // Initializing sceneTG (ceilingLampTG)
				
		
		// Create the pendantHolder of the ceiling lamp 
		CLObjects[0] = new pendantHolder(); 
		ceilingLampTG = CLObjects[0].position_Object();              // set ceilingLampTG to pendantHolder's objTG  
		
					
		// Create the pendant's glass and attach it to TLBase objTG
		CLObjects[1] = new pendantGlass(); 
		CLObjects[0].add_Child(CLObjects[1].position_Object());
		
		// Create the ceiling's Lamp bulb holder and attach it's objTG to pendantGlass objtG
		CLObjects[2] = new CLBulbHolder(); 
		CLObjects[1].add_Child(CLObjects[2].position_Object());
		
		
		
		return ceilingLampTG;
	}

}



class pendantHolder extends BODObjects{
	public pendantHolder() {
		scale = 1d;                                        	 // use to scale up/down original size
		post = new Vector3f(0f, 4f, 0f);                     // use to move object for positioning
		transform_Object("pendantHolder");                   // set transformation to 'objTG' and load object file
		create_Appearance() ;								 // Create the Full appearance of the body, including the colors, and texture
	}

	protected void create_Appearance() {	

		mtl_clr[0] = new Color3f(0.05f, 0.05f, 0.05f);  	// Very dark ambient
		mtl_clr[1] = new Color3f(0.15f, 0.15f, 0.15f);  	// Dark diffuse (not pure black)
		mtl_clr[2] = new Color3f(0.7f, 0.7f, 0.7f);    		// Bright specular - to give metallic look
		mtl_clr[3] = new Color3f(0.01f, 0.01f, 0.01f); 		// Minimal emissive
		setShininess(96.0f) ;
		obj_Appearance();								    // Applying colors on the object    	   		
	}
	
	
	public TransformGroup position_Object() {              	// attach object BranchGroup  to 'objTG'
		objTG.addChild(objBG);                            	
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                             // attach the next transformGroup to 'objTG'
	}
	
}



class pendantGlass extends BODObjects{
	public pendantGlass() {
		scale = 1d;                                        	 // use to scale up/down original size
		post = new Vector3f(0f, -1f, 0f);                  	 // use to move object for positioning
		transform_Object("pendantGlass");                    // set transformation to 'objTG' and load object file
		create_Appearance() ;								 // Create the Full appearance of the body, including the colors, and texture
	}

	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0] = new Color3f(0.15f, 0.08f, 0.04f);  // Warm dark ambient (brown base)
		mtl_clr[1] = new Color3f(0.772500f, 0.654900f, 0.000000f) ;
		mtl_clr[2] = new Color3f(0.7f, 0.6f, 0.5f);     // Warm metallic specular (slightly golden)
		mtl_clr[3] = new Color3f(0.1f, 0.05f, 0.02f);   // Subtle warm emissive
		obj_Appearance();							    // Applying colors on the object      
			
		   		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup  to 'objTG'
		objTG.addChild(objBG);                            		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
}



class CLBulbHolder extends BODObjects{
	public CLBulbHolder() {
		scale = 0.1d;                                        // use to scale up/down original size
		post = new Vector3f(0f, -0.1f, 0f);                  // use to move object for positioning
		transform_Object("CLBulbHolder");                    // set transformation to 'objTG' and load object file
		//create_Appearance() ;								 // Create the Full appearance of the body, including the colors, and texture
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
    texAttr.setTextureMode(TextureAttributes.MODULATE);
    
    // Scale the Texture if needed 
    float scl = 0.6f;
	    Transform3D transMap = new Transform3D();  			// to scale it 
    Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);							// apply scaling
    texAttr.setTextureTransform(transMap);
    
    // Set the Texture to the appearance 
    app.setTextureAttributes(texAttr);
	  app.setTexture(textured_App("TLOtherP"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup  to 'objTG'
		objTG.addChild(objBG);                            		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
}


