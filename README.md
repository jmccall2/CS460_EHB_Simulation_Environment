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

Questions for Tuseday:
* Should the speed only be enterable upon initial startup and if not then what should occur when the user pauses
the simulation and then changes the speed. 
* Should their be a delay from the pressure being set to the pressure being activated. Currently it's done
immidiately in the simulations. 
* Are our interfaces correct.
.. Add more here, this is all I had. 

Bugs (most important):
* I found bad states the simulation can go into when stress testing the GUI, this is the most important bullet point
in the todo as:
#**No combination of the GUI's input and the EHB's interaction with the interfaces should cause the simulation
to go into a bad state.**
* Tiretrack animations/wobble do not work in reverse (simple fix)
* Speed gauge does not work in reverse (simple fix)

GUI/Simulation binding:
* Data from simulation needs to be plotted on graph(s)/update real time. 

Simulation:
* ? Not sure if there was anything else

EHB:
* Add example to show what happens when you incorrectly apply pressure. 

Interfaces:
* Colors in color enum should be: Blue, Red, Green, Yellow, Orange, and Light Blue according to the SRS"s
* Sounds in the sound enum should be: Engaged, Disengaged, long beep, and short beep according tot he SRS"s. 

Presentation/Release:
* Need powerpoint
* Need to produce java docs for the interfaces.
* Need way to package and deliver library, either through interfacable jar, pre-made intellij/eclipse projects, or build scripts.

Optional:
* Fix the clouds so their speed is correct given their perspective. 
* Impove traction loss animations/add variety




