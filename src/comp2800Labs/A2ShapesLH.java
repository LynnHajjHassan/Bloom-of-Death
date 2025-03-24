package comp2800Labs;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.ColoringAttributes;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.TexCoordGeneration;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

import com.jogamp.opengl.util.texture.Texture;

public class A2ShapesLH {
	
	public static abstract class RotateObject extends BaseShapesLH{
		
		protected TransformGroup objRG ; 
		
		@Override
		public TransformGroup position_Object() {	          
			return objTG;   
		}
		
		@Override
		protected abstract Node create_Object();    
		
		protected void rotate_Object(int r_num, TransformGroup rot_TG , String shape){
			BoundingSphere hundred_BS = new BoundingSphere(new Point3d(), 100.0);
			Alpha rotationAlpha ; 	
			RotationInterpolator rot_beh = null ; 
			rot_TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			Transform3D rotationAxis = new Transform3D();
			rotationAlpha = new Alpha(-1, r_num);        // continuously rotating at 'r_num'/millisecond
			if ( shape.equals("Yaw") ){  				 // if the shape is Yaw, make it rotate counter clockwise, and around y axis
				rotationAxis.setIdentity();  // Default is Y-axis
		        rot_beh = new RotationInterpolator(rotationAlpha, rot_TG, rotationAxis, 0.0f, (float) Math.PI * 2.0f);  // default java 3d is counter clockwise
			}else if(shape.equals("Blades") ){			// if the shape is blades, make it rotate clockwise, and not around y axis
				rotationAxis.rotZ(Math.PI / 2);  //  use rotZ for Z-axis 
		        rot_beh = new RotationInterpolator(rotationAlpha, rot_TG, rotationAxis, 0.0f, -(float) Math.PI * 2.0f); //added - to make it clockwise
			}
			rot_beh.setSchedulingBounds(hundred_BS);           // start rotation at 0- and end at 360-degrees
			objTG.addChild(rot_beh); //add a rotation behavior 'B' to objTG and objRG
		}
		
		
		public TransformGroup RG_Object() {	// Used to make the code similar to the graph sent by the professor 
			return objRG;   
		}
			
		// Texture methode used to map images on the objects, pretty similar to the one we had in Lab5 in L5Textue
		// This method will 
		protected static Texture2D texture_App(String f_name) {
			String file_name = "images/" + f_name + ".jpg";    // indicate the location of the image
			TextureLoader loader = new TextureLoader(file_name, null);
			ImageComponent2D image = loader.getImage();        // get the image
			if (image == null)
				System.out.println("Cannot load file: " + file_name);

			Texture2D texture = new Texture2D(Texture2D.BASE_LEVEL,
					Texture2D.RGBA, image.getWidth(), image.getHeight());
			texture.setImage(0, image);                        // define the texture with the image

			return texture;		
		} // end of Texture method		
		
		
		// A method added to set the appearance of an object, specifically for the yaw and the blades sphere
		protected static Appearance set_Appearance(String s) {
			Appearance app = CommonsLH.set_Appearance(CommonsLH.White);
			PolygonAttributes pa = new PolygonAttributes();
			pa.setCullFace(PolygonAttributes.CULL_NONE);       // show both sides
			app.setPolygonAttributes(pa);

			//finding object's coordinates 
			TexCoordGeneration tcg = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,
					TexCoordGeneration.TEXTURE_COORDINATE_2);
			
			app.setTexCoordGeneration(tcg);
			app.setTexture(texture_App("Image" + s));
			
			TextureAttributes textureAttrib= new TextureAttributes();
			textureAttrib.setTextureMode(TextureAttributes.REPLACE);
			app.setTextureAttributes(textureAttrib);	
			
			float scl = 3f;                                 // scale the image 
			Vector3d scale = new Vector3d(scl, scl, scl);
			Transform3D transMap = new Transform3D();
			transMap.setScale(scale);
			textureAttrib.setTextureTransform(transMap);
			
