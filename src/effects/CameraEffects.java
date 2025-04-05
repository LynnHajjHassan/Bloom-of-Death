package effects;

import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.java3d.*;

import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.vecmath.*;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CameraEffects {
    private static  TransformGroup viewTG;
    private static final Random random = new Random();
    private static  final Timer timer = new Timer();
    private static boolean isShaking = false;
    private static Transform3D current = new Transform3D();
    
    
    public void blurCamera() {
        System.out.println("Camera blur triggered!");

        Appearance blurApp = new Appearance();

        TransparencyAttributes transAttr = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.6f);
        transAttr.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
        blurApp.setTransparencyAttributes(transAttr);

        PolygonAttributes polyAttr = new PolygonAttributes();
        polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
        blurApp.setPolygonAttributes(polyAttr);

        ColoringAttributes colorAttr = new ColoringAttributes(
            new Color3f(0.6f, 0.0f, 0.0f), ColoringAttributes.NICEST);
        blurApp.setColoringAttributes(colorAttr);

        Box blurBox = new Box(1.5f, 1.5f, 0.01f, blurApp); // Big enough to fill screen view

        Transform3D tr = new Transform3D();
        tr.setTranslation(new Vector3f(0f, 0f, -2f)); // Push in front of the camera

        TransformGroup blurTG = new TransformGroup(tr);
        blurTG.addChild(blurBox);

        blurTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        blurTG.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        blurTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        blurTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        BranchGroup blurBG = new BranchGroup();
        blurBG.setCapability(BranchGroup.ALLOW_DETACH);
        blurBG.addChild(blurTG);

        viewTG.addChild(blurBG);  // Attach to camera
        // Gradually fade out the blur box over time
        Timer fadeTimer = new Timer();
        final float[] alpha = {0.6f}; // starting transparency
        final TransparencyAttributes ta = transAttr;

        fadeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                alpha[0] += 0.05f;
                if (alpha[0] <= 0f) {
                    alpha[0] = 0f;
                    ta.setTransparency(alpha[0]);
                    blurBG.detach(); // remove blur panel
                    fadeTimer.cancel();
                } else {
                    ta.setTransparency(alpha[0]);
                }
            }
        }, 1000, 1500); // start fading after 150ms, update every 100ms
    }

    public CameraEffects(TransformGroup viewTG) {
        this.viewTG = viewTG;
    }

    public static void shakeCamera(double intensity, int durationMs) {
        if (isShaking) return;
        isShaking = true;

        final long startTime = System.currentTimeMillis();
        final int interval = 1; // very fast updates for aggressive shake

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTime;
                if (elapsed > durationMs) {
                    stopShake();
                    return;
                }

                applyShake(intensity);
            }
        }, 0, interval);
    }


    private static void applyShake(double intensity) {
        
        viewTG.getTransform(current);

        Vector3d pos = new Vector3d();
        current.get(pos);

        double jitterX = (random.nextDouble() - 0.5) * intensity;
        double jitterY = (random.nextDouble() - 0.5) * intensity;

        pos.x += jitterX;
        pos.y += jitterY;

        current.setTranslation(pos);
        viewTG.setTransform(current);
    }

    private static void stopShake() {
        timer.purge(); // remove scheduled tasks
        isShaking = false;
    }
}
