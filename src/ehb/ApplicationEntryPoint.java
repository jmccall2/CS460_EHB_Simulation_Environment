package ehb;

import interfaces.Brake;
import interfaces.Speed;
import simulation.*;
import simulation.engine.Camera;
import simulation.engine.Message;
import simulation.engine.Singleton;
import simulation.Sun;


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

    public void init()
    {
        Singleton.engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_SPEED));
        Singleton.engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_PRESSURE));
        Singleton.engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_ENGAGED_SOUND));
        Singleton.engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_DISENGAGED_SOUND));

        new Brake();
        new Speed();


        _gui = new GUI();
        _ehb = new EHB();
        Car car = new Car();
        car.addToWorld();
        Camera camera = new Camera();
        camera.attachToEntity(car);
        camera.setAsMainCamera();
        _buildWorld();

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
    }


    /**
     * Tells the application we need to shutdown
     */
    public void shutdown()
    {


    }
}
