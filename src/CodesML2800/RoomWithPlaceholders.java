package CodesML2800;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

public class RoomWithPlaceholders extends JPanel {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;

    public static BranchGroup create_Scene() {
        BranchGroup sceneBG = new BranchGroup();
        TransformGroup sceneTG = new TransformGroup();

        // Create room (5 walls, open front)
        Appearance wallAppearance = CommonsML.set_Appearance(CommonsML.Grey);
        sceneTG.addChild(createWall(new Vector3d(0, -0.5, 0), new Vector3d(1.0, 0.05, 1.0), wallAppearance)); // Floor
        //sceneTG.addChild(createWall(new Vector3d(0, 0.5, 0), new Vector3d(1.0, 0.05, 1.0), wallAppearance));  // Ceiling
        sceneTG.addChild(createWall(new Vector3d(-1.0, 0, 0), new Vector3d(0.05, 0.5, 1.0), wallAppearance)); // Left Wall
        //sceneTG.addChild(createWall(new Vector3d(1.0, 0, 0), new Vector3d(0.05, 0.5, 1.0), wallAppearance));  // Right Wall
        sceneTG.addChild(createWall(new Vector3d(0, 0, -1.0), new Vector3d(1.0, 0.5, 0.05), wallAppearance)); // Back Wall

        // Load the violin model
        sceneTG.addChild(loadViolinModel());

        sceneBG.addChild(sceneTG);
        sceneBG.addChild(CommonsML.add_Lights(CommonsML.White, 1));

        return sceneBG;
    }

    private static TransformGroup createWall(Vector3d position, Vector3d scale, Appearance appearance) {
        Transform3D transform = new Transform3D();
        transform.setTranslation(position);
        transform.setScale(scale);

        TransformGroup wallTG = new TransformGroup(transform);
        wallTG.addChild(new Box(1.0f, 1.0f, 1.0f, Box.GENERATE_NORMALS, appearance));
        return wallTG;
    }

    private static TransformGroup loadViolinModel() {
        TransformGroup violinTG = new TransformGroup();

        try {
            ObjectFile loader = new ObjectFile(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
            Scene violinScene = loader.load(new FileReader("models/Pillow.obj"));

            // Create Appearance with Texture
            Appearance color = new Appearance();
            TextureLoader textureLoader = new TextureLoader("textures/pillow_texture.jpg", "RGB", new Canvas3D(SimpleUniverse.getPreferredConfiguration()));
            Texture violinTexture = textureLoader.getTexture();
            color.setTexture(violinTexture);

            // Apply the texture to all Shape3D objects in the Violin model
            Iterator<Node> children = violinScene.getSceneGroup().getAllChildren();
            while (children.hasNext()) {
                Node obj = children.next();
                if (obj instanceof Shape3D) {
                    ((Shape3D) obj).setAppearance(color);
                }
            }

            // Scale and position the violin
            Transform3D scale = new Transform3D();
            scale.setScale(0.5); // Increase if still small
            Transform3D move = new Transform3D();
            move.setTranslation(new Vector3d(0.0, 0.2, 0.0));

            Transform3D finalTransform = new Transform3D();
            finalTransform.mul(move);
            finalTransform.mul(scale);

            TransformGroup transformGroup = new TransformGroup(finalTransform);
            transformGroup.addChild(violinScene.getSceneGroup());
            violinTG.addChild(transformGroup);

        } catch (IOException e) {
            System.out.println("Error loading obj: " + e.getMessage());
        }

        return violinTG;
    }


    public RoomWithPlaceholders(BranchGroup sceneBG) {
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        SimpleUniverse su = new SimpleUniverse(canvas);
        CommonsML.define_Viewer(su, new Point3d(2.5, 1.5, 3.0));

        sceneBG.compile();
        su.addBranchGraph(sceneBG);

        setLayout(new BorderLayout());
        add("Center", canvas);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        frame = new JFrame("Room with Placeholders and Violin");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new RoomWithPlaceholders(create_Scene()));
    }
}