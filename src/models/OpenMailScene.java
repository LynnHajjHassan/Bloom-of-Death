package models;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;
import java.awt.Container;

public class OpenMailScene {
    
    public static BranchGroup createOpenMailBG() {
        BranchGroup sceneRoot = new BranchGroup();
        
        // Create the open mail and paper parts via composite classes.
        OpenMail openMail = new OpenMail();
        Paper paper = new Paper();
        
        // Get each part's TransformGroup.
        TransformGroup openMailTG = openMail.position_Object();
        TransformGroup paperTG = paper.position_Object();
        
        // Create a main TransformGroup that holds both parts.
        TransformGroup mainTG = new TransformGroup();
        mainTG.addChild(openMailTG);
        mainTG.addChild(paperTG);
        sceneRoot.addChild(mainTG);
        
        sceneRoot.setPickable(true);
        sceneRoot.compile();
        return sceneRoot;
    }
    
    // -------------------------------------------------------------------------
    // OpenMail: Loads the open mail model with its texture.
    static class OpenMail extends BODObjects {
        public OpenMail() {
            // Set the translation and scale as in your original code.
            // Original openMailTransform: translation (0, 20, -15) and scale 5.5.
            scale = 1.0f; // We'll apply the overall scale in the transform.
            post = new Vector3f(0f, 20f, -15f);
            transform_Object("openmail"); // Loads "objects/openmail.obj"
            create_Appearance();
        }
        
        protected void create_Appearance() {
            // Set basic material colors.
            mtl_clr[0] = new Color3f(1f, 1f, 1f);
            mtl_clr[1] = new Color3f(1f, 1f, 1f);
            mtl_clr[2] = new Color3f(1f, 1f, 1f);
            mtl_clr[3] = new Color3f(0f, 0f, 0f);
            obj_Appearance();
            // Apply the texture (expects "textures/openmail_texture.jpg")
            app.setTexture(textured_App("openmail_texture"));
        }
        
        @Override
        public TransformGroup position_Object() {
            // Attach the loaded geometry to objTG.
            objTG.addChild(objBG);
            // Create a Transform3D to combine translation, scale, and rotation.
            Transform3D trans = new Transform3D();
            trans.setTranslation(post);           // Translation: (0,20,-15)
            trans.setScale(5.5f);                  // Scale: 5.5
            // Apply a Y-axis rotation of ~100°.
            Transform3D yRot = new Transform3D();
            yRot.rotY(Math.PI / 1.8);              // roughly 100°
            trans.mul(yRot);
            objTG.setTransform(trans);
            return objTG;
        }
        
        @Override
        public void add_Child(TransformGroup nextTG) {
            objTG.addChild(nextTG);
        }
    }
    
    // -------------------------------------------------------------------------
    // Paper: Loads the paper (note) model with its texture.
    static class Paper extends BODObjects {
        public Paper() {
            // Set the translation and scale as in your original code.
            // Original paperTransform: translation (0.0, 22, -15.1), scale 5.0, and Y-rotation 182°.
            scale = 1.0f;
            post = new Vector3f(0.0f, 22f, -15.1f);
            transform_Object("paper"); // Loads "objects/paper.obj"
            create_Appearance();
        }
        
        protected void create_Appearance() {
            mtl_clr[0] = new Color3f(1f, 1f, 1f);
            mtl_clr[1] = new Color3f(1f, 1f, 1f);
            mtl_clr[2] = new Color3f(1f, 1f, 1f);
            mtl_clr[3] = new Color3f(0f, 0f, 0f);
            obj_Appearance();
            // Apply the paper texture (expects "textures/note.jpg")
            app.setTexture(textured_App("note"));
        }
        
        @Override
        public TransformGroup position_Object() {
            objTG.addChild(objBG);
            // Create a Transform3D for the paper.
            Transform3D trans = new Transform3D();
            trans.setTranslation(post);   // Translation: (0, 22, -15.1)
            trans.setScale(5.0);           // Scale: 5.0
            // Apply a Y-axis rotation of 182°.
            Transform3D yRot = new Transform3D();
            yRot.rotY(Math.toRadians(182));
            trans.mul(yRot);
            objTG.setTransform(trans);
            return objTG;
        }
        
        @Override
        public void add_Child(TransformGroup nextTG) {
            objTG.addChild(nextTG);
        }
    }
    
    // -------------------------------------------------------------------------
    // The following helper methods are as in your original code.
    private static void enablePicking(Group group) {
        group.setCapability(BranchGroup.ALLOW_PICKABLE_READ);
        group.setCapability(BranchGroup.ALLOW_PICKABLE_WRITE);
        for (int i = 0; i < group.numChildren(); i++) {
            Node child = group.getChild(i);
            if (child instanceof Shape3D) {
                ((Shape3D) child).setCapability(Shape3D.ALLOW_PICKABLE_READ);
                ((Shape3D) child).setPickable(true);
            } else if (child instanceof Group) {
                enablePicking((Group) child);
            }
        }
    }
    
    public static BranchGroup loadOBJWithTexture(String objFilename, String textureFilename) {
        BranchGroup objRoot = new BranchGroup();
        ObjectFile loader = new ObjectFile(ObjectFile.RESIZE | ObjectFile.TRIANGULATE);
        Scene scene = null;
        try {
            scene = loader.load(objFilename);
        } catch (Exception e) {
            System.err.println("Error loading: " + objFilename);
            e.printStackTrace();
            return objRoot;
        }
        BranchGroup modelBG = scene.getSceneGroup();
        
        Appearance appearance = new Appearance();
        TextureLoader texLoader = new TextureLoader(textureFilename, new Container());
        Texture texture = texLoader.getTexture();
        appearance.setTexture(texture);
        
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        appearance.setTextureAttributes(texAttr);
        
        Material material = new Material();
        material.setDiffuseColor(new Color3f(1f, 1f, 1f));
        appearance.setMaterial(material);
        
        setAppearanceRecursively(modelBG, appearance);
        objRoot.addChild(modelBG);
        return objRoot;
    }
    
    private static void setAppearanceRecursively(Node node, Appearance app) {
        if (node instanceof Shape3D) {
            ((Shape3D) node).setAppearance(app);
        } else if (node instanceof Group) {
            Group group = (Group) node;
            for (int i = 0; i < group.numChildren(); i++) {
                setAppearanceRecursively(group.getChild(i), app);
            }
        }
    }
}
