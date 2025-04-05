package models;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class ClockMain extends JPanel {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private static final int OBJ_NUM = 20;
    private static ClockObjects4[] object3D = new ClockObjects4[OBJ_NUM];

    private static HourHand hourHand;
    private static MinuteHand minuteHand;

    private static ClockMain instance;

    public static ClockMain getInstance() {
        return instance;
    }

    public static TransformGroup create_ClockHS() {
        Transform3D scale = new Transform3D();
        scale.setScale(3.7);

        Transform3D rotation = new Transform3D();
        rotation.setRotation(new AxisAngle4d(0, 1, 0, 0));

        Transform3D translation = new Transform3D();
        translation.setTranslation(new Vector3f(-5.0f, 1.9f, -25.0f));

        Transform3D combined = new Transform3D();
        combined.mul(scale);
        combined.mul(rotation);
        combined.mul(translation);

        TransformGroup rootTG = new TransformGroup(combined);
        rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        TransformGroup fanTG = new TransformGroup();

        object3D[0] = new ClockBase();
        fanTG = object3D[0].position_Object();

        object3D[1] = new ClockHouse();
        object3D[0].add_Child(object3D[1].position_Object());

        object3D[2] = new ClockCenter();
        object3D[1].add_Child(object3D[2].position_Object());

        object3D[3] = new ClockTop();
        object3D[1].add_Child(object3D[3].position_Object());

        object3D[4] = new ClockSide1();
        object3D[3].add_Child(object3D[4].position_Object());

        object3D[5] = new ClockSide2();
        object3D[3].add_Child(object3D[5].position_Object());

        object3D[6] = new ClockWindowobj();
        object3D[1].add_Child(object3D[6].position_Object());

        object3D[7] = new ClockTree1();
        object3D[1].add_Child(object3D[7].position_Object());

        object3D[8] = new TreeBase1();
        object3D[7].add_Child(object3D[8].position_Object());

        object3D[9] = new ClockTree2();
        object3D[1].add_Child(object3D[9].position_Object());

        object3D[10] = new TreeBase2();
        object3D[9].add_Child(object3D[10].position_Object());

        // Clock hands
        object3D[11] = hourHand = new HourHand();
        object3D[2].add_Child(hourHand.position_Object());

        object3D[12] = minuteHand = new MinuteHand();
        object3D[2].add_Child(minuteHand.position_Object());

        object3D[13] = new ClockDong();
        object3D[0].add_Child(object3D[13].position_Object());

        rootTG.addChild(fanTG);

        return rootTG;
    }

    public static BranchGroup create_Scene() {
        BranchGroup sceneBG = new BranchGroup();
        TransformGroup sceneTG = new TransformGroup();
        sceneTG.addChild(create_ClockHS());
        sceneBG.addChild(sceneTG);
        return sceneBG;
    }

    public ClockMain(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        SimpleUniverse su = new SimpleUniverse(canvas);
        Commons.define_Viewer(su, new Point3d(0.25d, 0.25d, 10.0d));
        View view = su.getViewer().getView();
        view.setBackClipDistance(10.0);
        view.setFrontClipDistance(0.1);

        sceneBG.compile();
        su.addBranchGraph(sceneBG);
        setLayout(new BorderLayout());
        add("Center", canvas);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        frame = new JFrame("HS's Assignment");
        frame.getContentPane().add(new ClockMain(create_Scene()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Manual control method
    public static void moveClockHandsTo(int hour, int minute) {
        if (hourHand != null && minuteHand != null) {
            hourHand.updateRotation(hour);
            minuteHand.updateRotation(minute);
        }
    }
}
