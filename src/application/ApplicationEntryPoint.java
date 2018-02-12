package application;

import GUI.SimulationScreen.ExternalView.Car;
import engine.*;
import GUI.*;
import ElectronicHandBrake.*;
import PublicInterfaces.*;


/**
 * This is the only part of the application that the engine
 * directly and explicitly knows about. It only guarantees
 * two things: it will call init() at the start and shutdown()
 * at the end. If you need anything else you must set it up
 * with the engine
 */
public class ApplicationEntryPoint {
    /**
     * Initializes the application
     */

    GUI _gui;
    EHB _ehb;
    ButtonInterface _bi;

    public void init()
    {
        _gui = new GUI();
        _bi = new ButtonInterface(_gui);
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
