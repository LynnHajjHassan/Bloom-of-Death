package models;

import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3d;

public class Corpse {
	
    private static BODObjects[] corpseParts = new BODObjects[1];

    public static TransformGroup create_Corpse() {
        TransformGroup corpseTG = new TransformGroup();
        
        // Create main human body and attach
        corpseParts[0] = new CorpseMain();
        //((CorpseMain)corpseParts[0]).registerForTweaking();	// UNCOMMENT ME TO CONTROL REAL TIME MATERIAL ADJUSTMENT BEFORE HARDCODED LINES IN create_Appearance
        corpseTG = corpseParts[0].position_Object();
     
        return corpseTG;
    }
}

/* Corpse main body as a BODObjects part */
class CorpseMain extends BODObjects {

	public CorpseMain() {
	    scale = 14d;
	    post = new Vector3f(15.00f, -15.98f, 14.0f);
	    transform_Object("corpse");

	     // Collision bounds setup
	    BoundingBox bounds = new BoundingBox(
	        new Point3d(-1.5, -1.5, -1.5),   // Adjust size depending on model
	        new Point3d(1.5, 1.5, 1.5)
	    );
	    objTG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
	    objTG.setPickable(true);
	    objTG.setBounds(bounds);

	    //obj_Appearance(); // CONTROL REAL TIME MATERIAL ADJUSTMENT 
	    create_Appearance();
	}

    protected void create_Appearance() {
    	mtl_clr[0] = new Color3f(0f, 0f, 0f); // Ambient color — overall soft light the object "receives" from the environment.
    	mtl_clr[1] = new Color3f(1.0f, 1.0f, 1.0f); // Diffuse color — how the object reflects light when directly lit.
    	mtl_clr[2] = new Color3f(0.03f, 0.03f, 0.03f); // Specular color — the shininess/gloss highlights.
    	mtl_clr[3] = new Color3f(0.0f, 0.0f, 0.0f); // Emissive color — self-glow of the object. 
    	setShininess(31.0f); // Shininess (float) — how concentrated or broad the specular highlight is.		mtl DOES NOT EXIST IN MAIN CODE
        obj_Appearance();

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);
        app.setTexture(textured_App("corpse"));
        
        //updateAppearance();						//UNCOMMENT ME FOR REAL TIME MATERIAL ADJUSTMENT
    }

    public TransformGroup position_Object() {
        objTG.addChild(objBG);                        // attach loaded corpse branch
        return objTG;
    }

    public void add_Child(TransformGroup nextTG) {
        objTG.addChild(nextTG);						  // attach additional parts if needed
    }
}