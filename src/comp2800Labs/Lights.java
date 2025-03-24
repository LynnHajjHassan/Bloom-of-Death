package comp2800Labs;

import org.jogamp.java3d.AmbientLight;
import org.jogamp.java3d.Background;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.DirectionalLight;
import org.jogamp.java3d.ExponentialFog;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

public class Lights {
	
	protected static void setupSceneEffects(TransformGroup sceneTG) {
	    // Add ambient light
	    AmbientLight ambientLight = createAmbientLight(new Color3f(0.1f, 0.1f, 0.1f), 100.0);
	    sceneTG.addChild(ambientLight);

	    // Add a faint directional light
	    DirectionalLight directionalLight = createDirectionalLight(
	        new Color3f(0.3f, 0.2f, 0.2f), // Light color
	        new Vector3f(-1.0f, -1.0f, -0.5f), // Light direction
	        100.0 // Influencing bounds radius
	    );
	    sceneTG.addChild(directionalLight);

	    // Add fog
	    ExponentialFog fog = createExponentialFog(
	        new Color3f(0.0f, 0.0f, 0.0f), // Fog color
	        0.005f, // Fog density
	        100.0 // Influencing bounds radius
	    );
	    sceneTG.addChild(fog);

	    // Set background color
	    Background background = createBackground(
	        new Color3f(0.0f, 0.0f, 0.0f), // Background color
	        100.0 // Influencing bounds radius
	    );
	    sceneTG.addChild(background);
	}
	
	private static AmbientLight createAmbientLight(Color3f color, double boundsRadius) {
	    AmbientLight ambientLight = new AmbientLight(color);
	    ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), boundsRadius));
	    return ambientLight;
	}
	
	private static DirectionalLight createDirectionalLight(Color3f color, Vector3f direction, double boundsRadius) {
	    DirectionalLight directionalLight = new DirectionalLight();
	    directionalLight.setColor(color);
	    directionalLight.setDirection(direction);
	    directionalLight.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), boundsRadius));
	    return directionalLight;
	}
	
	private static  ExponentialFog createExponentialFog(Color3f color, double density, double boundsRadius) {
	    ExponentialFog fog = new ExponentialFog(color, (float) density);
	    fog.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), boundsRadius));
	    return fog;
	}
	
	private static Background createBackground(Color3f color, double boundsRadius) {
	    Background background = new Background(color);
	    background.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), boundsRadius));
	    return background;
	}

}



/*
// Setting up the light resource, and applying it to the TableLamp sceneBG
// Add ambient light
AmbientLight ambientLight = new AmbientLight(new Color3f(1.0f, 1.0f, 1.0f));
ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
sceneBG.addChild(ambientLight);
// Add directional light
DirectionalLight directionalLight = new DirectionalLight(
    new Color3f(1.0f, 1.0f, 1.0f), // Light color
    new Vector3f(-1.0f, -1.0f, -1.0f) // Light direction
);
directionalLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
*/

/*
// Create room (3 walls, open front and ceiling)
Appearance wallAppearance = CommonsLH.set_Appearance(CommonsLH.Grey);
sceneTG.addChild(createWall(new Vector3d(0, -0.5, 0.2), new Vector3d(1.2, 0.05, 1.2), wallAppearance)); // Floor
sceneTG.addChild(createWall(new Vector3d(-1.2, 0.05, 0.2), new Vector3d(0.05, 0.6, 1.2), wallAppearance)); // Left Wall
sceneTG.addChild(createWall(new Vector3d(1.2, 0.05, 0.2), new Vector3d(0.05, 0.6, 1.2), wallAppearance)); // Right Wall
sceneTG.addChild(createWall(new Vector3d(0, 0.05, -1.0), new Vector3d(1.2, 0.6, 0.05), wallAppearance)); // Back Wall
*/
 /*DirectionalLight directionalLight = new DirectionalLight(
		 
new Color3f(1.0f, 1.0f, 1.0f), // Full intensity white light
new Vector3f(-0.5f, -1.0f, -0.5f) // Slightly angled direction
);
directionalLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
*/
