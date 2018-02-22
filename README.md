# CS460_EHB_Simulation_Environment


Simulation environment for the EHB software.

## EHB Package
* All files created, and code written for the EHB software should be located in the ehb package.
* You will be provided an empty shell class called EHB.java, inside said class there is a method called update. This method will be called at 60 times a second, and should not contain any infinite loops as it will lock up the entire simulation.

## Main
* The main method is located in src/simulation/engine/Engine.java - this will run as expected without modification and it will automatically call your code
* You should not write your own static main method inside the ehb package and you should not modify Engine.main (or any other code inside of the simulation package)

## Interfaces
* The only external package you have access to is the interfaces package which contains the interfaces/drivers and public data structures (enum).
* NOTE: These are static classes so you do not and should not create an instance of any of the interfaces. All methods are static you can call them immediately. 
* Documentation for the interfaces can be found here: https://jmccall2.github.io/CS460_EHB_Simulation_Environment/

## Interface Use Examples:
```java
boolean active = ButtonInterface.isDown(); # Example how to check if the button is currently pressed down.

ButtonInterface.play(ButtonSoundTypes.ENGAGED); # Example how to play a sound, this is played immediately.

ButtonInterface.setColor(ButtonColorTypes.RED); # Example how to set the color, this is changed immediately.

BrakeInterface.setPressure(50); # Set brake pressure, the simulation takes this into account right away.

double speed = SpeedInterface.getSpeed(); # Example how to get the current speed (in meters/second)

GearTypes gear = GearInterface.getGear(); # Example how to get the current gear. 
```

## Third Party:
Image Sources (All under CC license):
http://maxpixel.freegreatpicture.com/Auto-Car-Sports-Car-Shelby-Mustang-Ford-158479
https://pixnio.com/textures-and-patterns/textile-cloth/black-leather-texture-pattern-close
https://upload.wikimedia.org/wikipedia/commons/7/72/Audi_MMI_2.jpg
https://c1.staticflickr.com/3/2658/4153625805_b3f328f87a_b.jpg
https://pixabay.com/p-1844227/?no_redirect
http://www.publicdomainpictures.net/pictures/190000/velka/the-sun-30.jpg
https://commons.wikimedia.org/wiki/File:Linecons_small-cloud.svg
http://clipart-library.com/images/kc8o9GEoi.jpg
https://www.soundjay.com/beep-sounds-2.html
GUI theme source (DarkTheme.css):
http://code.makery.ch/library/javafx-8-tutorial/part4/
Source for engaged/disengaged sound clip: http://www.text2speech.org/


