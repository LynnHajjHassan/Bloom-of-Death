package models;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Random;

import javax.sound.sampled.Clip;

import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PointLight;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Switch;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.WakeupCondition;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnElapsedTime;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class CeilingLamp {
	
	private static BODObjects[] CLObjects = new BODObjects[4];
	private static CLbulb bulb = new CLbulb(); 


    public static TransformGroup create_CeilingLamp() {
        TransformGroup ceilingLampTG = new TransformGroup();
        
        // Create the pendantHolder
        CLObjects[0] = new pendantHolder(); 
        ceilingLampTG = CLObjects[0].position_Object();  
        
        // Create the pendant's glass
        CLObjects[1] = new pendantGlass(); 
        CLObjects[0].add_Child(CLObjects[1].position_Object());
        
        // Create the bulb holder
        CLObjects[2] = new CLBulbHolder(); 
        CLObjects[1].add_Child(CLObjects[2].position_Object());
        
        // Add the bulb
        CLObjects[1].add_Child(bulb.position_Object());
        
        return ceilingLampTG;
    }
    
    public static  void startFlickering() {
    	bulb.setFlickering(true);
        
    }
    
    public static void stopFlickering() {
        if (bulb != null) {
            bulb.setFlickering(false);
        }
    }
    
    public void setLightOn(boolean on) {
        if (bulb != null) {
            bulb.setOn(on);
        }
    }
    
    public boolean isFlickering() {
        return bulb != null && bulb.isFlickering();
    }
    
    public boolean isLightOn() {
        return bulb != null && bulb.isOn();
    }
	

}

class pendantHolder extends BODObjects{
	public pendantHolder() {
		scale = 7f;                                        	 // use to scale up/down original size
		post = new Vector3f(0f, 30f, 0f);                     // use to move object for positioning
		transform_Object("PendantHolder");                   // set transformation to 'objTG' and load object file
		create_Appearance() ;								 // Create the Full appearance of the body, including the colors, and texture
	}

	protected void create_Appearance() {	

		mtl_clr[0] = new Color3f(0.05f, 0.05f, 0.05f);  	// Very dark ambient
		mtl_clr[1] = new Color3f(0.15f, 0.15f, 0.15f);  	// Dark diffuse (not pure black)
		mtl_clr[2] = new Color3f(0.7f, 0.7f, 0.7f);    		// Bright specular - to give metallic look
		mtl_clr[3] = new Color3f(0.01f, 0.01f, 0.01f); 		// Minimal emissive
		setShininess(96.0f) ;
		obj_Appearance();								    // Applying colors on the object    	   		
	}
	
	
	public TransformGroup position_Object() {              	// attach object BranchGroup  to 'objTG'
		objTG.addChild(objBG);                            	
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                             // attach the next transformGroup to 'objTG'
	}
	
}



class pendantGlass extends BODObjects{
	public pendantGlass() {
		scale = 1d;                                        	 // use to scale up/down original size
		post = new Vector3f(0f, -1.3f, 0f);                  	 // use to move object for positioning
		transform_Object("pendantGlass");                    // set transformation to 'objTG' and load object file
		create_Appearance() ;								 // Create the Full appearance of the body, including the colors, and texture
	}

	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0] = new Color3f(0.15f, 0.08f, 0.04f);  
		mtl_clr[1] = new Color3f(0.772500f, 0.654900f, 0.000000f) ;
		mtl_clr[2] = new Color3f(0.7f, 0.6f, 0.5f);     // Warm metallic specular (slightly golden)
		mtl_clr[3] = new Color3f(0.1f, 0.05f, 0.02f);   // Subtle warm emissive
		obj_Appearance();							    // Applying colors on the object      
			
		   		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup  to 'objTG'
		objTG.addChild(objBG);                            		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
}



class CLBulbHolder extends BODObjects{
	public CLBulbHolder() {
		scale = 0.1d;                                        // use to scale up/down original size
		post = new Vector3f(0f, -0.1f, 0f);                  // use to move object for positioning
		transform_Object("CLBulbHolder");                    // set transformation to 'objTG' and load object file
		create_Appearance() ;								 // Create the Full appearance of the body, including the colors, and texture
	}

	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0] = new Color3f(0.15f, 0.08f, 0.04f);  
		mtl_clr[1] = new Color3f(0.772500f, 0.654900f, 0.000000f) ;
		mtl_clr[2] = new Color3f(0.7f, 0.6f, 0.5f);          // Warm metallic specular (slightly golden)
		mtl_clr[3] = new Color3f(0.1f, 0.05f, 0.02f);        // Subtle warm emissive
		obj_Appearance();									// Applying colors on the object                                 
		    		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup  to 'objTG'
		objTG.addChild(objBG);                            		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
}


