package models;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

import java.awt.Container;

public class FlatScreenTV {

    // Empty constructor for integration.
    public FlatScreenTV() {
    }
    
    /**
     * Builds and returns a BranchGroup representing the TV scene.
     * It loads the TV frame and TV screen models with textures,
     * positions them, groups them, and then applies an overall transform
     * for custom location and scaling.
     *
     * @return the BranchGroup representing the TV scene.
     */
    public static BranchGroup createTvSceneBG() {
        BranchGroup sceneRoot = new BranchGroup();
        
        // Load and texture the TV parts.
        // TV frame:
        BranchGroup tvFrameBG = loadOBJWithTexture("objects/tv.obj", "textures/tv_frame_texture.jpg");
        // TV screen:
        BranchGroup tvScreenBG = loadOBJWithTexture("objects/tv_screen.obj", "textures/blank.jpg");
        
        // Wrap the TV frame in a TransformGroup for individual positioning.
        TransformGroup frameTG = new TransformGroup();
        Transform3D frameTrans = new Transform3D();
        // Position the TV frame as needed. (Adjust these values as necessary.)
        frameTrans.setTranslation(new Vector3f(0f, 0f, 0f));
        frameTG.setTransform(frameTrans);
        frameTG.addChild(tvFrameBG);
        
        // Wrap the TV screen in a TransformGroup.
        TransformGroup screenTG = new TransformGroup();
        Transform3D screenTrans = new Transform3D();
        // For example, move the screen slightly forward relative to the frame.
        screenTrans.setTranslation(new Vector3f(0f, 0f, 0.1f));
        screenTG.setTransform(screenTrans);
        screenTG.addChild(tvScreenBG);
        
        // Group the TV parts together.
        TransformGroup tvTG = new TransformGroup();
        tvTG.addChild(frameTG);
        tvTG.addChild(screenTG);
        
        // Wrap the assembled TV parts in an overall TransformGroup for custom location and scaling.
        TransformGroup tvPositionTG = new TransformGroup();
        tvPositionTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        // Create a custom Transform3D for scaling and translation.
        Transform3D overallTrans = new Transform3D();
        // Scale the entire TV. (Adjust scale factor as needed.)
        overallTrans.setScale(11.5f);
        // Move the entire TV to a desired location (adjust the translation values).
        overallTrans.setTranslation(new Vector3f(27.0f, 0.0f, -95.0f));
        tvPositionTG.setTransform(overallTrans);
        tvPositionTG.addChild(tvTG);
        
        // Add the overall TV group to the scene root.
        sceneRoot.addChild(tvPositionTG);
        
        // Optionally, add a background.
        Background background = new Background(new Color3f(0.9f, 0.9f, 0.9f));
        background.setApplicationBounds(new BoundingSphere(new Point3d(0,0,0), 100.0));
        sceneRoot.addChild(background);
        
        sceneRoot.compile();
        return sceneRoot;
    }
    
    /**
     * Loads an OBJ model with its texture and returns it as a BranchGroup.
     *
     * @param objFilename     the filename of the OBJ model.
     * @param textureFilename the filename of the texture image.
     * @return a BranchGroup containing the textured model.
     */
    private static BranchGroup loadOBJWithTexture(String objFilename, String textureFilename) {
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
        
        // Create an Appearance for the model with its texture.
        Appearance appearance = new Appearance();
        TextureLoader texLoader = new TextureLoader(textureFilename, new Container());
        Texture texture = texLoader.getTexture();
        appearance.setTexture(texture);
        
        // Setup texture attributes so the texture blends with lighting.
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        appearance.setTextureAttributes(texAttr);
        
        // Set a basic material.
        Material material = new Material();
        material.setDiffuseColor(new Color3f(1f, 1f, 1f));
        appearance.setMaterial(material);
        
        // Recursively apply the appearance to all Shape3D nodes.
        setAppearanceRecursively(modelBG, appearance);
        objRoot.addChild(modelBG);
        return objRoot;
    }
    
    /**
     * Recursively traverses the scene graph and applies the given Appearance to all Shape3D nodes.
     *
     * @param node the starting node.
     * @param app  the Appearance to apply.
     */
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
