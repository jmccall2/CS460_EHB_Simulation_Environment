package simulation;

import ehb.EHB;
import interfaces.BrakeInterface;
import interfaces.ButtonInterface;
import interfaces.GearInterface;
import interfaces.SpeedInterface;
import simulation.engine.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
    boolean restart = false;
    boolean wasRestarted = false;
    Helper helper = new Helper();
    private ArrayList<SingleFrameEntity> clouds;
    private List<Integer> cloudSpeeds;
    private List<Integer> cloudYLocs;
    private List<Integer> cloudXLocs;

    {
       cloudSpeeds = Arrays.asList(0, 8, 12, 20);
       cloudYLocs = Arrays.asList(0, 5, 35, 0);
       cloudXLocs = Arrays.asList(0, -400, 250, 100);
    }

    public void init()
    {
        clouds = new ArrayList<>();
        Engine.getConsoleVariables().find(Singleton.CALCULATE_MOVEMENT).setValue("false");
        _registerSimulationMessages();
        // instances of the interfaces so that they do get creates
        new BrakeInterface();
        new SpeedInterface();
        new ButtonInterface();
        new GearInterface();

        Engine.getMessagePump().sendMessage(new Message(Singleton.ADD_PULSE_ENTITY,this));
        _ehb = new EHB();
        _gui = new GUI();
        _car = new Car();
        _car.setGUI(_gui);
        _car.addToWorld();
        Camera camera = new Camera();
        camera.attachToEntity(_car);
        camera.setAsMainCamera();
        _buildWorld();
        _buildMetricPanels();
    }

    private void _registerSimulationMessages()
    {
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.SPEED));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_PRESSURE));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.START_SIM));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.RESET_SIM));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.ACTIVATE_BRAKE));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.DEACTIVATE_BRAKE));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.GEAR_CHANGE));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.SPEED));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_BUTTON_COLOR));
        Engine.getMessagePump().registerMessage(new Message(SimGlobals.JERK));
        Engine.getMessagePump().signalInterest(SimGlobals.RESET_SIM, helper);
    }



    private void _buildWorld()
    {
        assert(cloudSpeeds.size() == cloudYLocs.size());
        assert(cloudYLocs.size() == cloudXLocs.size());
        String cloudPath = "resources/img/world/cloud.png";
       for(int i = 1; i <=6; i++)
       {
           BackgroundPanel bp = new BackgroundPanel("resources/img/world/background"+i+".jpeg",-1000 + (1000*(i-1)),-15,10,1000,700);
           bp.addToWorld();
           // There should probably be a better heuristic to decide where the clouds are placed.
           for(int j = 0; j < cloudSpeeds.size(); j++) clouds.add(new SingleFrameEntity(cloudPath,cloudXLocs.get(j)+(1000*(i-1)), cloudYLocs.get(j),5, cloudSpeeds.get(j),0,100,100));
           for(SingleFrameEntity cloud : clouds) cloud.addToWorld();
       }

        Sun sun = new Sun();
        sun.addToWorld();

        // Constrain the sun's y movement so it always stays in the same
        // spot along the y axis
        //sun.setConstrainXYMovement(false, true);

        // Attach the sun to the car so that it never gets left behind
        //_car.attachActor(sun);

        // Make the sun static so it's always on screen
        sun.setAsStaticActor(true);
    }

    private void _buildMetricPanels()
    {
        SingleFrameEntity renderPanel = new SingleFrameEntity("resources/img/panelback.png",0,460,4,0,0,1020,240);
        renderPanel.setAsStaticActor(true);
        renderPanel.addToWorld();

        SingleFrameEntity blockingPanel = new SingleFrameEntity("resources/img/panelback.png",0,635,2,0,0,1020,70);
        blockingPanel.setAsStaticActor(true);
        blockingPanel.addToWorld();
    }


    /**
     * Tells the application we need to shutdown
     */
    public void shutdown()
    {


    }

    @Override
    public void pulse(double deltaSeconds) {
        if(_car.running()) _ehb.update();
        if(init)_gui.setInitColor();
        init = false;
    }

    class Helper implements MessageHandler
    {
        @Override
        public void handleMessage(Message message)
        {
            if(message.getMessageName().equals(SimGlobals.RESET_SIM))
            {
                Engine.getMessagePump().sendMessage(new Message(Singleton.PERFORM_SOFT_RESET));
            }
        }
    }
}
