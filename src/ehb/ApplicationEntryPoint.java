package ehb;

import simulation.Car;
import interfaces.EHBButton;
import simulation.GUI;


/**
 * This is the only part of the application that the simulation.engine
 * directly and explicitly knows about. It only guarantees
 * two things: it will call init() at the start and shutdown()
 * at the end. If you need anything else you must set it up
 * with the simulation.engine
 */
public class ApplicationEntryPoint {
    /**
     * Initializes the application
     */

    GUI _gui;
    EHB _ehb;
    EHBButton _bi;

    public void init()
    {
        _gui = new GUI();
        _bi = new EHBButton(_gui);
        _ehb = new EHB();
        Car car = new Car();
        car.addToWorld();
    }

    /**
     * Tells the application we need to shutdown
     */
    public void shutdown()
    {


    }
}
