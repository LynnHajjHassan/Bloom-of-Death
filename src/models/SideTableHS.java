package models;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class SideTableHS extends JPanel {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;

	public static TransformGroup create_TableHS() {
        TransformGroup sceneTG = new TransformGroup();
        sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // Create the side table
        SideTableObject table = new SideTableObject(); // Create the side table
        sceneTG.addChild(table.position_Object());

        // Add lighting
       // sceneTG.addChild(Commons.add_Lights(Commons.White, 1));
		//sceneBG.addChild(CommonsHS.rotate_Behavior(7500, sceneTG));	

        // Add the side table to the scene

        return sceneTG;
    }
	public SideTableHS(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        SimpleUniverse su = new SimpleUniverse(canvas);

        // Set the camera view to see the side table from the front
        Commons.define_Viewer(su, new Point3d(0.0d, 0.5d, 3.0d)); // Adjusted for a better view

        // Compile and add the scene to the universe
        sceneBG.compile();
        su.addBranchGraph(sceneBG);

        // Set up the JFrame
        setLayout(new BorderLayout());
        add("Center", canvas);

        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        frame = new JFrame("Side Table Object 2");
      //  frame.getContentPane().add(new ChairObject1(create_ChairHS()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}





class SideTableObject extends BaseShapesHS {

    public SideTableObject() {
        // Create the tabletop
    	TransformGroup translationTG = new TransformGroup();
	    TransformGroup rotationTG = new TransformGroup();
	    TransformGroup scaleTG = new TransformGroup();
	    
	    // Set up the transformations
	    Transform3D translation = new Transform3D();
	    translation.setTranslation(new Vector3d(42d, -9.5d, 40.0d));
	    translationTG.setTransform(translation);
	    
	    Transform3D rotation = new Transform3D();
	    rotation.rotY(Math.toRadians(100)); // Rotate around Y-axis
	    rotationTG.setTransform(rotation);
	    
	    Transform3D scaling = new Transform3D();
	    scaling.setScale(13.0);//before was 8
	    scaleTG.setTransform(scaling);
	    
	    // Build the hierarchy
	    translationTG.addChild(rotationTG);
	    rotationTG.addChild(scaleTG);
	    scaleTG.addChild(create_Object());
	    
	    objTG = translationTG;
    }

    @Override
    protected Node create_Object() {
        // Create a warm, natural brown appearance
        Appearance wickerApp = createWickerAppearance();

        // Create the circular tabletop (smaller radius)
        Cylinder tabletop = new Cylinder(0.4f, 0.05f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, wickerApp);

        // Create the raised edge with a braided pattern (smaller radius)
        Cylinder edge = new Cylinder(0.45f, 0.02f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, wickerApp);
        Transform3D edgeTransform = new Transform3D();
        edgeTransform.setTranslation(new Vector3d(0.0, 0.06, 0.0)); // Position the edge slightly above the tabletop
        TransformGroup edgeTG = new TransformGroup(edgeTransform);
        edgeTG.addChild(edge);

        // Create the legs (taller height)
        TransformGroup legsTG = new TransformGroup();
        double angleIncrement = 2 * Math.PI / 4; // 90 degrees between legs
        double legRadius = 0.35; // Distance from the center of the tabletop to the legs

        for (int i = 0; i < 4; i++) {
            double angle = i * angleIncrement;
            double x = legRadius * Math.cos(angle);
            double z = legRadius * Math.sin(angle);

            // Leg starting from the bottom of the tabletop
            Cylinder leg = new Cylinder(0.03f, 0.7f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, wickerApp);
            Transform3D legTransform = new Transform3D();
            legTransform.setTranslation(new Vector3d(x, -0.35, z)); // Adjusted so legs start from the tabletop bottom
            TransformGroup legTG = new TransformGroup(legTransform);
            legTG.addChild(leg);
            legsTG.addChild(legTG);
        }


        // Create the lower shelf (adjusted position)
        Cylinder lowerShelf = new Cylinder(0.35f, 0.03f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, wickerApp);
        Transform3D shelfTransform = new Transform3D();
        shelfTransform.setTranslation(new Vector3d(0.0, -0.5, 0.0)); // Position the shelf lower
        TransformGroup shelfTG = new TransformGroup(shelfTransform);
        shelfTG.addChild(lowerShelf);

        // Combine all parts into a single TransformGroup
        TransformGroup tableTG = new TransformGroup();
        tableTG.addChild(tabletop);
        tableTG.addChild(edgeTG);
        tableTG.addChild(legsTG);
        tableTG.addChild(shelfTG);

        return tableTG;
    }

    private Appearance createWickerAppearance() {
        Appearance appearance = new Appearance();

        // Load wicker texture (ensure the file is in the "images" folder)
        TextureLoader loader = new TextureLoader("textures/why.jpg", null);
        Texture texture = loader.getTexture();
        if (texture != null) {
            appearance.setTexture(texture);
        }

        // Set material properties
        Material material = new Material();
        material.setDiffuseColor(new Color3f(0.6f, 0.4f, 0.2f)); // Warm brown color
        material.setSpecularColor(new Color3f(0.8f, 0.8f, 0.8f)); // White specular highlights
        material.setShininess(32.0f); // Moderate shininess
        appearance.setMaterial(material);

        return appearance;
    }
}