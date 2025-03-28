package BOD;

import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.java3d.TransformGroup;



public class Table5 {
    private static BODObjects[] tableParts = new BODObjects[1];

    protected static TransformGroup create_Table5() {
        TransformGroup tableTG = new TransformGroup();
        tableParts[0] = new Table5Main();
        //((Table5Main)tableParts[0]).registerForTweaking();	// UNCOMMENT ME TO CONTROL REAL TIME MATERIAL ADJUSTMENT BEFORE HARDCODED LINES IN create_Appearance
        tableTG = tableParts[0].position_Object();
        return tableTG;
    }
}

/* Table5 main body as a BODObjects part */
class Table5Main extends BODObjects {
	public Table5Main() {
	    scale = 11d;
	    post = new Vector3f(-40.0f, -11.40f, 6f);
	    transform_Object("table5");

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

	    obj_Appearance(); 
	    create_Appearance();
	}

    protected void create_Appearance() {
    	mtl_clr[0] = new Color3f(0.05f, 0.05f, 0.05f); // Ambient color — overall soft light the object "receives" from the environment.
    	mtl_clr[1] = new Color3f(1.00f, 1.00f, 1.00f); // Diffuse color — how the object reflects light when directly lit.
    	mtl_clr[2] = new Color3f(0.01f, 0.01f, 0.01f); // Specular color — the shininess/gloss highlights.
    	mtl_clr[3] = new Color3f(0.05f, 0.05f, 0.05f); // Emissive color — self-glow of the object.
    	setShininess(31.0f) ;
    	//mtl.setShininess(31.0f); // Shininess (float) — how concentrated or broad the specular highlight is.		mtl DOES NOT EXIST IN MAIN CODE
        obj_Appearance();

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);
        app.setTexture(textured_App("table5"));
        
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
        objTG.addChild(objBG);
        
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
        objTG.addChild(nextTG);
    }
    
    public float getBoundingRadius() {
        return 15.0f; // tweak per object size
    }
}
