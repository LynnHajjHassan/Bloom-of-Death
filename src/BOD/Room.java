package BOD;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Node;
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
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

// This class will be used to create a room for the project 
// To call the method createRoom in your main code use: 
// 1_ TransformGroup roomTG = Room.createRoom() ; 
// 2_ then connect the roomTG to your sceneTG using sceneTG.addChild(roomTG)
// 3_ attach everything else ( room objects ) to roomTG. 
// example : roomTG.addChild(LampTG)

// Dependencies: BODObjects class. 

public class Room {
	
	// Room dimensions
    protected static double roomWidth = 50.0;  
    protected static double roomLength = 100.0; 
    protected static double roomHeight = 25.0;
        
	
	// Method used to connect the room's walls.
	// Input: nothing, Output: TransformGroup roomTG 
	protected static TransformGroup createEmptyRoom() {
		
		 TransformGroup roomTG = new TransformGroup();

		    // Appearance for walls, floor, and ceiling
		    Appearance wallsAppearance = get_Appearance("wall2" ,3f) ;
		    Appearance floorAppearance = get_Appearance("Floor3", 4f) ;
		    Appearance ceilingAppearance = get_Appearance("wall2", 3f); 
		    
		   // Appearance wallAppearance = CommonsLH.set_Appearance(CommonsLH.Grey);  // Grey walls
	       // Appearance floorAppearance = CommonsLH.set_Appearance(CommonsLH.Grey); 
		   // Appearance ceilingAppearance = CommonsLH.set_Appearance(CommonsLH.Grey); // White ceiling

		 
		    // Floor
		    roomTG.addChild(createWall(
		        new Vector3d(0, -18, 0), // Position: Centered at the bottom
		        new Vector3d(roomWidth, 0.05, roomLength), // Scale: Wide and long, but thin
		        floorAppearance
		    ));

		    // Left Wall
		    roomTG.addChild(createWall(
		        new Vector3d(-roomWidth, -2, 0), // Position: Left side of the room
		        new Vector3d(0.05, roomHeight, roomLength), // Scale: Tall and long, but thin
		        wallsAppearance
		    ));

		    // Right Wall
		    roomTG.addChild(createWall(
		        new Vector3d( roomWidth , -2, 0), // Position: Right side of the room
		        new Vector3d(0.05, roomHeight, roomLength), // Scale: Tall and long, but thin
		        wallsAppearance
		    ));

		    // Back Wall
		    roomTG.addChild(createWall(
		        new Vector3d(0, -2, -roomLength ), // Position: Back side of the room
		        new Vector3d(roomWidth, roomHeight, 0.05), // Scale: Wide and tall, but thin
		        wallsAppearance
		    ));
	
		    // Front Wall
		    roomTG.addChild(createWall(
		        new Vector3d(0, -2 , roomLength), // Position: Front side of the room
		        new Vector3d(roomWidth, roomHeight, 0.05), // Scale: Wide and tall, but thin
		        wallsAppearance
		    ));
	
		    // Ceiling
		    roomTG.addChild(createWall(
		        new Vector3d(0,14, 0), 
		        new Vector3d(roomWidth, 0.05, roomLength), // Scale: Wide and long, but thin
		        ceilingAppearance
		    ));

		    return roomTG;
	}
	

		private static TransformGroup createWall(Vector3d position, Vector3d scale, Appearance appearance) {
		    // Create a Transform3D to set the position and scale of the wall
		    Transform3D transform = new Transform3D();
		    transform.setTranslation(position); // Set the position of the wall
		    transform.setScale(scale);         // Set the scale of the wall

		    // Create a TransformGroup to hold the wall
		    TransformGroup wallTG = new TransformGroup(transform);
		    
		    // Create a Box shape for the wall and attach it to the TransformGroup
		    wallTG.addChild(new Box(1.0f, 1.0f, 1.0f, Box.GENERATE_NORMALS | Box.GENERATE_TEXTURE_COORDS, appearance));

		    return wallTG;
		} 
		
		//  get_Appearance inputs : wallName : name of the appearance wall 
		// scale : the scale used for the wall's texture 
		private static Appearance get_Appearance(String wallName, float scl ) {
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
		
		private  static Texture load_Texture(String name) {
			String filename = "textures/" + name + ".jpg";       // tell the folder of the image
			TextureLoader loader = new TextureLoader(filename, null);
			ImageComponent2D image = loader.getImage();        // load the image
			if (image == null)
				System.out.println("Cannot load file: " + filename);

			Texture2D texture = new Texture2D(Texture.BASE_LEVEL,
					Texture.RGBA, image.getWidth(), image.getHeight());
			texture.setImage(0, image);                        // set image for the texture

			return texture;
		}
		

}


