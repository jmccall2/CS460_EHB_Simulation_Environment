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

List of things that need to be done (this is not everything)

GUI:
* Initial parameters have not been added.
* Nothing really happens when you change gears.
* Determine when/what components will become unusable while the simulation is running/not running. 

Simulation (Visual):
* Add moving background rendering to simulation.
* Add extra animations to car for when it skids/loses control from braking to fast.
* Add support to scale animation (car) speed.
* Add frames for internal animation of brake (I have frames available under CC I believe)

Simulation (Logic):
* Non of the physics/internal simulation logic is implemented, this includes (what I can think of)
- What happens when you change from some gear to another in motion/not in motion/ different speeds.
- How does various braking pressures effect the change of the rate of rotation of the wheels and how does
that change the traction/friction on the wheels, and at what traction/speed combination would the car start to lose control.
* Tying in the Simulation logic to the GUI events and the visual part of the simulation.

EHB:
* We need to add an example EHB software package, right now it's very basic.

Public Interfaces:
* Is this what he was talking about in the email??? Is the structure correct/ and are the required things made avaliable?

