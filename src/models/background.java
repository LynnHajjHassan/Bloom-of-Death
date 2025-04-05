package models;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PointLight;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

// This class will be used to create a room for the project 
public class background {

    // Room dimensions
    protected static double roomWidth = 50.0;
    protected static double roomLength = 100.0;
    protected static double roomHeight = 35.0;

    // Method to connect the room's walls and windows
    public static TransformGroup createWindowBackground () {

        TransformGroup windowTG = new TransformGroup();

        // Appearance for walls, floor, ceiling, and windows
        
        Appearance windowAppearance = get_Appearance("view2", 1f);  // Glass texture for windows


        // Add windows to the room (positions and sizes are arbitrary, can be adjusted)
        //roomTG.addChild(createWindow(new Vector3d(-15, 5, 50), new Vector3d(5, 4, 0.05), windowAppearance));  // Window on the front wall
        windowTG.addChild(createWindow(new Vector3d(-47.5f, 6f, -16f), new Vector3d(4.9, 8, 0.05), windowAppearance));   // Another window on the front wall

       

        return windowTG;
    }

    private static TransformGroup createWindow(Vector3d position, Vector3d scale, Appearance appearance) {
        // Create Transform3D for scaling
        Transform3D scaleTransform = new Transform3D();
        scaleTransform.setScale(scale);

        // Create Transform3D for rotation
        Transform3D rotation = new Transform3D();
        rotation.rotY(Math.toRadians(90)); // Rotate 45 degrees around Y-axis

        // Combine scale and rotation
        rotation.mul(scaleTransform); // rotation * scale

        // Apply translation
        Transform3D finalTransform = new Transform3D();
        finalTransform.setTranslation(position);
        finalTransform.mul(rotation); // translation * (rotation * scale)

        // Create TransformGroup with final transformation
        TransformGroup windowTG = new TransformGroup(finalTransform);

        // Add the Box
        windowTG.addChild(new Box(1.0f, 1.0f, 1.0f,
                Box.GENERATE_NORMALS | Box.GENERATE_TEXTURE_COORDS, appearance));

        return windowTG;
    }

   
   
    
    
    private static Appearance get_Appearance(String wallName, float scl) {
        Appearance app = new Appearance();

        // Set material properties for shiny wood
        int shine = 16; // Lower shininess for a matte finish (walls are not very shiny)
        Color3f[] mtl_clr = {
                new Color3f(0.8f, 0.8f, 0.8f), // Ambient color (light gray to avoid overly bright walls)
                new Color3f(0.9f, 0.9f, 0.9f), // Diffuse color (slightly off-white for a natural look)
                new Color3f(0.2f, 0.2f, 0.2f), // Specular color (very low for subtle highlights)
                new Color3f(0.0f, 0.0f, 0.0f)  // Emissive color (black, no glow)
        };

        Material mtl = new Material();
        mtl.setShininess(shine); // Set shininess (lower for matte walls)
        mtl.setAmbientColor(mtl_clr[0]);
        mtl.setDiffuseColor(mtl_clr[1]);
        mtl.setSpecularColor(mtl_clr[2]); // Set specular color for subtle highlights
        mtl.setEmissiveColor(mtl_clr[3]);
        mtl.setLightingEnable(true); // Enable lighting
        app.setMaterial(mtl);

        // Enable texture attributes
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE mixes texture with material

        // Scale the texture if needed
        Transform3D transMap = new Transform3D();
        Vector3d scale = new Vector3d(scl, scl, scl);
        transMap.setScale(scale);
        texAttr.setTextureTransform(transMap);

        // Apply texture attributes to the appearance
        app.setTextureAttributes(texAttr);
        System.out.println("Applying texture to wall: " + wallName);
        app.setTexture(load_Texture(wallName)); // Load the texture

        return app;
    }

    private static Texture load_Texture(String name) {
        String filename = "textures/" + name + ".jpg"; // tell the folder of the image
        TextureLoader loader = new TextureLoader(filename, null);
        ImageComponent2D image = loader.getImage(); // load the image
        if (image == null)
            System.out.println("Cannot load file: " + filename);

        Texture2D texture = new Texture2D(Texture.BASE_LEVEL,
                Texture.RGBA, image.getWidth(), image.getHeight());
        texture.setImage(0, image); // set image for the texture

        return texture;
    }
}
