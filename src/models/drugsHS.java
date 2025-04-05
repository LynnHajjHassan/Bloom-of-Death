package models;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

public class drugsHS extends BaseShapesHS {

    public drugsHS() {
        // Create the scene
    	 TransformGroup translationTG = new TransformGroup();
 	    TransformGroup rotationTG = new TransformGroup();
 	    TransformGroup scaleTG = new TransformGroup();
 	    
 	    // Set up the transformations
 	    Transform3D translation = new Transform3D();
 	    translation.setTranslation(new Vector3d(2d, -17.4d, 24d));
 	    translationTG.setTransform(translation);
 	    
 	    Transform3D rotation = new Transform3D();
 	    rotation.rotY(Math.toRadians(180)); // Rotate around Y-axis
 	    rotationTG.setTransform(rotation);
 	    
 	    Transform3D scaling = new Transform3D();
 	    scaling.setScale(2.0);
 	    scaleTG.setTransform(scaling);
 	    
 	    // Build the hierarchy
 	    translationTG.addChild(rotationTG);
 	    rotationTG.addChild(scaleTG);
 	    scaleTG.addChild(create_Object());
 	    
 	    objTG = translationTG;
 	}

    @Override
    protected Node create_Object() {
        // Create a TransformGroup to hold all components
        TransformGroup sceneTG = new TransformGroup();

        // Create the pill bottle (on the ground)
        Node pillBottle = createPillBottle();
        sceneTG.addChild(pillBottle);

        // Create the cap (on the ground beside the bottle)
        Node cap = createCap();
        sceneTG.addChild(cap);

        // Define the safe radius for pill placement
        float bottleRadius = 0.2f; // Radius of the bottle
        float safeRadius = bottleRadius + 0.1f; // Safe radius (buffer of 0.1f)

        // Create and scatter pills on the ground around the bottle
        for (int i = 0; i < 10; i++) {
            Node pill = createPill();

            // Randomize the position using polar coordinates
            double angle = Math.random() * 2 * Math.PI; // Random angle (0 to 2π)
            double distance = safeRadius + Math.random() * 0.2f; // Random distance within safe radius

            // Convert polar coordinates to Cartesian coordinates
            double x = distance * Math.cos(angle);
            double z = distance * Math.sin(angle);

            // Position the pill on the ground
            Transform3D pillTransform = new Transform3D();
            pillTransform.setTranslation(new Vector3d(x, 0.0, z)); // Y position is 0 (on the ground)
            TransformGroup pillTG = new TransformGroup(pillTransform);
            pillTG.addChild(pill);

            sceneTG.addChild(pillTG);
        }

        return sceneTG;
    }
    
    

   private Node createPillBottle() {
        // Create a brown appearance for the bottle
        Appearance brownApp = createAppearance("textures/drugs.jpg");

        // Create the bottle body
        Cylinder body = new Cylinder(0.2f, 0.6f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, brownApp);

        // Create the rim (open top)
        Cylinder rim = new Cylinder(0.18f, 0.02f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, brownApp);
        Transform3D rimTransform = new Transform3D();
        rimTransform.setTranslation(new Vector3d(0.0, 0.31, 0.0)); // Position the rim
        TransformGroup rimTG = new TransformGroup(rimTransform);
        rimTG.addChild(rim);

        // Combine the body and rim
        TransformGroup bottleTG = new TransformGroup();
        bottleTG.addChild(body);
        bottleTG.addChild(rimTG);

        // Rotate the bottle to lie on its left side (90 degrees around the Z-axis)
        Transform3D rotation = new Transform3D();
        rotation.rotZ(Math.PI / 2); // Rotate 90 degrees around the Z-axis

        // Position the bottle on the ground
        Transform3D bottleTransform = new Transform3D();
        bottleTransform.setTranslation(new Vector3d(0.0, 0.2, 0.0)); // Move the bottle down
        bottleTransform.mul(rotation); // Apply rotation
        TransformGroup bottleGroup = new TransformGroup(bottleTransform);
        bottleGroup.addChild(bottleTG);

        return bottleGroup;
    }

