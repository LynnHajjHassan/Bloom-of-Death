package models;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.picking.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class HiddenLetter {
    // Shared variables for mail system (sound removed)
    public static Switch mailSwitch;
    public static TransformGroup mailRootTG;
    public static TransformGroup rotatingTG;
    public static PickTool pickTool;

    public static TransformGroup createMailSystem() {
        BranchGroup mailBG = create_Scene();
        
        // Create transform group for positioning in room
        mailRootTG = new TransformGroup();
        mailRootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        // Group for rotation (child of mailRootTG)
        rotatingTG = new TransformGroup();
        rotatingTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        // Set initial position (adjust these values as needed)
        Transform3D pos = new Transform3D();
        pos.setTranslation(new Vector3f(30f, -15f, 90f));
        pos.setScale(0.5f);
        mailRootTG.setTransform(pos);
        
        // Add the mail scene to rotating group
        rotatingTG.addChild(mailBG);
        
        // Add rotating group to root
        mailRootTG.addChild(rotatingTG);
        
        return mailRootTG;
    }

    public static void toggleMailState() {
        if (mailSwitch != null) {
            int current = mailSwitch.getWhichChild();
            mailSwitch.setWhichChild(current == 0 ? 1 : 0);
            // Sound functionality removed
        }
    }

    public static void rotateMail(float degrees) {
        if (rotatingTG != null) {
            Transform3D trans = new Transform3D();
            rotatingTG.getTransform(trans);
            
            Transform3D rot = new Transform3D();
            // rot.rotY(Math.toRadians(degrees));
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

        mailSwitch = new Switch();
        mailSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        
        // Create mail states with different behaviors
        mailSwitch.addChild(MailScene.createMailBG());    // State 0
        mailSwitch.addChild(OpenMailScene.createOpenMailBG()); // State 1
        mailSwitch.setWhichChild(0); // Start with Mail
        
        sceneTG.addChild(mailSwitch);
        sceneBG.addChild(sceneTG);

        // Setup picking
        pickTool = new PickTool(sceneBG);
        pickTool.setMode(PickTool.GEOMETRY);

        // Add lights
        sceneBG.addChild(createBasicLighting());
        sceneBG.compile();
        
        return sceneBG;
    }

    public static Node createBasicLighting() {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0), 1000);
        
        DirectionalLight light1 = new DirectionalLight(
            new Color3f(1f, 1f, 1f),
            new Vector3f(-1f, -1f, -1f));
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

    // Test method (optional)
    public static void main(String[] args) {
        JFrame frame = new JFrame("Mail System Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JPanel() {
            {
                setLayout(new BorderLayout());
                add("Center", new Canvas3D(SimpleUniverse.getPreferredConfiguration()) {
                    {
                        SimpleUniverse su = new SimpleUniverse(this);
                        su.getViewingPlatform().setNominalViewingTransform();
                    }
                });
            }
        });
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}