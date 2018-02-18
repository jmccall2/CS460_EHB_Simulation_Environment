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


TODO:

Config:
* Add another config file to store information about the initial state of the simulation

GUI:
* Grey out park gear when appropriate. (Need to update logic once velocity is avaliable.)
* Add popup graph pane that can pane graphs on data gathered from the simulation. 

GUI/Animation:
* Add pressure bars to the speed/pressure values being reported. (Added, needs to scale with actual values now).

GUI/Simulation binding:
* Initial parameters do nothing when you set them.

Animations:
* Need more tiretrack svg's for various traction loss levels. (only two exist)
* Need to add car wobble for the traction loss levels.
* Need to add the final animaion of complete traction loss.

Simulation:
* Need to calculate new traction/speed values for each engine tick and make the information avaliable
to the appropriate entitys. 

Simulation/Animation binding:
* Need to take in the traction values and map it to different animations, including change of car
wheel rpm, traction loss level animations.

Engine:
* Figure out issue with background wrapping. ***DONE

EHB:
* We need to add an example EHB software package, right now it's very basic.


