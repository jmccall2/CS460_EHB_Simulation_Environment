package ehb;

import interfaces.BrakeInterface;
import interfaces.EHBButtonInterface;
import interfaces.GearInterface;
import interfaces.SpeedInterface;
import simulation.BackgroundPanel;
import simulation.Car;
import simulation.GUI;
import simulation.SimGlobals;
import simulation.SingleFrameEntity;
import simulation.Sun;
import simulation.engine.Camera;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.PulseEntity;
import simulation.engine.Singleton;


/**
 * This is the only part of the application that the simulation.engine
 * directly and explicitly knows about. It only guarantees
 * two things: it will call init() at the start and shutdown()
 * at the end. If you need anything else you must set it up
 * with the simulation.engine
 */
public class ApplicationEntryPoint implements PulseEntity{
    /**
     * Initializes the application
     */

    GUI _gui;
    EHB _ehb;
    Car _car;
    boolean init = true;

    public void init()
    {
        Engine.getConsoleVariables().find(Singleton.CALCULATE_MOVEMENT).setValue("false");
        _registerSimulationMessages();
        // instances of the interfaces so that they do get creates
        new BrakeInterface();
        new SpeedInterface();
        new EHBButtonInterface();
        new GearInterface();

        Engine.getMessagePump().sendMessage(new Message(Singleton.ADD_PULSE_ENTITY,this));
        _ehb = new EHB();
        _gui = new GUI();
        _car = new Car();
        _car.addToWorld();
        Camera camera = new Camera();
        camera.attachToEntity(_car);
        camera.setAsMainCamera();
        _buildWorld();
    }

    private void _registerSimulationMessages()
    {
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_SPEED));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_PRESSURE));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_ENGAGED_SOUND));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_DISENGAGED_SOUND));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_ACTIVATED_COLOR));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_UNACTIVATED_COLOR));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.START_SIM));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.STOP_SIM));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.ACTIVATE_BRAKE));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.DEACTIVATE_BRAKE));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.GEAR_CHANGE));
    }



    private void _buildWorld()
    {
        String cloud = "resources/img/world/cloud.png";
       for(int i = 1; i <=6; i++)
       {
           BackgroundPanel bp = new BackgroundPanel("resources/img/world/part"+i+".jpeg",-375 + (1000*(i-1)),0,10,1000,500);
           bp.addToWorld();
           // There should probably be a better heuristic to decide where the clouds are placed.
           SingleFrameEntity cloud1 = new SingleFrameEntity(cloud,0+ (1000*(i-1)),0,5,5,0,100,100);
           SingleFrameEntity cloud2 = new SingleFrameEntity(cloud,-400+ (1000*(i-1)),10,5,8,0,100,100);
           SingleFrameEntity cloud3 = new SingleFrameEntity(cloud,250+ (1000*(i-1)),35,5,12,0,100,100);
           SingleFrameEntity cloud4 = new SingleFrameEntity(cloud,100+ (1000*(i-1)),0,5,20,0,100,100);
           cloud1.addToWorld();
           cloud2.addToWorld();
           cloud3.addToWorld();
           cloud4.addToWorld();
       }

        Sun sun = new Sun();
        sun.addToWorld();


        SingleFrameEntity renderPanel = new SingleFrameEntity("resources/img/panelback.png",0,460,2,0,0,1020,240);
        renderPanel.setAsStaticActor(true);
        renderPanel.addToWorld();

        // Attach the sun to the car so that it never gets left behind
        _car.attachActor(sun);
    }


    /**
     * Tells the application we need to shutdown
     */
    public void shutdown()
    {


    }

    @Override
    public void pulse(double deltaSeconds) {
        _ehb.update();
        if(init)_gui.setInitColor();
        init = false;
    }
}
