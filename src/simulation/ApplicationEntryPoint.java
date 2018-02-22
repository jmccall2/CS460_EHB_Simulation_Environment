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

    private GUI _gui;
    private EHB _ehb;
    private Car _car;
    private Sun _sun;
    private boolean _init = true;
    private Helper _helper = new Helper();
    private ArrayList<SingleFrameEntity> _clouds;
    private List<Integer> _cloudSpeeds;
    private List<Integer> _cloudYLocs;
    private List<Integer> _cloudXLocs;
    private double _initialCarY = 0.0; // Used to correct the position of the sun

    {
        _cloudSpeeds = Arrays.asList(0, 8, 12, 20);
        _cloudYLocs = Arrays.asList(0, 5, 35, 0);
        _cloudXLocs = Arrays.asList(0, -400, 250, 100);
    }

    /**
     * Initialize interfaces, and actors in the world.
     */
    public void init()
    {
        _clouds = new ArrayList<>();
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
        _initialCarY = _car.getLocationY();
        Camera camera = new Camera();
        camera.attachToEntity(_car);
        camera.setAsMainCamera();
        _buildWorld();
        _buildMetricPanels();
    }

    // Let the engine know which messages to listen for.
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
        Engine.getMessagePump().signalInterest(SimGlobals.RESET_SIM, _helper);
    }


    // Add all supporting actors to the world.
    private void _buildWorld()
    {
        assert(_cloudSpeeds.size() == _cloudYLocs.size());
        assert(_cloudYLocs.size() == _cloudXLocs.size());
        String cloudPath = "resources/img/world/cloud.png";
       for(int i = 1; i <=6; i++)
       {
           BackgroundPanel bp = new BackgroundPanel("resources/img/world/background"+i+".jpeg",-1000 + (1000*(i-1)),-15,10,1000,700);
           bp.addToWorld();
           // There should probably be a better heuristic to decide where the clouds are placed.
           for(int j = 0; j < _cloudSpeeds.size(); j++) _clouds.add(new SingleFrameEntity(cloudPath,_cloudXLocs.get(j)+(1000*(i-1)), _cloudYLocs.get(j),5, _cloudSpeeds.get(j),0,100,100));
           for(SingleFrameEntity cloud : _clouds) cloud.addToWorld();
       }

        _sun = new Sun();
        _sun.addToWorld();

        // Make the sun static so it's always on screen
        _sun.setAsStaticActor(true);
    }

    // Add the speed/pressure gauges to the world.
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
    public void shutdown() {}

    /**
     * Call's the EHB's main update procedure when the simulation is running and
     * handles the sun actors translations.
     * @param deltaSeconds Change in seconds since the last update.
     *                     If the simulation.engine is running at 60 frames per second,
     */
    @Override
    public void pulse(double deltaSeconds) {
        if(_car.running()) _ehb.update(deltaSeconds);
        if(_init)_gui.setInitColor();
        _init = false;
        double currCarY = _car.getLocationY();
        if (currCarY != _initialCarY) {
            double deltaCarY = _initialCarY - currCarY;
            _sun.setLocationXYDepth(_sun.getLocationX(), _sun.getLocationY() + deltaCarY, _sun.getDepth());
            _initialCarY = currCarY;
        }
    }

    /**
     * Inner class to handle RESET_SIM messages relayed by the engine.
     */
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
