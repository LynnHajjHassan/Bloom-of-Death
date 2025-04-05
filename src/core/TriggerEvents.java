package core;

import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import audio.SoundManager;
import effects.CameraEffects;
import effects.Display2DImage;
import effects.InnerThought;
import models.CeilingLamp;
import models.TVGlitch;
/**Now we need to manage story events based on the player's interactions with objects.
 *  The triggerEvent method will handle this.
 * This method will be used to trigger specific events based on the current progress.
 */


public class TriggerEvents {

	// Declaring an instance of the class InnerThought
	// Used to Display an inner thought
    // x and y; position of the mouse click, don't change them 
    // Format to display an inner thought:  innerThought.showInnerThought("your message", x, y);         
	private static InnerThought innerThought = new InnerThought(BODMain.frame);	 ;   // This line is fixed, don't change it       
	//IMPORTANAT it is used to call my sound class (anyone if you want to the class call it like this way)
    public static  SoundManager soundManager = SoundManager.getInstance();
    //to call inside the class like this 
    //  SoundManager.getInstance().playSound("clockTicking");
    // SoundManager.getInstance().playSoundForDuration("wind", 5000);
    private static Display2DImage image = new Display2DImage(BODMain.frame);
	
	public static void triggerEvent(String eventName, int x, int y ) {
		
	    switch (eventName) {
    
	    
	 // Marco's work ----------------------------------------
        case "PILLOW_FOUND":
            innerThought.showInnerThought("It's warm... was someone just lying here?", x, y);
            break;
        
        case "WINDOW_FOUND":
            innerThought.showInnerThought("The outside world feels so far away... and so dark...", x, y);
            break; 
        
        case "CLOCK_FOUND":
            innerThought.showInnerThought("That ticking is getting on my nerves...", x, y);
            break;
        
        case"RECORD_FOUND":
            innerThought.showInnerThought("It’s still playing… Did she hear this before she...?", x, y);
            break;
            
        case"SOFA_FOUND":
            innerThought.showInnerThought("Blood smeared across the fabric... she struggled here", x, y);
            break;    
            
        case"BASS_FOUND":
            innerThought.showInnerThought("One of the strings snapped. Did someone hit it…", x, y);
            break;

        case "CORPSE_CLUE_FOUND":                           
         innerThought.showInnerThought("Who killed her... how did she die?", x, y);         // If corpse clue was found, we display an inner thought 
         //SoundManager.getInstance().playSoundForDuration("breathing", 5000);
         //CameraEffects cameraEffects  = new CameraEffects(BODMain.viewTransformGroup);
        // cameraEffects.shakeCamera(0.3, 500); // Shake the screen to add tension   //Trigger camera effects or sound for the first clue
            break;

        case "KNIFE_CLUE_FOUND":
             innerThought.showInnerThought("She was stabbed...!?", x, y);         // If corpse clue was found, we display an inner thought 

          //  CameraEffects.displayCameraEffect("blur");
            break;

        case "FLOWER_CLUE_FOUND":
            // Show new inner thoughts for the flower clue
            innerThought.showInnerThought("Was she with her lover before she died?", x, y);
            break;
         //-----------------------------------------------------------------------------------------------------------
            
            
        case "TV_CLUE_FOUND":
            innerThought.showInnerThought("The static warps for a second—was that… my reflection? I need a mirror.", x, y); 		// If corpse clue was found, we display an inner thought 
	         TVGlitch.toggleTVState(); // Will play glitch sound
	         soundManager.playSoundForDuration("find", 4000);// find the mirror sound play for the 5 second 
	         
	         soundManager.playSoundForDuration("ghost-whispers", 5000);// find the mirror sound play for the 5 second 
	         
	         soundManager.playSoundForDuration("glitch", 2000);// find the mirror sound play for the 5 second 
	                  

	         break ;
	         
        case "MIRROR_CLUE_FOUND":
            SoundManager.getInstance().stopSound("clockTicking");
            SoundManager.getInstance().stopSound("song");
            image.showImage("textures/The_Killer.jpeg", 0, 0, 700, 800, 10000);
       
            break;
            
         // Lynn's part -----------------------------------------------------------------------------------------------
        case "LETTER_CLUE_FOUND":
            // Show a message when the note is found
            // Step 1: Show Fahim's letter using the keyboard
            // Step 2: Darken the atmosphere
        	 Timer timer = new Timer();
        	  
        	timer.schedule(new TimerTask() {
                @Override
                public void run() {
                	innerThought.showInnerThought("What does that means....?", 500, 300);
                }
            }, 10000); // Initial delay of 5 seconds

            // Initial delay of 5 seconds before the first effect
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    CameraEffects cameraEffects = new CameraEffects(BODMain.viewTransformGroup);
                    cameraEffects.shakeCamera(0.3, 5000); // Shake the screen to add tension
                    cameraEffects.blurCamera();
                    SoundManager.getInstance().playSoundForDuration("breathing", 5000);
                }
            }, 10000); // Initial delay of 10 seconds

            // Second effect after 10 more seconds (total 15 seconds)
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    CeilingLamp.startFlickering();
                    SoundManager.getInstance().playSoundForDuration("LightTicking", 10000);
                }
            }, 18000); // Total delay of 15 seconds from the start
            
            
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    CeilingLamp.stopFlickering();
                    SoundManager.getInstance().stopSound("LightTicking");
                }
            }, 10000); 

            break;

    	 //-----------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------- 

	    }
	}
}