class CLbulb extends BODObjects {
    private PointLight bulbLight;
    private boolean isOn = true;
    private FlickerBehavior flickerBehavior;
    private boolean isFlickering = false;

    public CLbulb() {
        this(0.2d, new Vector3f(0f, -0.3f, 0f));
    }

    public CLbulb(double scale, Vector3f position) {
        this.scale = scale;
        this.post = new Vector3f(position);
        
        // Initialize transform group with capabilities
        objTG = new TransformGroup();
        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        
        transform_Object("CLbulb");
        create_Appearance();
        create_Light();
        
        // Initialize but don't start flickering yet
        initializeFlickerBehavior();
    }

    protected void create_Appearance() {
        mtl_clr[0] = new Color3f(0.2f, 0.2f, 0.2f);
        mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f);
        mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f);
        mtl_clr[3] = new Color3f(0.8f, 0.8f, 0.8f);
        obj_Appearance();
    }

    private void create_Light() {
        bulbLight = new PointLight();
        bulbLight.setCapability(PointLight.ALLOW_STATE_WRITE);  // Fixed typo
        bulbLight.setCapability(PointLight.ALLOW_COLOR_WRITE);
        bulbLight.setCapability(PointLight.ALLOW_POSITION_WRITE);
        
        bulbLight.setColor(new Color3f(1f, 1f, 1f));
        bulbLight.setPosition(new Point3f(post.x, post.y, post.z));
        bulbLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 500.0));
        bulbLight.setEnable(isOn);
        objTG.addChild(bulbLight);
    }

    private void initializeFlickerBehavior() {
        flickerBehavior = new FlickerBehavior(bulbLight);
        flickerBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
        objTG.addChild(flickerBehavior);
    }
    
    public TransformGroup position_Object() {
        objTG.addChild(objBG);
        return objTG;
    }

    public void add_Child(TransformGroup nextTG) {
        objTG.addChild(nextTG);
    }

    public void setOn(boolean on) {
        isOn = on;
        if (!isFlickering) {  // Only change if not flickering
            bulbLight.setEnable(on);
        }
    }

    public boolean isOn() {
        return isOn;
    }
    
    public void setFlickering(boolean shouldFlicker) {
        if (shouldFlicker == isFlickering) return;
        
        isFlickering = shouldFlicker;
        if (shouldFlicker) {
            flickerBehavior.startFlickering();
        } else {
            flickerBehavior.stopFlickering();
            bulbLight.setEnable(isOn); // Restore to original state
        }
    }
    
    public boolean isFlickering() {
        return isFlickering;
    }
}


class FlickerBehavior extends Behavior {
    private PointLight bulbLight;
    private boolean shouldFlicker = false;
    private Random random = new Random();
    private WakeupCondition wakeupCondition;
    private boolean needsInitialTrigger = false;

    public FlickerBehavior(PointLight light) {
        this.bulbLight = light;
        this.wakeupCondition = new WakeupOnElapsedTime(100);
    }

    @Override
    public void initialize() {
        wakeupOn(wakeupCondition);
        if (needsInitialTrigger) {
            wakeupOn(new WakeupOnElapsedTime(100));
            needsInitialTrigger = false;
        }
    }

    public void startFlickering() {
        shouldFlicker = true;
        needsInitialTrigger = true;
        // Can't call wakeupOn directly here - will be handled in initialize()
    }

    public void stopFlickering() {
        shouldFlicker = false;
        bulbLight.setEnable(true); // Ensure light is on when stopping
    }

    @Override
    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        if (!shouldFlicker) {
            wakeupOn(wakeupCondition);
            return;
        }
        
        // Toggle light state (70% chance to be on)
        boolean newState = random.nextFloat() < 0.7f;
        bulbLight.setEnable(newState);
            
        // Schedule next flicker with random delay (100-300ms)
        wakeupOn(new WakeupOnElapsedTime(100 + random.nextInt(200)));
    }
}


