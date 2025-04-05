package models;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;
import java.awt.Container;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TvScene2 {

    // Array to hold the two textures we want to loop between.
    private static Texture[] textures;
    // Index to track current texture.
    private static int currentTextureIndex = 0;
    // Timer to switch textures periodically.
    private static Timer textureTimer;
    // Keep a reference to the Appearance used on the tv_screen.
    private static Appearance screenApp;

    public static BranchGroup createTvSceneBG() {
        BranchGroup sceneRoot = new BranchGroup();
        
        // Load TV frame model (remains static).
        BranchGroup tvFrameBG = loadOBJWithTexture("objects/tv.obj", "textures/tv_frame_texture.jpg");
        
        // Instead of loading a single texture for the tv screen, we load two textures.
        textures = new Texture[3];
        textures[0] = loadTexture("textures/texture1.jpg");
        textures[1] = loadTexture("textures/Corpse_Bloody.png");
        textures[2] = loadTexture("textures/texture2.jpg");
        
        
        
        // Create the tv_screen appearance and set the first texture.
        screenApp = new Appearance();
        screenApp.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
        screenApp.setCapability(Appearance.ALLOW_TEXTURE_READ);
        if (textures[0] != null) {
            screenApp.setTexture(textures[0]);
        }
        
        // Load the TV screen model with the appearance that will have its texture switched.
        BranchGroup tvScreenBG = loadOBJWithAppearance("objects/tv_screen.obj", screenApp);
        
        // Frame transform (identical to normal state)
        TransformGroup frameTG = new TransformGroup();
        frameTG.addChild(tvFrameBG);
        
        // Screen transform (offset as needed)
        TransformGroup screenTG = new TransformGroup();
        Transform3D screenTrans = new Transform3D();
        screenTrans.setTranslation(new Vector3f(0f, 0f, 0.1f));
        screenTG.setTransform(screenTrans);
        screenTG.addChild(tvScreenBG);
        
        // Combine frame and screen
        TransformGroup tvTG = new TransformGroup();
        tvTG.addChild(frameTG);
        tvTG.addChild(screenTG);
        
        // Main container with transform capabilities.
        TransformGroup tvContainer = new TransformGroup();
        tvContainer.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tvContainer.addChild(tvTG);
        
        sceneRoot.addChild(tvContainer);
        sceneRoot.compile();
        
        // Start texture switching (e.g., every 500 ms)
        startTextureSwitching();
        
        return sceneRoot;
    }
    
    /**
     * Loads an OBJ model with the given Appearance and returns it as a BranchGroup.
     */
    private static BranchGroup loadOBJWithAppearance(String objFilename, Appearance app) {
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
        // Recursively set the appearance.
        setAppearanceRecursively(modelBG, app);
        objRoot.addChild(modelBG);
        return objRoot;
    }
    
    /**
     * Loads an OBJ model with its texture and returns it as a BranchGroup.
     * (This method is similar to your existing one.)
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
     * Loads a texture from the given path.
     */
    private static Texture loadTexture(String texturePath) {
        TextureLoader loader = new TextureLoader(texturePath, new Container());
        return loader.getTexture();
    }
    
    /**
     * Recursively traverses the scene graph and applies the given Appearance to all Shape3D nodes.
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
    
    /**
     * Starts a timer that switches the texture on the tv_screen's appearance between two textures.
     */
    private static void startTextureSwitching() {
        int delay = 500; // milliseconds between texture switches
        textureTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle the current texture index.
                currentTextureIndex = (currentTextureIndex + 1) % textures.length;
                if (textures[currentTextureIndex] != null) {
                    screenApp.setTexture(textures[currentTextureIndex]);
                }
            }
        });
        textureTimer.start();
    }
}
