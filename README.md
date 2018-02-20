# CS460_HandBrake

>>> TEMPORARY: Here is our current working outline for the structure of this program:
        https://go.gliffy.com/go/share/sitm5q6uo5wgpeuxe8b6

Simulation of electronic handbrake for CS 460.


Image Sources (All under CC license):
http://maxpixel.freegreatpicture.com/Auto-Car-Sports-Car-Shelby-Mustang-Ford-158479
https://pixnio.com/textures-and-patterns/textile-cloth/black-leather-texture-pattern-close
https://upload.wikimedia.org/wikipedia/commons/7/72/Audi_MMI_2.jpg
https://c1.staticflickr.com/3/2658/4153625805_b3f328f87a_b.jpg
https://pixabay.com/p-1844227/?no_redirect
http://www.publicdomainpictures.net/pictures/190000/velka/the-sun-30.jpg
https://commons.wikimedia.org/wiki/File:Linecons_small-cloud.svg
http://clipart-library.com/images/kc8o9GEoi.jpg

GUI theme source (DarkTheme.css):
http://code.makery.ch/library/javafx-8-tutorial/part4/

TODO:

Summary of what Roman said:
* The button interface should be returning whether the button is held down or not. I.e to have the brake activated
the user will have to have their mouse click held on the button.
* There is no pause simulation there is only start/restart. Restart should wipe all state. 
* The logic in the ehb needs to be more robust, *High pressure should be applied at low speeds and low pressure at high speeds*
* Make GUI buttons all same size and aligned horizontally and vertically.
* Make corrections to the diagram to show in class. 

Bugs (most important):
* I found bad states the simulation can go into when stress testing the GUI, this is the most important bullet point
in the todo as:
#**No combination of the GUI's input and the EHB's interaction with the interfaces should cause the simulation
to go into a bad state.**
* Tiretrack animations/wobble do not work in reverse (simple fix)
* Speed gauge does not work in reverse (simple fix)
* The sun follows the car onto the land 


GUI:
* Change button to send active signal to the engine while the button is pressed, and send a message to the engine when
the button is unpressed. (Meaning a click of the mouse triggers both events, the user has to hold down their mouse on the button
to brake.
* Change the start/stop button to just be a start button and then add another button that is called "reset", when the
user clicks reset all state will be wiped out and everything in the GUI should be cleared. Then the user can enter a new speed and 
click start again. Reset should only stop the simulation and clear state, not start the simulation. 

Interfaces:
* Accept new messages sent by GUI from button and make it avaliable to the EHB. 

GUI/Simulation binding:
* Data from simulation needs to be plotted on graph(s)/update real time. 

Simulation:
* ? Not sure if there was anything else

EHB:
* Add example to show what happens when you incorrectly apply pressure. *High pressure should be applied at low speeds and low pressure at high speeds* 

Interfaces:
* Colors in color enum should be: Blue, Red, Green, Yellow, Orange, and Light Blue according to the SRS"s
* Sounds in the sound enum should be: Engaged, Disengaged, long beep, and short beep according tot he SRS"s. 

Presentation/Release:
* No powerpoint for the presentation: focus on showing the interfaces and the demo EHB package so they know how to 
inteface with it. And then demo using the GUI/running the simulation. 
* Need to produce java docs for the interfaces.
* Need way to package and deliver library, either through interfacable jar, pre-made intellij/eclipse projects, or build scripts.

Optional:
* Fix the clouds so their speed is correct given their perspective. 
* Impove traction loss animations/add variety




