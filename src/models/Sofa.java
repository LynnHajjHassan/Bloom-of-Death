package models;

import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3d;

public class Sofa {

    private static BODObjects[] sofaParts = new BODObjects[1];

    public static TransformGroup create_Sofa() {
        TransformGroup sofaTG = new TransformGroup();

        // Create main sofa body and attach
        sofaParts[0] = new SofaMain();
        //((SofaMain)sofaParts[0]).registerForTweaking();	// UNCOMMENT ME TO CONTROL REAL TIME MATERIAL ADJUSTMENT BEFORE HARDCODED LINES IN create_Appearance
        sofaTG = sofaParts[0].position_Object();

        return sofaTG;
    }
	public static BODObjects[] getSofaParts() {
	    return sofaParts;
	}
}

/* Sofa main body as a BODObjects part */
class SofaMain extends BODObjects {

    public SofaMain() {
        scale = 17d;                                           // Scale as per your current implementation
        post = new Vector3f(40.00f, -10.50f, 14f);                  // Position of the sofa
        transform_Object("sofa");
        
        // Collision bounds setup
        BoundingBox bounds = new BoundingBox(
            new Point3d(-2.5, -1.0, -1.0),   // Adjust these as needed based on sofa size
            new Point3d(2.5, 1.0, 1.0)
        );
        objTG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        objTG.setPickable(true);
        objTG.setBounds(bounds);

        //obj_Appearance(); // CONTROL REAL TIME MATERIAL ADJUSTMENT 
        create_Appearance(); // optional additional texture setup
    }

    protected void create_Appearance() {
    	mtl_clr[0] = new Color3f(0f, 0f, 0f); // Ambient color — overall soft light the object "receives" from the environment.
    	mtl_clr[1] = new Color3f(0.30f, 0.30f, 0.30f); // Diffuse color — how the object reflects light when directly lit.
    	mtl_clr[2] = new Color3f(0.03f, 0.03f, 0.03f); // Specular color — the shininess/gloss highlights.
    	mtl_clr[3] = new Color3f(0.3f, 0.3f, 0.3f); // Emissive color — self-glow of the object.
    	setShininess(8.0f); // Shininess (float) — how concentrated or broad the specular highlight is.		mtl DOES NOT EXIST IN MAIN CODE
        obj_Appearance();
        
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);
        app.setTexture(textured_App("sofa"));

        //updateAppearance();						//UNCOMMENT ME FOR REAL TIME MATERIAL ADJUSTMENT
    }
    
    /*public void updateAppearance() {				//UNCOMMENT ME FOR REAL TIME MATERIAL ADJUSTMENT
    	updateAppearanceGeneral(
		    BODMain.currentAmbient,
		    BODMain.currentDiffuse,
		    BODMain.currentSpecular,
		    BODMain.currentEmissive,
		    BODMain.currentShininess
		);
    }
    
    public void registerForTweaking() {				//UNCOMMENT ME FOR REAL TIME MATERIAL ADJUSTMENT
        BODMain.updateCallback = this::updateAppearance;
    }*/
    
    public TransformGroup position_Object() {
        objTG.addChild(objBG);                        // attach loaded sofa branch
    
        // Apply rotation to objTG AFTER it was created
        Transform3D current = new Transform3D();
        objTG.getTransform(current);

        Transform3D rot = new Transform3D();
        rot.rotY(-Math.PI / 2);  // rotate 45° on Y-axis

        current.mul(rot);       // apply rotation after translation/scale
        objTG.setTransform(current);
        return objTG;
    }

    public void add_Child(TransformGroup nextTG) {
        objTG.addChild(nextTG);                       // attach additional parts if needed
    }
}