package models;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

import java.awt.Container;

public class CactusPotScene {
    
    // Retain the class name "CactusPotScene" as before.
    // Removed "extends JFrame" so that it can be used as a component of your overall scene.
    public CactusPotScene() {
        // Empty constructor (not used when integrating the scene).
    }
    
    // Static TransformGroup for controlling cactus pot position and scale.
    private static TransformGroup cactusPositionTG;
    
    /**
     * This static method builds and returns a BranchGroup containing the cactus pot scene.
     * It loads the cactus pot, soil, cactus body, and spikes models with textures, positions them,
     * groups them, adds rotation behavior, lighting, and a background.
     *
     * Additionally, it wraps the assembled scene in a TransformGroup (cactusPositionTG)
     * that applies a custom transformation (location and scaling) to the entire cactus pot.
     *
     * @return the BranchGroup representing the cactus pot scene.
     */
    public static BranchGroup createCactusPotBG() {
        BranchGroup sceneRoot = new BranchGroup();
        
        // *******************************
        // Load and texture the cactus parts
        // *******************************
        
        // Load the cactus pot, soil, cactus body, and spikes.
        BranchGroup potBG      = loadOBJWithTexture("objects/cac_pot.obj",    "textures/cac_pot_texture.jpg");
        BranchGroup soilBG     = loadOBJWithTexture("objects/cac_soil.obj",   "textures/cac_soil_texture.jpg");
        BranchGroup bodyBG     = loadOBJWithTexture("objects/cac.obj",        "textures/cac_texture.jpg");
        BranchGroup spikeBG    = loadOBJWithTexture("objects/cac_spike.obj",  "textures/cac_spike_texture.jpg");
        
        // Wrap each part in a TransformGroup for individual positioning.
        
        // Cactus Pot (base)
        TransformGroup potTG = new TransformGroup();
        Transform3D potTrans = new Transform3D();
        // Position the pot at the origin (adjust as needed)
        potTrans.setTranslation(new Vector3f(0f, 0f, 0f));
        potTG.setTransform(potTrans);
        potTG.addChild(potBG);
        
        // Soil: position slightly above the pot's top.
        TransformGroup soilTG = new TransformGroup();
        Transform3D soilTrans = new Transform3D();
        soilTrans.setTranslation(new Vector3f(0f, 0.5f, 0f)); // tweak Y as needed
        soilTG.setTransform(soilTrans);
        soilTG.addChild(soilBG);
        
        // Cactus Body: position above the soil.
        TransformGroup bodyTG = new TransformGroup();
        Transform3D bodyTrans = new Transform3D();
        bodyTrans.setTranslation(new Vector3f(0f, 1.5f, 0f)); // adjust based on your model
        bodyTG.setTransform(bodyTrans);
        bodyTG.addChild(bodyBG);
        
        // Cactus Spikes: these are applied to the cactus body.
        TransformGroup spikeTG = new TransformGroup();
        Transform3D spikeTrans = new Transform3D();
        spikeTrans.setTranslation(new Vector3f(0f, 1.8f, 0f)); // same base position as the body
        spikeTG.setTransform(spikeTrans);
        spikeTG.addChild(spikeBG);
        
        // Group the cactus body and its spikes together.
        TransformGroup cactusTG = new TransformGroup();
        cactusTG.addChild(bodyTG);
        cactusTG.addChild(spikeTG);
        
        // Group all cactus parts: pot, soil, and cactus (body+spikes)
        TransformGroup cactusPlantTG = new TransformGroup();
        cactusPlantTG.addChild(potTG);
        cactusPlantTG.addChild(soilTG);
        cactusPlantTG.addChild(cactusTG);
        
        // Wrap everything in a TransformGroup so we can move and scale the entire cactus.
        cactusPositionTG = new TransformGroup();
        cactusPositionTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D cactusPosTrans = new Transform3D();
        // Default translation (adjust as needed)
        
        cactusPosTrans.setTranslation(new Vector3f(42f, -15f, 40f));
        // Default scale (1.0 means no scaling)
        cactusPosTrans.setScale(1.7f);
        cactusPositionTG.setTransform(cactusPosTrans);
        cactusPositionTG.addChild(cactusPlantTG);
        
        sceneRoot.addChild(cactusPositionTG);
        
        // *******************************
        // Add rotation behavior
        // *******************************
       /* Alpha rotationAlpha = new Alpha(-1, 5000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, cactusPositionTG);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100.0));
        cactusPositionTG.addChild(rotator);*/
        
        // *******************************
        // Setup lighting for the scene
        // *******************************
        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 100.0);
        AmbientLight ambientLight = new AmbientLight(new Color3f(0.3f, 0.3f, 0.3f));
        ambientLight.setInfluencingBounds(bounds);
        sceneRoot.addChild(ambientLight);
        DirectionalLight dirLight = new DirectionalLight(
                new Color3f(1f, 1f, 0.9f),
                new Vector3f(-0.5f, -1.0f, -0.3f));
        dirLight.setInfluencingBounds(bounds);
        sceneRoot.addChild(dirLight);
        Background background = new Background(new Color3f(0.9f, 0.9f, 0.9f));
        background.setApplicationBounds(bounds);
        sceneRoot.addChild(background);
        
        sceneRoot.compile();
        return sceneRoot;
    }
    
    /**
     * Sets the translation (x, y, z) of the cactus pot.
     * @param translation A Vector3f representing the new translation.
     */
    public static void setCactusTranslation(Vector3f translation) {
        Transform3D currentTransform = new Transform3D();
        cactusPositionTG.getTransform(currentTransform);
        currentTransform.setTranslation(translation);
        cactusPositionTG.setTransform(currentTransform);
    }
    
    /**
     * Sets the scale of the cactus pot.
     * @param scale The new scale factor.
     */
    public static void setCactusScale(float scale) {
        Transform3D currentTransform = new Transform3D();
        cactusPositionTG.getTransform(currentTransform);
        currentTransform.setScale(scale);
        cactusPositionTG.setTransform(currentTransform);
    }
    
    /**
     * Loads an OBJ model with a texture and returns it as a BranchGroup.
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
    
    /**
     * Recursively traverses the scene graph and applies the given Appearance to all Shape3D nodes.
     *
     * @param node The starting node.
     * @param app  The Appearance to apply.
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