			return app;
		}
		
		
	}  // end of RotateObject classs
	
	
	static class NewYaw extends RotateObject{
		
		public NewYaw() {			
		    Transform3D translator = new Transform3D();
		    translator.setTranslation(new Vector3d(0.0, 0.5, 0)); 
		    TransformGroup translateTG = new TransformGroup(translator); // New TransformGroup for translation
		    objRG = new TransformGroup();  // Keep objRG for rotation
		    translateTG.addChild(create_Object()); // Add the sphere to the translated TG
		    objRG.addChild(translateTG);  // Add translated TG to objRG
		    rotate_Object(20000 , objRG, "Yaw" ); 		 	// Rotate the object
		    objTG.addChild(objRG);          // Attach objRG to objTG
		}
		
		@Override
		protected Node create_Object() {
			//app = CommonsLH.set_Appearance(CommonsLH.Red);  This is the old one which we don't want  
			app = set_Appearance("Sphere");	//Setting the appearance of the Yaw, to map on it an image 		
			return new Sphere(0.12f , Primitive.GENERATE_NORMALS ,30,app);  //Sphere sphere = new Sphere(float radius, int primflags, int divisions, Appearance appearance);	
		}		
	}// end of NewYaw class  
	
	static class NewBlades extends RotateObject{
		public NewBlades() {
			Transform3D translator = new Transform3D();
			translator.setTranslation(new Vector3d(-0.33, 0.01, 0));
		    TransformGroup translateTG = new TransformGroup(translator); // New TransformGroup for translation
			objRG = new TransformGroup();  
		    translateTG.addChild(create_Object()); // Add the sphere to the translated TG
		    objRG.addChild(translateTG);  // Add translated TG to objRG
			rotate_Object(5000 , objRG, "Blades" ); // rotate the object
			objTG.addChild(objRG);          // add objRG to objTG			
		}
		
		@Override
		protected Node create_Object() {
			app = CommonsLH.set_Appearance(CommonsLH.Magenta);  // the blades box appearance 
			//app2 = CommonsLH.set_Appearance(CommonsLH.Red);     /// the blades sphere appearance, this is the old one 
			app2 = set_Appearance("Sphere"); // new sphere appearance, for assig2 
			
			Group combinedGroup = new Group();  // this object will handle both the sphere and the box
			
			Box box = new Box(0.01f, 0.06f, 0.5f, Primitive.GENERATE_NORMALS, app); // create the box
			Sphere sphere = new Sphere(0.06f , Primitive.GENERATE_NORMALS ,30,app2);  // create the sphere 
			
			Transform3D sphereTransform = new Transform3D();   // make a transformation for the sphere to change it's position relative to the box
	        sphereTransform.setTranslation(new Vector3d(0.06, 0.0, 0.0)); // moving the sphere 0.05 t the right of x relative to the box
	        
	        TransformGroup sphereTG = new TransformGroup(sphereTransform);
	        sphereTG.addChild(sphere); // Add the sphere to its TransformGroup
	        
	        combinedGroup.addChild(box);
	        combinedGroup.addChild(sphereTG);
	        
	        return combinedGroup ; // return the combined objTG made out of the box and the sphere		
		}		
	}// end of NewBlades class 
	
	// class to make the new base 
	static class Base extends RotateObject{
	    public Base() {
	        Transform3D translator = new Transform3D();
	        translator.setTranslation(new Vector3d(0.0, -0.54, 0));  // Position the base
	        objTG = new TransformGroup(translator);
	        
	        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);  // Allow transformations if needed
	        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);   

	        objTG.addChild(create_Object());  // Attach the object to 'objTG'
	    }

	    protected Node create_Object() {
	        //Define colors for each face
	        Color3f[] colors = {
	            new Color3f(1.0f, 0.0f, 1.0f), // Magenta	
	            new Color3f(1.0f, 0.0f, 0.0f), // Red
	            new Color3f(1.0f, 1.0f, 0.0f), // Yellow
	            new Color3f(0.0f, 1.0f, 0.0f), // Green	   
	            new Color3f(0.0f, 1.0f, 1.0f), // Cyan
	            new Color3f(1.0f, 0.5f, 0.0f), // Orange
	            
	        };

	        //Create transparency attributes, 50% required 
	        TransparencyAttributes transparency = new TransparencyAttributes();
	        transparency.setTransparencyMode(TransparencyAttributes.BLENDED);  // Blended mode 
	        transparency.setTransparency(0.5f);  // such 0 is opaque and 1 is fully transparent 

	        //Create a default appearance
	        Appearance defaultApp = new Appearance();
	        defaultApp.setTransparencyAttributes(transparency);

	        //Create the box with the default appearance
	        Box box = new Box(0.5f, 0.04f, 0.5f, Primitive.GENERATE_NORMALS, defaultApp);

	        //Set different colors for each face
	        int[] faces = {Box.TOP, Box.BOTTOM, Box.LEFT, Box.RIGHT, Box.FRONT, Box.BACK};
	        for (int i = 0; i < faces.length; i++) {
	            Appearance faceApp = new Appearance();
	            faceApp.setTransparencyAttributes(transparency);
	            faceApp.setColoringAttributes(new ColoringAttributes(colors[i], ColoringAttributes.NICEST));
	            box.getShape(faces[i]).setAppearance(faceApp);
	        }

	        return box;
	    }
	}

}

