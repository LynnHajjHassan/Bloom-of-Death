package models;

import org.jogamp.java3d.*;

import org.jogamp.vecmath.*;



public class TvScene {



    /**

     * Assembles the TV scene and returns it as a BranchGroup.

     */

    public static BranchGroup createTvSceneBG() {

        // Create the TV frame

        TVFrame frame = new TVFrame();

        TransformGroup tvTG = frame.position_Object();



        // Create the TV screen and attach it to the frame

        TVScreen screen = new TVScreen();

        frame.add_Child(screen.position_Object());


        // Create the scene BranchGroup and add the assembled TV

        BranchGroup sceneBG = new BranchGroup();

        sceneBG.addChild(tvTG);

        sceneBG.compile();

        return sceneBG;

    }

}





// -------------------------------------------------------------------------

// TVFrame: Represents the outer frame (shell) of the TV.

class TVFrame extends BODObjects {

    public TVFrame() {

        // Set scale and position for the frame (adjust as needed)

        scale = 1.0;

        post = new Vector3f(0f, 0f, 0f);

        // Load the TV frame OBJ file (expects "objects/tv_frame.obj")

        transform_Object("tv");

        create_Appearance();

    }

    

    protected void create_Appearance() {

        // Define material colors for the frame

        mtl_clr[0] = new Color3f(0.2f, 0.2f, 0.2f); // Ambient

        mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); // Diffuse

        mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f);  // Specular

        mtl_clr[3] = new Color3f(0f, 0f, 0f);         // Emissive

        // Apply these colors to the appearance

        obj_Appearance();

        // Set the texture for the frame. This will try to load "textures/tv_frame_texture.png/jpg/jpeg"

        app.setTexture(textured_App("tv_frame_texture"));

    }

    

    @Override

    public TransformGroup position_Object() {

        // Attach the loaded object (objBG) to the transformation group (objTG)

        objTG.addChild(objBG);

        return objTG;

    }

    

    @Override

    public void add_Child(TransformGroup nextTG) {

        // Attach child objects (for example, the TV screen) to this object

        objTG.addChild(nextTG);

    }

}





// -------------------------------------------------------------------------

// TVScreen: Represents the TV screen (the display part).

class TVScreen extends BODObjects {

    public TVScreen() {

        scale = 1.0;

        // Position the screen slightly in front of the frame

        post = new Vector3f(0f, 0f, 0.1f);

        // Load the TV screen OBJ file (expects "objects/tv_screen.obj")

        transform_Object("tv_screen");

        create_Appearance();

    }

    

    protected void create_Appearance() {

        // Use neutral white colors for the screen

        mtl_clr[0] = new Color3f(1.0f, 1.0f, 1.0f);

        mtl_clr[1] = new Color3f(1.0f, 1.0f, 1.0f);

        mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f);

        mtl_clr[3] = new Color3f(0f, 0f, 0f);

        obj_Appearance();

        // Set the texture for the screen (expects "textures/tv_screen_texture.png/jpg/jpeg")

        app.setTexture(textured_App("blank"));

    }

    

    @Override

    public TransformGroup position_Object() {

        objTG.addChild(objBG);

        return objTG;

    }

    

    @Override

    public void add_Child(TransformGroup nextTG) {

        objTG.addChild(nextTG);

    }

}