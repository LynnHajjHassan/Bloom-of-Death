# Project: Bloom of Death  

Game Type: Crime investigation mystery game.

Setting: The player wakes up in a strange room with no memory of how they got there and sees a corpse lying on the floor. 

Gameplay: Players use their instincts to guide them. They can click on and inspect various objects lying around the room.

Objective: Slowly piece together the clues and reveal the killer’s identity.


## 📌 Features

- Interactive Crime Scene: Players can explore and interact with objects in a 3D environment.
- Java3D Graphics: The game leverages Java3D to render a fully immersive crime scene environment.
- Collision Detection: Prevents players from walking through tables and other objects.
- Interaction with Mouse/Keyboard: Enables interaction with evidence and other game elements.
- Animations: Includes effects like opening letters, turning on Tv, etc. 
- Sounds: Objects have unique sound effects, and certain environmental elements will produce ambient sounds.
- Navigation: First-person controls using the mouse and keyboard for movement and interaction.

## 📁 Project Structure  
```plaintext
Bloom of Death/
├── models/                     # Stores 3D object files
├── sounds/                     # Sound effects and music files
├── textures/                   # Texture files for objects and scenes
├── lib/                        # Required libraries
├── src/  
│   ├── core/                   # Core game logic
│   │   ├── GameState.java
│   │   ├── TriggerEvents.java
│   │   ├── BODMain.java
│   │
│   ├── models/                  # 3D and interactive objects
│   │   ├── background
│   │   ├── BaseShapesHS
│   │   ├── BloodPool
│   │   ├── BODObjects
│   │   ├── CactusPotScene
│   │   ├── Carpet
│   │   ├── CeilingLamp
│   │   ├── ChairHS
│   │   ├── clock
│   │   ├── ClockMain
│   │   ├── ClockObjects4
│   │   ├── Commons
│   │   ├── Corpse
│   │   ├── DoubleBass
│   │   ├── drugsHS
│   │   ├── drugsObject3
│   │   ├── FlatScreenTV
│   │   ├── FloorPillow
│   │   ├── FlowerScene
│   │   ├── flowerVase2
│   │   ├── FlowerVaseScene
│   │   ├── frameMain
│   │   ├── frameTeam
│   │   ├── GarbageCan
│   │   ├── HiddenLetter
│   │   ├── Knife
│   │   ├── MailScene
│   │   ├── Mirror
│   │   ├── OpenMailScene
│   │   ├── Pill
│   │   ├── PillBottle
│   │   ├── Pillow
│   │   ├── Plant2PotScene
│   │   ├── PlantPotScene
│   │   ├── RecordPlayer
│   │   ├── Room
│   │   ├── SideTableHS
│   │   ├── Sofa
│   │   ├── Table5
│   │   ├── TableLamp
│   │   ├── TVGlitch
│   │   ├── TVScene
│   │   ├── TVScene2
│   │   ├── WindowMain
│   │   ├── WindowObjects
│   │
│   ├── audio/                   # Handles sound and music playback
│   │   ├── SoundManager.java
│   │   ├── SoundUtilityJOA.java
│   │
│   ├── effects/                 # Visual and gameplay effects
│   │   ├── CameraEffects.java
│   │   ├── InnerThought.java
│   │   ├── Lights.java
│   │   ├── Display2DImage.java
│
├── README.md                    # Overview of the repository
├── .settings/                    # Project settings (specific to IDEs like Eclipse)
├── .classpath & .project         # Eclipse configuration files
└── bin/                          # Compiled bytecode

```


## How to Run the Project
1. Clone or download the repository.
2. Open Eclipse and import the project:
   - Go to **File** > **Import** > **General** > **Existing Projects into Workspace**.
   - Select the folder where the project was cloned/extracted.
3. The `lib/` folder contains all required libraries (must be .jdk not .jre)
   - **Run** --> **Run configurations**  --> **arguments** -->  copy paste those arguments in VM arguments : 
```plaintext
--add-exports=java.base/java.lang=ALL-UNNAMED
--add-exports=java.desktop/sun.awt=ALL-UNNAMED
--add-exports=java.desktop/sun.java2d=ALL-UNNAMED
```
4. Right-click on **BODMain.java** and select **Run As** > **Java Application**.
