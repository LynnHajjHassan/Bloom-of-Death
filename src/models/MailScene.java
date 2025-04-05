package models;

import org.jogamp.java3d.*;

import org.jogamp.java3d.loaders.Scene;

import org.jogamp.java3d.loaders.objectfile.ObjectFile;

import org.jogamp.java3d.utils.image.TextureLoader;

import org.jogamp.vecmath.*;

import java.awt.Container;



public class MailScene {


public static BranchGroup createMailBG() {

// Create the mail envelope part using our composite MailEnvelope class.

MailEnvelope envelope = new MailEnvelope();

TransformGroup mailTG = envelope.position_Object();


// Apply overall transformations: position, scale, and rotations.

Transform3D mailTrans = new Transform3D();

mailTrans.setTranslation(new Vector3f(0f, 0f, 0f));

mailTrans.setScale(5.2f);


// Apply an X-axis rotation (tilt forward by 30°).

Transform3D xRotation = new Transform3D();

xRotation.rotX(Math.PI / 3); // 30 degrees forward tilt

mailTrans.mul(xRotation);


// Apply a Y-axis rotation (rotate 90°).

Transform3D yRotation = new Transform3D();

yRotation.rotY(Math.PI / 2); // 90 degrees rotation

mailTrans.mul(yRotation);


// Apply a Z-axis rotation (rotate 30° around Z-axis).

Transform3D zRotation = new Transform3D();

zRotation.rotZ(Math.PI / 6); // 30 degrees rotation

mailTrans.mul(zRotation);


// Set the combined transformation on the mail TransformGroup.

mailTG.setTransform(mailTrans);


// Create the scene root and add the mail TransformGroup.

BranchGroup sceneRoot = new BranchGroup();

sceneRoot.addChild(mailTG);

sceneRoot.setPickable(true);

sceneRoot.compile();

return sceneRoot;

}

}



// -------------------------------------------------------------------------

// MailEnvelope: A composite part for the mail object.

// This class loads the "mail.obj" model and applies the "mail_texture.jpg" texture.

class MailEnvelope extends BODObjects {

public MailEnvelope() {

// Set scale and position for the envelope.

scale = 1.0; // Adjust the scaling if necessary.

post = new Vector3f(0f, 0f, 0f); // Position at origin.

// Load the mail model from "objects/mail.obj"

transform_Object("mail");

create_Appearance();

}


protected void create_Appearance() {

// Define basic colors (you can adjust these if desired).

mtl_clr[0] = new Color3f(1.0f, 1.0f, 1.0f); // Ambient color.

mtl_clr[1] = new Color3f(1.0f, 1.0f, 1.0f); // Diffuse color.

mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f); // Specular color.

mtl_clr[3] = new Color3f(0f, 0f, 0f); // Emissive color.

// Apply the material settings.

obj_Appearance();

// Set the texture on the Appearance.

// This will try to load "textures/mail_texture.png" (or .jpg/.jpeg).

app.setTexture(textured_App("mail_texture"));

}


@Override

public TransformGroup position_Object() {

// Attach the loaded object (objBG) to the object's TransformGroup (objTG).

objTG.addChild(objBG);

return objTG;

}


@Override

public void add_Child(TransformGroup nextTG) {

// Attach additional children if needed.

objTG.addChild(nextTG);

}

}