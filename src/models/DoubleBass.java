package models;

import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.java3d.TransformGroup;



public class DoubleBass {
	
    private static BODObjects[] bassParts = new BODObjects[1];

    public static TransformGroup create_DoubleBass() {
        TransformGroup bassTG = new TransformGroup();

        // Create main doubleBass body and attach
        bassParts[0] = new DoubleBassMain();
        //((DoubleBassMain)bassParts[0]).registerForTweaking();	// UNCOMMENT ME TO CONTROL REAL TIME MATERIAL ADJUSTMENT BEFORE HARDCODED LINES IN create_Appearance
        bassTG = bassParts[0].position_Object();
        
        return bassTG;
    }
}

/* DoubleBass main body as a BODObjects part */
class DoubleBassMain extends BODObjects {
    
	public DoubleBassMain() {
        scale = 12.30d;
        post = new Vector3f(-45.0f, -5.65f, -95.0f);
        transform_Object("doublebass");
        
        // Collision bounds setup
        BoundingBox bounds = new BoundingBox(
            new Point3d(-1.2, -2.5, -0.8),   // Adjust size based on object shape
            new Point3d(1.2, 2.5, 0.8)
        );
        objTG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        objTG.setPickable(true);
        objTG.setBounds(bounds);
    
        //obj_Appearance(); // CONTROL REAL TIME MATERIAL ADJUSTMENT 
        create_Appearance();
    }

    protected void create_Appearance() {
    	mtl_clr[0] = new Color3f(0.2f, 0.2f, 0.2f); // Ambient color — overall soft light the object "receives" from the environment.
    	mtl_clr[1] = new Color3f(1.00f, 1.00f, 1.00f); // Diffuse color — how the object reflects light when directly lit.
    	mtl_clr[2] = new Color3f(0.83f, 0.83f, 0.83f); // Specular color — the shininess/gloss highlights.
    	mtl_clr[3] = new Color3f(0.15f, 0.15f, 0.15f); // Emissive color — self-glow of the object.
    	setShininess(128.0f); // Shininess (float) — how concentrated or broad the specular highlight is.		mtl DOES NOT EXIST IN MAIN CODE
        obj_Appearance();

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);
        app.setTexture(textured_App("doublebass"));
        
        //updateAppearance();						//UNCOMMENT ME FOR REAL TIME MATERIAL ADJUSTMENT
    }

    public TransformGroup position_Object() {
        objTG.addChild(objBG);

        // Apply rotation to objTG AFTER it was created
        Transform3D current = new Transform3D();
        objTG.getTransform(current);

        Transform3D rot = new Transform3D();
        rot.rotY(Math.PI / 6);  // rotate 45° on Y-axis

        current.mul(rot);       // apply rotation after translation/scale
        objTG.setTransform(current);

        return objTG;
    }

    public void add_Child(TransformGroup nextTG) {
        objTG.addChild(nextTG);
    }
}