package models;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;
import java.awt.Container;
import javax.swing.JPanel;

public class FlowerScene extends JPanel {
    private static final long serialVersionUID = 1L;
    
    // Global TransformGroup for overall flower positioning.
    private static TransformGroup flowerPositionTG;
    
    /**
     * Creates and returns a BranchGroup containing the complete flower scene.
     */
    public static BranchGroup createFlowerBG() {
        BranchGroup sceneBG = new BranchGroup();
        TransformGroup sceneTG = new TransformGroup();
        
        // Assemble the flower from its parts.
        TransformGroup flowerTG = createFlower();
        sceneTG.addChild(flowerTG);
        
        // Add basic lighting.
        sceneBG.addChild(createLights());
        
        // Optionally, add a background.
        Background bg = new Background(new Color3f(0.9f, 0.9f, 0.9f));
        bg.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), 100.0));
        sceneBG.addChild(bg);
        
        sceneBG.addChild(sceneTG);
        return sceneBG;
    }
    
    /**
     * Assembles the flower parts (stem, pistil, petal) into one TransformGroup.
     * The positions and scales are preserved from the original code.
     */
    private static TransformGroup createFlower() {
        TransformGroup flowerTG = new TransformGroup();
        
        // Create the flower parts.
        FlowerStem stem = new FlowerStem();
        FlowerPistil pistil = new FlowerPistil();
        FlowerPetal petal = new FlowerPetal();
        
        // Add parts as siblings (exact order as in the original code).
        flowerTG.addChild(stem.position_Object());
        flowerTG.addChild(pistil.position_Object());
        flowerTG.addChild(petal.position_Object());
        
        // Wrap the flower in a global TransformGroup for overall positioning.
        flowerPositionTG = new TransformGroup();
        flowerPositionTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D overallTrans = new Transform3D();
        overallTrans.setTranslation(new Vector3f(39.0f, -11.50f, 21.0f));
        flowerPositionTG.setTransform(overallTrans);
        flowerPositionTG.addChild(flowerTG);
        
        return flowerPositionTG;
    }
    
    /**
     * Creates basic lighting for the scene.
     */
    private static BranchGroup createLights() {
        BranchGroup lightBG = new BranchGroup();
        
        AmbientLight ambientLight = new AmbientLight(new Color3f(0.5f, 0.5f, 0.5f));
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100.0));
        lightBG.addChild(ambientLight);
        
        DirectionalLight directionalLight = new DirectionalLight(
            new Color3f(1f, 1f, 1f), new Vector3f(-0.5f, -1.0f, -0.5f));
        directionalLight.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100.0));
        lightBG.addChild(directionalLight);
        
        return lightBG;
    }
    
    // -------------------------------------------------------------------------
    // FlowerStem: Represents the flower's stem.
    static class FlowerStem extends BODObjects {
        public FlowerStem() {
            // Set scale and translation to match original:
            // Translation: (-0.5, 1.0, -3.5); Scale: 3.5.
            scale = 3.5f;
            post = new Vector3f(-0.5f, 1.0f, -3.5f);
            // Load the stem model. (Assumes the OBJ file is named "stem.obj" in the correct folder)
            transform_Object("stem");
            create_Appearance();
        }
        
        protected void create_Appearance() {
            // Use white as base colors.
            mtl_clr[0] = new Color3f(1.0f, 1.0f, 1.0f);
            mtl_clr[1] = new Color3f(1.0f, 1.0f, 1.0f);
            mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f);
            mtl_clr[3] = new Color3f(0f, 0f, 0f);
            obj_Appearance();
            // Apply the stem texture from "stem_texture.jpg".
            app.setTexture(textured_App("stem_texture"));
        }
        
        @Override
        public TransformGroup position_Object() {
            // Attach the loaded stem geometry.
            objTG.addChild(objBG);
            return objTG;
        }
        
        @Override
        public void add_Child(TransformGroup nextTG) {
            objTG.addChild(nextTG);
        }
    }
    
    // -------------------------------------------------------------------------
    // FlowerPistil: Represents the flower's pistil (center part).
    static class FlowerPistil extends BODObjects {
        public FlowerPistil() {
            // Set position as per original: (0.0, 1.2, 0.0); Scale: 1.0.
            scale = 1.0f;
            post = new Vector3f(0.0f, 1.2f, 0.0f);
            transform_Object("pistil");
            create_Appearance();
        }
        
        protected void create_Appearance() {
            mtl_clr[0] = new Color3f(1.0f, 1.0f, 1.0f);
            mtl_clr[1] = new Color3f(1.0f, 1.0f, 1.0f);
            mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f);
            mtl_clr[3] = new Color3f(0f, 0f, 0f);
            obj_Appearance();
            // Apply the pistil texture from "pistil_texture.jpg".
            app.setTexture(textured_App("pistil_texture"));
        }
        
        @Override
        public TransformGroup position_Object() {
            objTG.addChild(objBG);
            Transform3D pistilTrans = new Transform3D();
            pistilTrans.setTranslation(post);
            objTG.setTransform(pistilTrans);
            return objTG;
        }
        
        @Override
        public void add_Child(TransformGroup nextTG) {
            objTG.addChild(nextTG);
        }
    }
    
    // -------------------------------------------------------------------------
    // FlowerPetal: Represents the flower's petals.
    static class FlowerPetal extends BODObjects {
        public FlowerPetal() {
            // Set position as per original: (0.0, 1.2, 0.0); Scale: 1.0.
            scale = 1.0f;
            post = new Vector3f(0.0f, 1.2f, 0.0f);
            transform_Object("petal");
            create_Appearance();
        }
        
        protected void create_Appearance() {
            mtl_clr[0] = new Color3f(1.0f, 1.0f, 1.0f);
            mtl_clr[1] = new Color3f(1.0f, 1.0f, 1.0f);
            mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f);
            mtl_clr[3] = new Color3f(0f, 0f, 0f);
            obj_Appearance();
            // Apply the petal texture from "petal_texture.jpg".
            app.setTexture(textured_App("petal_texture"));
        }
        
        @Override
        public TransformGroup position_Object() {
            objTG.addChild(objBG);
            Transform3D petalTrans = new Transform3D();
            petalTrans.setTranslation(post);
            objTG.setTransform(petalTrans);
            return objTG;
        }
        
        @Override
        public void add_Child(TransformGroup nextTG) {
            objTG.addChild(nextTG);
        }
    }
}
