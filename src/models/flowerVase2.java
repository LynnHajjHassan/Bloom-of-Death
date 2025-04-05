package models;import org.jogamp.java3d.*;
	import org.jogamp.java3d.loaders.Scene;
	import org.jogamp.java3d.loaders.objectfile.ObjectFile;
	import org.jogamp.java3d.utils.image.TextureLoader;
	import org.jogamp.vecmath.*;

	import java.awt.Container;

	public class flowerVase2 {

	    // Empty constructor for integration
	    public flowerVase2() {
	    }

	    /**
	     * Creates and returns a BranchGroup representing the flower vase scene.
	     * The scene includes a vase container, soil, stem, and flower parts.
	     *
	     * @return the BranchGroup for the flower vase.
	     */
	    public static BranchGroup createFlowerVaseBG() {
	        BranchGroup sceneRoot = new BranchGroup();

	        // *******************************
	        // Load and texture the vase parts.
	        // *******************************
	        BranchGroup vaseBG   = loadOBJWithTexture("objects/vase.obj",           "textures/vase_texture.jpg");
	        BranchGroup soilBG   = loadOBJWithTexture("objects/vase_soil.obj",      "textures/vase_soil_texture.jpg");
	        BranchGroup stemBG   = loadOBJWithTexture("objects/vase_stem.obj",      "textures/vase_stem_texture.jpg");
	        BranchGroup flowerBG = loadOBJWithTexture("objects/vase_flower.obj",    "textures/vase_flower_texture.jpg");

	        // *******************************
	        // Apply individual transformations.
	        // *******************************
	        
	        // Vase container (main body)
	        TransformGroup vaseTG = new TransformGroup();
	        Transform3D vaseTrans = new Transform3D();
	        vaseTrans.setTranslation(new Vector3f(0f, 0f, 0f)); // adjust if needed
	        vaseTG.setTransform(vaseTrans);
	        vaseTG.addChild(vaseBG);

	        // Soil: position it inside the vase.
	        TransformGroup soilTG = new TransformGroup();
	        Transform3D soilTrans = new Transform3D();
	        soilTrans.setTranslation(new Vector3f(0f, 0.9f, 0f)); // adjust vertical position
	        Transform3D soilScale = new Transform3D();
	        soilScale.setScale(0.8f);  // scale the soil if necessary
	        soilTrans.mul(soilScale);
	        soilTG.setTransform(soilTrans);
	        soilTG.addChild(soilBG);

	        // Stem: position and scale up if desired.
	        TransformGroup stemTG = new TransformGroup();
	        Transform3D stemTrans = new Transform3D();
	        // Move the stem upward from the soil
	        stemTrans.setTranslation(new Vector3f(0f, 1.0f, 0f));  
	        Transform3D stemScale = new Transform3D();
	        stemScale.setScale(1.5f);  // scale the stem (1.0 is original size)
	        stemTrans.mul(stemScale);
	        stemTG.setTransform(stemTrans);
	        stemTG.addChild(stemBG);

	        // Flower: position on top of the stem and scale up.
	        TransformGroup flowerTG = new TransformGroup();
	        Transform3D flowerTrans = new Transform3D();
	        // Adjust translation based on stem height
	        flowerTrans.setTranslation(new Vector3f(0f, 2.0f, 0f));  
	        Transform3D flowerScale = new Transform3D();
	        flowerScale.setScale(1.8f);  // scale up the flower
	        flowerTrans.mul(flowerScale);
	        flowerTG.setTransform(flowerTrans);
	        flowerTG.addChild(flowerBG);

	        // *******************************
	        // Group all parts together.
	        // *******************************
	        TransformGroup vaseObjectTG = new TransformGroup();
	        vaseObjectTG.addChild(vaseTG);
	        vaseObjectTG.addChild(soilTG);
	        vaseObjectTG.addChild(stemTG);
	        vaseObjectTG.addChild(flowerTG);

	        // *******************************
	        // Apply overall transformation.
	        // *******************************
	        // Wrap the complete vase in a TransformGroup so you can adjust its position and scale.
	        TransformGroup plantPositionTG = new TransformGroup();
	        plantPositionTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	        
	        Transform3D customTransform = new Transform3D();
	        // Scale the entire flower vase (adjust value as needed)
	        customTransform.setScale(3f);
	        // Translate to desired position in your main scene (adjust X, Y, Z as needed)
	        customTransform.setTranslation(new Vector3f(-41f, -15.5f, 92f));
	        
	        plantPositionTG.setTransform(customTransform);
	        plantPositionTG.addChild(vaseObjectTG);

	        sceneRoot.addChild(plantPositionTG);

	        // *******************************
	        // Add a background.
	        // *******************************
	        Background background = new Background(new Color3f(0.9f, 0.9f, 0.9f));
	        background.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), 100.0));
	        sceneRoot.addChild(background);

	        sceneRoot.compile();
	        return sceneRoot;
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