    private Node createCap() {
        // Create a metallic appearance for the cap
        Appearance metallicApp = new Appearance();
        Material material = new Material();
        material.setDiffuseColor(new Color3f(0.8f, 0.8f, 0.8f)); // Light gray (metallic)
        material.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f)); // White specular highlights
        material.setShininess(64.0f); // High shininess
        metallicApp.setMaterial(material);

        // Create the cap
        Cylinder cap = new Cylinder(0.18f, 0.1f, Primitive.GENERATE_NORMALS, metallicApp);

        // Position the cap on the ground beside the bottle
        Transform3D capTransform = new Transform3D();
        capTransform.setTranslation(new Vector3d(-0.7, 0.05, 0.0)); // Move the cap down
        TransformGroup capTG = new TransformGroup(capTransform);
        capTG.addChild(cap);

        return capTG;
    }

    private Node createPill() {
        // Create appearances for red and yellow
        Appearance redApp = new Appearance();
        Material redMaterial = new Material();
        redMaterial.setDiffuseColor(new Color3f(1.0f, 0.0f, 0.0f)); // Red color
        redMaterial.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f)); // White specular highlights
        redMaterial.setShininess(64.0f); // High shininess
        redApp.setMaterial(redMaterial);

        Appearance yellowApp = new Appearance();
        Material yellowMaterial = new Material();
        yellowMaterial.setDiffuseColor(new Color3f(1.0f, 1.0f, 0.0f)); // Yellow color
        yellowMaterial.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f)); // White specular highlights
        yellowMaterial.setShininess(64.0f); // High shininess
        yellowApp.setMaterial(yellowMaterial);

        // Create the pill components
        float pillRadius = 0.04f; // Radius of the pill
        float pillLength = 0.15f;  // Length of the pill (cylinder part)

        // Create the cylinder for the middle part
        Cylinder middle = new Cylinder(pillRadius, pillLength, Primitive.GENERATE_NORMALS, redApp);

        // Create the spheres for the rounded ends
        Sphere topEnd = new Sphere(pillRadius, Primitive.GENERATE_NORMALS, yellowApp); // Top half (yellow)
        Sphere bottomEnd = new Sphere(pillRadius, Primitive.GENERATE_NORMALS, yellowApp); // Bottom half (red)

        // Position the spheres on top and bottom
        Transform3D topTransform = new Transform3D();
        topTransform.setTranslation(new Vector3f(0.0f, pillLength / 2, 0.0f)); // Top sphere
        TransformGroup topTG = new TransformGroup(topTransform);
        topTG.addChild(topEnd);

        Transform3D bottomTransform = new Transform3D();
        bottomTransform.setTranslation(new Vector3f(0.0f, -pillLength / 2, 0.0f)); // Bottom sphere
        TransformGroup bottomTG = new TransformGroup(bottomTransform);
        bottomTG.addChild(bottomEnd);

        // Combine the parts into a TransformGroup
        TransformGroup pillTG = new TransformGroup();
        pillTG.addChild(topTG);
        pillTG.addChild(bottomTG);
        pillTG.addChild(middle);

        // Rotate the pill to lie horizontally (90 degrees around the Z-axis)
        Transform3D rotation = new Transform3D();
        rotation.rotZ(Math.PI / 2); // Rotate 90 degrees around the Z-axis
        TransformGroup rotatedPillTG = new TransformGroup(rotation);
        rotatedPillTG.addChild(pillTG);

        return rotatedPillTG;
    }

    private Appearance createAppearance(String texturePath) {
        Appearance appearance = new Appearance();

        // Load texture
        TextureLoader loader = new TextureLoader(texturePath, null);
        Texture texture = loader.getTexture();
        if (texture != null) {
            appearance.setTexture(texture);
        }

        // Set material properties
        Material material = new Material();
        material.setDiffuseColor(new Color3f(0.6f, 0.4f, 0.2f)); // Brown color
        material.setSpecularColor(new Color3f(0.8f, 0.8f, 0.8f)); // White specular highlights
        material.setShininess(32.0f); // Moderate shininess
        appearance.setMaterial(material);

        return appearance;
    }
}
