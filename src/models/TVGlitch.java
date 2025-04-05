package models;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.picking.*;
import org.jogamp.vecmath.*;

public class TVGlitch {
    // TV system components (sound-related fields removed)
    public static Switch tvSwitch;
    public static TransformGroup tvRootTG;
    public static TransformGroup rotatingTG;
    public static PickTool pickTool;

    public static TransformGroup createTVSystem() {
        BranchGroup tvBG = create_Scene();
        
        // Main positioning group
        tvRootTG = new TransformGroup();
        tvRootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        // Rotation group
        rotatingTG = new TransformGroup();
        rotatingTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        // Set position, rotation and scale
        Transform3D transform = new Transform3D();
        transform.setTranslation(new Vector3f(27.0f, 3.0f, -95.0f)); // TV position
        transform.setScale(11.5f); // TV size
        
        // Initial rotation (0° = facing forward)
        Transform3D rotation = new Transform3D();
        rotation.rotY(Math.toRadians(0)); 
        transform.mul(rotation);
        
        tvRootTG.setTransform(transform);
        rotatingTG.addChild(tvBG);
        tvRootTG.addChild(rotatingTG);
        
        return tvRootTG;
    }

    public static void toggleTVState() {
        if (tvSwitch != null) {
            int current = tvSwitch.getWhichChild();
            tvSwitch.setWhichChild(current == 0 ? 1 : 0);
            // Sound functionality removed
        }
    }

    public static void rotateTV(float degrees) {
        if (rotatingTG != null) {
            Transform3D trans = new Transform3D();
            rotatingTG.getTransform(trans);
            
            Transform3D rot = new Transform3D();
            rot.rotY(Math.toRadians(degrees));
            trans.mul(rot);
            
            rotatingTG.setTransform(trans);
        }
    }

    public static BranchGroup create_Scene() {
        BranchGroup sceneBG = new BranchGroup();
        TransformGroup sceneTG = new TransformGroup();
        sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        sceneTG.setCapability(Node.ENABLE_PICK_REPORTING);

        // Create switch between normal/glitched states
        tvSwitch = new Switch();
        tvSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        tvSwitch.addChild(TvScene.createTvSceneBG());   // State 0: Normal TV
        tvSwitch.addChild(TvScene2.createTvSceneBG());  // State 1: Glitched TV
        tvSwitch.setWhichChild(0); // Start with normal state
        
        sceneTG.addChild(tvSwitch);
        sceneBG.addChild(sceneTG);

        // Setup picking
        pickTool = new PickTool(sceneBG);
        pickTool.setMode(PickTool.GEOMETRY);

        // Add lighting
        sceneBG.addChild(createBasicLighting());
        sceneBG.compile();
        
        return sceneBG;
    }

    private static Node createBasicLighting() {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0), 1000);
        
        DirectionalLight light1 = new DirectionalLight(
            new Color3f(1f, 1f, 1f), // Color
            new Vector3f(-1f, -1f, -1f)); // Direction
        light1.setInfluencingBounds(bounds);
        
        DirectionalLight light2 = new DirectionalLight(
            new Color3f(0.5f, 0.5f, 0.5f),
            new Vector3f(1f, 1f, 1f));
        light2.setInfluencingBounds(bounds);
        
        AmbientLight ambient = new AmbientLight(new Color3f(0.3f, 0.3f, 0.3f));
        ambient.setInfluencingBounds(bounds);
        
        BranchGroup lights = new BranchGroup();
        lights.addChild(light1);
        lights.addChild(light2);
        lights.addChild(ambient);
        
        return lights;
    }
}