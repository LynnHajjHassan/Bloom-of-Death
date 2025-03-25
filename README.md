# Project: Bloom of Death  
This is a 3D mystery game developed using Java3D, where the player takes on the role of a detective with a dark secret. Instead of solving a crime, the detective is secretly working for the killer to hide and manipulate evidence in exchange for money.

## 📌 Features
- Interactive Crime Scene: Players can explore and interact with objects in a 3D environment.
- Decision-Based Gameplay: The choices players make influence the final case outcome.
- Dynamic Storytelling: The narrative adapts based on the player's actions, creating multiple possible endings.
- Java3D Graphics: The game leverages Java3D to render a fully immersive crime scene environment.
- Collision Detection: Prevents players from walking through tables and other objects.
- Interaction with Mouse/Keyboard: Enables interaction with evidence and other game elements.
- Animations: Includes effects like turning lamps on/off and clock movements.
- Sounds: Objects have unique sound effects, and certain environmental elements will produce ambient sounds.
- Navigation: First-person controls using the mouse and keyboard for movement and interaction.

## 📁 Project Structure  
```plaintext
BloomOfDeath/
├── objects/                      # Stores 3D object files
├── src/
├── sounds/
├── textures/
├── src/                          # Java source code
│   ├── BOD/                      #package
│       ├── BODMain.java          # Main class for the project
│       ├── BaseShapes.java       # shapes helper function
│       ├── Commons.java          # Helper functions
│       ├── GroupObjects.java     # helper functions
├── Preview/                      # Previews for the objects made 
│   ├── TableLamp.png
│   ├── TableLamp.mp4
├── lib/                          # Required Libraries
│   ├── java3d-core.jar
│   ├── java3d-examples.jar
│   ├── java3d-utils.jar
│   ├── jogamp-fat.jar
│   ├── vecmath.jar
├── README.md                     # Overview of the repository
├── .settings/                    # Project settings (specific to IDEs like Eclipse).
├── .classpath  &  .project       # Eclipse configuration files.
└── bin/BOD/                      # Compiled bytecode.
```

## How to Run the Project
1. Clone or download the repository.
2. Open Eclipse and import the project:
   - Go to **File** > **Import** > **General** > **Existing Projects into Workspace**.
   - Select the folder where the project was cloned/extracted.
3. The `lib/` folder contains all required libraries (must be .jdk not .jre)
   - **Run** --> **Run configurations**  --> **arguments** -->  copy paste those in VM arguments
4. Right-click on **BODMain.java** and select **Run As** > **Java Application**.
