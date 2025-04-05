package models;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

import java.awt.Container;

public class Plant2PotScene {

    public Plant2PotScene() {}

    private static TransformGroup plant2PositionTG;

    // ✅ Add this: returns the TransformGroup for use in roomTG & collisions
    public static TransformGroup getPlant2PotTG() {
        BranchGroup potBG   = loadOBJWithTexture("objects/plant2_pot.obj",   "textures/plant2_pot_texture.jpg");
        BranchGroup soilBG  = loadOBJWithTexture("objects/plant2_soil.obj",  "textures/plant2_soil_texture.jpg");
        BranchGroup leafBG  = loadOBJWithTexture("objects/plant2_leaf.obj",  "textures/plant2_leaf_texture.jpg");

        TransformGroup potTG = new TransformGroup();
        Transform3D potTrans = new Transform3D();
        potTrans.setTranslation(new Vector3f(0f, 0f, 0f));
        potTG.setTransform(potTrans);
        potTG.addChild(potBG);

        TransformGroup soilTG = new TransformGroup();
        Transform3D soilTrans = new Transform3D();
        soilTrans.setTranslation(new Vector3f(0f, 0.9f, 0f));
        Transform3D soilScale = new Transform3D();
        soilScale.setScale(0.8f);
        soilTrans.mul(soilScale);
        soilTG.setTransform(soilTrans);
        soilTG.addChild(soilBG);

        TransformGroup leafTG = new TransformGroup();
        Transform3D leafTrans = new Transform3D();
        leafTrans.setTranslation(new Vector3f(0.01f, 3.9f, 0f));
        Transform3D leafScale = new Transform3D();
        leafScale.setScale(3.0f);
        leafTrans.mul(leafScale);
        leafTG.setTransform(leafTrans);
        leafTG.addChild(leafBG);

        TransformGroup plantTG = new TransformGroup();
        plantTG.addChild(potTG);
        plantTG.addChild(soilTG);
        plantTG.addChild(leafTG);

        plant2PositionTG = new TransformGroup();
        plant2PositionTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        Transform3D customTransform = new Transform3D();
        customTransform.setScale(3f);
        customTransform.setTranslation(new Vector3f(45f, -15f, -23.0f));
        plant2PositionTG.setTransform(customTransform);
        plant2PositionTG.addChild(plantTG);

        // ✅ Add collision
        BoundingBox bounds = new BoundingBox(
            new Point3d(-1.0, -1.0, -1.0),
            new Point3d(1.0, 1.0, 1.0)
        );
        plant2PositionTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        plant2PositionTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        plant2PositionTG.setCapability(Node.ENABLE_PICK_REPORTING);
        plant2PositionTG.setPickable(true);
        plant2PositionTG.setBounds(bounds);

        return plant2PositionTG;
    }

    // ✅ Unchanged — returns full BranchGroup with lighting and background
    public static BranchGroup createPlant2PotBG() {
        BranchGroup sceneRoot = new BranchGroup();
        sceneRoot.addChild(getPlant2PotTG());

        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 100.0);

        AmbientLight ambientLight = new AmbientLight(new Color3f(0.3f, 0.3f, 0.3f));
        ambientLight.setInfluencingBounds(bounds);
        sceneRoot.addChild(ambientLight);

        DirectionalLight dirLight = new DirectionalLight(
            new Color3f(1f, 1f, 0.9f),
            new Vector3f(-0.5f, -1.0f, -0.3f)
        );
        dirLight.setInfluencingBounds(bounds);
        sceneRoot.addChild(dirLight);

        Background background = new Background(new Color3f(0.9f, 0.9f, 0.9f));
        background.setApplicationBounds(bounds);
        sceneRoot.addChild(background);

        sceneRoot.compile();
        return sceneRoot;
    }

    public static void setPlant2Translation(Vector3f translation) {
        Transform3D currentTransform = new Transform3D();
        plant2PositionTG.getTransform(currentTransform);
        currentTransform.setTranslation(translation);
        plant2PositionTG.setTransform(currentTransform);
    }

    public static void setPlant2Scale(float scale) {
        Transform3D currentTransform = new Transform3D();
        plant2PositionTG.getTransform(currentTransform);
        currentTransform.setScale(scale);
        plant2PositionTG.setTransform(currentTransform);
    }

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

     // Set a better material with full lighting control
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.1f, 0.1f, 0.1f));
        material.setDiffuseColor(new Color3f(0.5f, 0.5f, 0.5f));
        material.setSpecularColor(new Color3f(0.5f, 0.5f, 0.5f));
        material.setEmissiveColor(new Color3f(0.0f, 0.0f, 0.0f));
        material.setShininess(30.0f); // adjust sharpness of highlights
        material.setLightingEnable(true); // make sure it's lit
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
