package models;

import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3d;

public class Pillow {
	
    private static BODObjects[] pillowParts = new BODObjects[1];

    public static TransformGroup create_Pillow() {
        TransformGroup pillowTG = new TransformGroup();
        
        // Create main pillow body and attach
        pillowParts[0] = new PillowMain();
        //((PillowMain)pillowParts[0]).registerForTweaking();	// UNCOMMENT ME TO CONTROL REAL TIME MATERIAL ADJUSTMENT BEFORE HARDCODED LINES IN create_Appearance
        pillowTG = pillowParts[0].position_Object();
        
        return pillowTG;
    }
}

/* Pillow main body as a BODObjects part */
class PillowMain extends BODObjects {
    public PillowMain() {
    	scale = 4.30d;
        post = new Vector3f(40.00f, -8.00f, 5.00f);
        transform_Object("pillow");
        
        // Collision bounds setup
        BoundingBox bounds = new BoundingBox(
            new Point3d(-1.0, -1.0, -1.0),   // Adjust if needed
            new Point3d(1.0, 1.0, 1.0)
        );
        objTG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        objTG.setPickable(true);
        objTG.setBounds(bounds);
        
        //obj_Appearance(); // CONTROL REAL TIME MATERIAL ADJUSTMENT 
        create_Appearance();
    }

    protected void create_Appearance() {
    	mtl_clr[0] = new Color3f(0.20f, 0.20f, 0.20f); // Ambient color — overall soft light the object "receives" from the environment.
    	mtl_clr[1] = new Color3f(0.45f, 0.45f, 0.45f); // Diffuse color — how the object reflects light when directly lit.
    	mtl_clr[2] = new Color3f(0.04f, 0.04f, 0.04f); // Specular color — the shininess/gloss highlights.
    	mtl_clr[3] = new Color3f(0.00f, 0.00f, 0.00f); // Emissive color — self-glow of the object.
    	setShininess(32.0f); // Shininess (float) — how concentrated or broad the specular highlight is.		mtl DOES NOT EXIST IN MAIN CODE
        obj_Appearance();

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);
        app.setTexture(textured_App("bloody_pillow"));
        
        //updateAppearance();						//UNCOMMENT ME FOR REAL TIME MATERIAL ADJUSTMENT
    }
    
    public TransformGroup position_Object() {
        objTG.addChild(objBG);  // model already created by transform_Object()

        // Apply rotation to objTG AFTER it was created
        Transform3D current = new Transform3D();
        objTG.getTransform(current);

        Transform3D rot = new Transform3D();
        rot.rotY(-Math.PI / 6);  // rotate 45° on Y-axis

        current.mul(rot);       // apply rotation after translation/scale
        objTG.setTransform(current);

        return objTG;
    }

    public void add_Child(TransformGroup nextTG) {
        objTG.addChild(nextTG);
    }
}
