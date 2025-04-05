package models;

import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.RenderingAttributes;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class Mirror {
	
	/* a function to create the desk fan */
	public static TransformGroup create_Mirror() {	
		TransformGroup mirrorTG = new TransformGroup();		
		
		
		// Creat the outer frame of the mirro
		MirrorFrame mirrorFrame = new MirrorFrame() ; 
		mirrorTG = mirrorFrame.position_Object();               	
						
		// Create the mirror's surface 
		MirrorSurface mirrorSurface = new MirrorSurface() ; 
		mirrorFrame.add_Child(mirrorSurface.position_Object());
				
		return mirrorTG;
	}

}


class MirrorFrame extends BODObjects{
	public MirrorFrame() {
		scale = 8d;                                        	// use to scale up/down original size
		post = new Vector3f(-20f, 0f, 99f);                   	// use to move object for positioning
		transform_Object("MirrorFrame");                      	// set transformation to 'objTG' and load object file
		objBG.setName("MirrorFrame");
		create_Appearance() ;								// Create the Full appearance of the body, including the colors, and texture
	}

	protected void create_Appearance() {
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
		app.setTexture(textured_App("MirrorFrame"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "LTBody" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
}



class MirrorSurface extends BODObjects{
	public MirrorSurface() {
		scale = 0.97d;                                        	// use to scale up/down original size
		post = new Vector3f(0f, 0f, 0f);                   	// use to move object for positioning
		transform_Object("MirrorSurface");                      	// set transformation to 'objTG' and load object file
		create_Appearance() ;								// Create the Full appearance of the body, including the colors, and texture
	}

	protected void create_Appearance() {
	    // More reflective material settings
	    mtl_clr[0] = new Color3f(0.02f, 0.02f, 0.02f);       // Very dark ambient
	    mtl_clr[1] = new Color3f(0.15f, 0.15f, 0.15f);       // Slightly brighter diffuse
	    mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f);          // Full white specular
	    mtl_clr[3] = new Color3f(0.5f, 0.5f, 0.5f);          // No emissive
	    setShininess(128);                                    // Maximum shininess
	    
	    obj_Appearance();
	    
	    // More reflective texture settings
	    TextureAttributes texAttr = new TextureAttributes();
	    texAttr.setTextureMode(TextureAttributes.COMBINE);
	    texAttr.setCombineRgbMode(TextureAttributes.COMBINE_REPLACE);
	    
	    // Adjust texture scaling
	    float scl = 3f;
	    Transform3D transMap = new Transform3D();
	    Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);
	    texAttr.setTextureTransform(transMap);
	    
	    app.setTextureAttributes(texAttr);
	    app.setTexture(textured_App("MirrorSurface"));
	    
	    // Reduced transparency for stronger reflections
	    TransparencyAttributes transAtt = new TransparencyAttributes();
	    transAtt.setTransparencyMode(TransparencyAttributes.BLENDED);
	    transAtt.setTransparency(0.05f);  // Very slight transparency
	    app.setTransparencyAttributes(transAtt);
	    
	    // Enable two-sided lighting 
	    PolygonAttributes polyAttrs = new PolygonAttributes();
	    polyAttrs.setCullFace(PolygonAttributes.CULL_NONE);
	    polyAttrs.setPolygonMode(PolygonAttributes.POLYGON_FILL);
	    app.setPolygonAttributes(polyAttrs);
	    
	    // Additional rendering attributes for better reflectivity
	    RenderingAttributes renderAttrs = new RenderingAttributes();
	    renderAttrs.setIgnoreVertexColors(true);
	    renderAttrs.setDepthBufferEnable(true);
	    renderAttrs.setAlphaTestValue(0.1f);
	    app.setRenderingAttributes(renderAttrs);
	}
	
	
	public TransformGroup position_Object() {              
		objTG.addChild(objBG);                            
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
}

