package models;import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

import java.awt.Container;

public class FlowerVaseScene {

    public static TransformGroup getFlowerVaseTG() {
        // Load and transform individual parts
        TransformGroup vaseTG = new TransformGroup();
        vaseTG.setTransform(new Transform3D());
        vaseTG.addChild(loadOBJWithTexture("objects/vase.obj", "textures/vase_texture.jpg"));

        TransformGroup soilTG = new TransformGroup();
        Transform3D soilT3D = new Transform3D();
        soilT3D.setTranslation(new Vector3f(0f, 0.9f, 0f));
        Transform3D soilScale = new Transform3D();
        soilScale.setScale(0.8f);
        soilT3D.mul(soilScale);
        soilTG.setTransform(soilT3D);
        soilTG.addChild(loadOBJWithTexture("objects/vase_soil.obj", "textures/vase_soil_texture.jpg"));

        TransformGroup stemTG = new TransformGroup();
        Transform3D stemT3D = new Transform3D();
        stemT3D.setTranslation(new Vector3f(0f, 1.0f, 0f));
        Transform3D stemScale = new Transform3D();
        stemScale.setScale(1.5f);
        stemT3D.mul(stemScale);
        stemTG.setTransform(stemT3D);
        stemTG.addChild(loadOBJWithTexture("objects/vase_stem.obj", "textures/vase_stem_texture.jpg"));

        TransformGroup flowerTG = new TransformGroup();
        Transform3D flowerT3D = new Transform3D();
        flowerT3D.setTranslation(new Vector3f(0f, 2.0f, 0f));
        Transform3D flowerScale = new Transform3D();
        flowerScale.setScale(1.8f);
        flowerT3D.mul(flowerScale);
        flowerTG.setTransform(flowerT3D);
        flowerTG.addChild(loadOBJWithTexture("objects/vase_flower.obj", "textures/vase_flower_texture.jpg"));

        // Combine parts
        TransformGroup combined = new TransformGroup();
        combined.addChild(vaseTG);
        combined.addChild(soilTG);
        combined.addChild(stemTG);
        combined.addChild(flowerTG);

        // Final positioning group with collision
        TransformGroup plantPositionTG = new TransformGroup();
        Transform3D posT3D = new Transform3D();
        posT3D.setScale(3f);
        posT3D.setTranslation(new Vector3f(-41f, -15.5f, 92f));
        plantPositionTG.setTransform(posT3D);
        plantPositionTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        plantPositionTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        plantPositionTG.setCapability(Node.ENABLE_PICK_REPORTING);
        plantPositionTG.setPickable(true);
        plantPositionTG.setBounds(new BoundingBox(new Point3d(-1.0, -1.0, -1.0), new Point3d(1.0, 1.0, 1.0)));
        plantPositionTG.addChild(combined);

        return plantPositionTG;
    }

    public static BranchGroup createFlowerVaseBG() {
        BranchGroup sceneRoot = new BranchGroup();
        sceneRoot.addChild(getFlowerVaseTG());

        Background background = new Background(new Color3f(0.9f, 0.9f, 0.9f));
        background.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), 100.0));
        sceneRoot.addChild(background);

        sceneRoot.compile();
        return sceneRoot;
    }

    // (leave loadOBJWithTexture() and setAppearanceRecursively() unchanged)

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

        // Create an Appearance for the model with its texture.
        Appearance appearance = new Appearance();
        TextureLoader texLoader = new TextureLoader(textureFilename, new Container());
        Texture texture = texLoader.getTexture();
        appearance.setTexture(texture);

        // Setup texture attributes so the texture blends with lighting.
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
