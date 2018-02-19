package simulation;

import java.util.ArrayList;

import interfaces.GearInterface;
import interfaces.GearTypes;
import interfaces.SpeedInterface;
import javafx.scene.paint.Color;
import simulation.engine.Animation;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.RenderEntity;
import simulation.engine.Singleton;

public class Car extends RenderEntity
{

    Helper helper = new Helper();
    private Animation _animationSequence;
    private double speed;
    private double oldSpeed; // feel free to delete this later
    private GearTypes gear;
    //acceleration due to engine, max ~ 5 m/s^2
    private boolean _isActive;
    private double applied_brake_force = 0;
    private double actual_brake_force;
    private double brake_percentage;
    private boolean pressure_set = false;
    private boolean first_start = false;
    private int START_Y = 215;
    private double _wobbleMinInput = 0.0;
    private double _wobbleMaxInput = Math.PI*2;
    private double _wobbleCurrentInput = _wobbleMinInput;
    private double _wobbleInputStepSize = _wobbleMaxInput / 500;


    private BarEntity _SpeedGauge;
    private BarEntity _PressureGauge;

    private static final double mass = 1600; // in kg
 //   private static final double h = 1.0/60; // update rate
    private static final double drag_c = 2; // drag coefficient
    private boolean _startTractionLossAnimation = false;
    private static final double uk = .68; // coefficient of kinetic friction
    private static final double us = .9; // coefficient of static friction
    private static final double friction_threshold = us * 9.81 * mass;
    private GUI guiRef;

    private ArrayList<Fire> _bringTheFire = new ArrayList<>();

    public Car()
    {
        // The second parameter to the Animation is the rate of change
        // If the rate is 5.0, it means that every 5 seconds we move
        // to a new animation frame
         _animationSequence = new Animation(this, 0);

        _buildFrames();
        setLocationXYDepth(0, START_Y, -1);
        setSpeedXY(speed, 0);
        setWidthHeight(200, 100);
        Engine.getMessagePump().signalInterest(SimGlobals.ACTIVATE_BRAKE, helper);
        Engine.getMessagePump().signalInterest(SimGlobals.DEACTIVATE_BRAKE,helper);
        Engine.getMessagePump().signalInterest(SimGlobals.SET_PRESSURE,helper);
        Engine.getMessagePump().signalInterest(SimGlobals.GEAR_CHANGE,helper);
        Engine.getMessagePump().signalInterest(SimGlobals.START_SIM,helper);
        oldSpeed = speed;

        _SpeedGauge = new BarEntity(Color.GREEN,22,625,3,0,0,75,240, BarEntityModes.SPEED);
        _SpeedGauge.setAsStaticActor(true);
        _SpeedGauge.addToWorld();

        _PressureGauge = new BarEntity(Color.GREEN,902,625,3,0,0,75,240, BarEntityModes.PRESSURE);
        _PressureGauge.setAsStaticActor(true);
        _PressureGauge.addToWorld();

    }
    
    public void setGUI(GUI gui)
    {
      this.guiRef = gui;
    }

    private void _insertFire()
    {
        _removeFire();
        // lol
        int particlesPerSecond = 1000;
        int iterations = (int)(speed);
        System.out.println("SPEED = " + speed);
        for (int i = 0; i < iterations; ++i) {
            Fire fire = new Fire(particlesPerSecond, getLocationX(), getLocationY() + getHeight() / 2, getDepth() + 1,getHeight() / 2, -1, 0);
            fire.addToWorld();
            attachActor(fire);
            _bringTheFire.add(fire);
        }
    }

    private void _removeFire()
    {
        for (Fire fire : _bringTheFire)
        {
            removeActor(fire);
            fire.removeFromWorld();
        }
        _bringTheFire.clear();
    }

    private void _buildFrames()
    {
        // In this case, "car_drive" specifies a set of animation frames that go together.
        // We can create different categories within the same animation object so that
        // we can have the car play different animations under different circumstances if we want.
        for(int i = 1; i <= 13; i++) _animationSequence.addAnimationFrame("car_drive", "resources/img/car/car" + i + ".png");

        // This part isn't completely necessary since we only have one category,
        // but I guess it's good to be explicit
        _animationSequence.setCategory("car_drive");
    }

    private void update(double deltaSeconds){
        boolean atomic_active = _isActive;

        float idle_a = 1;
        if(gear == GearTypes.REVERSE){
//            idle_a = -2.196f;
            idle_a = -(float)(4.0f*(drag_c/mass)) + (9.81f*.02f);
        } else if(gear == GearTypes.NEUTRAL){
            idle_a = 0.0f;
        } else if(gear == GearTypes.DRIVE){
//            idle_a = 2.196f;
//            idle_a = 5.0f;
            idle_a = (float)(4.0f*(drag_c/mass)) + (9.81f*.02f);
        } else if(gear == GearTypes.PARK){
            idle_a = 0.0f;
        }

        int speedMod = 1;
        if(speed < 0) speedMod = -1;

        applied_brake_force  = 167* brake_percentage;
        if(applied_brake_force < friction_threshold) actual_brake_force = applied_brake_force;
        else actual_brake_force = uk * mass * 9.81;

        if (applied_brake_force > friction_threshold - 4000)  _startTractionLossAnimation = true;
        else _startTractionLossAnimation = false;

        double actual_acceleration;

        int brake;

        brake = 1;
        if(speed==0) brake = 0;

        if(!pressure_set && gear != GearTypes.NEUTRAL && !first_start){
            actual_acceleration=0;
        }
        else{
            actual_acceleration = speedMod*(-(drag_c * speed * speed)/mass - brake*(actual_brake_force / mass) - brake*(.02 * 9.81))+idle_a;
        }

        double lastSpeed = speed;
        double nextSpeed = speed + actual_acceleration * deltaSeconds;

        if(gear == GearTypes.REVERSE){
            if(speed <= 0 && nextSpeed >= 0)speed = 0;
            else if(speed == 0 && pressure_set) speed = 0;
            else if(atomic_active && actual_acceleration < 0) speed=0;
            else speed = nextSpeed;
            System.out.println("reverse");
        } else if(gear == GearTypes.NEUTRAL){
            if(speed >= 0 && nextSpeed <= 0) speed = 0;
            else if(speed <= 0 && nextSpeed >= 0) speed = 0;
            else if(speed == 0 && pressure_set) speed = 0;
            else if(atomic_active && Math.abs(speed) < 1) speed=0;
            else speed = nextSpeed;
            System.out.println("neutral");
        } else if(gear == GearTypes.DRIVE){
            if(speed >= 0 && nextSpeed < 0) speed = 0;
            else if(speed == 0 && pressure_set)speed = 0;
            else if(atomic_active && actual_acceleration > 0)speed=0;
            else speed = nextSpeed;
            System.out.println("drive");
        } else if(gear == GearTypes.PARK){
            speed = 0;
            System.out.println("park");
        }
        System.out.println(speed);
        double speedToDisplay = speed/0.448;
        guiRef.setSpeed(speedToDisplay);
        guiRef.setPressure(brake_percentage);
//        System.out.println("change in momentum = " + (mass*lastSpeed - mass*speed));
    }


    private void _wobble()
    {
        double wobblePeriod;
        double wobble;
        _wobbleCurrentInput+=_wobbleInputStepSize;
        if(_wobbleCurrentInput > _wobbleMaxInput) _wobbleCurrentInput = _wobbleMinInput;
        if(speed < 63 && speed > 50) wobblePeriod = 1;
        else if(speed < 50 && speed > 35) wobblePeriod = 5;
        else if (speed < 35 && speed > 20) wobblePeriod = 10;
        else if (speed < 20 && speed > 10) wobblePeriod = 15;
        else wobblePeriod = 20;
        wobble  =(speed/20)*Math.sin(wobblePeriod*_wobbleCurrentInput);
        setRotation(wobble);
        setLocationXYDepth(getLocationX(), getLocationY() + wobble, -1);
    }


    int delay = 0;
    int xOffset = 0;
    @Override
    public void pulse(double deltaSeconds) {
        _animationSequence.update(deltaSeconds); // Make sure we call this!
        update(deltaSeconds);
        // Add the fire
        // we should probably remove this lol
        boolean calcMovement = Engine.getConsoleVariables().find(Singleton.CALCULATE_MOVEMENT).getcvarAsBool();
        if (calcMovement && speed < 2)
        {
            _removeFire();
        }
        else if (calcMovement && _bringTheFire.size() == 0)
        {
            _insertFire();
        }
        else if (calcMovement && oldSpeed != speed)
        {
            _insertFire();
            oldSpeed = speed;
        }
        else if (!calcMovement && _bringTheFire.size() > 0)
        {
            _removeFire();
        }
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.SPEED,speed));
        setSpeedXY(speed*45,0);
        _animationSequence.setAnimationRate(1.91/(13*((speed == 0) ? 0.0001 : speed)));
        _SpeedGauge.updateState(speed);
        _PressureGauge.updateState(brake_percentage);
        if(speed > 5 && _startTractionLossAnimation) {
            new TireTrack(this.getLocationX() + xOffset, this.getLocationY() + 55, 1).addToWorld();
            _wobble();
        }

    }

    class Helper implements MessageHandler
    {
        @Override
        public void handleMessage(Message message)
        {
            switch (message.getMessageName())
            {
                case SimGlobals.GEAR_CHANGE:
                    gear = (GearTypes) message.getMessageData();
		    break;
                case SimGlobals.SET_PRESSURE:
                    brake_percentage = (Double) message.getMessageData();
                    if(brake_percentage != 0)  pressure_set = true;
                    if(brake_percentage != 0) {
                        pressure_set = true;
                        if(!first_start) first_start = true;
                    }
//                    System.out.println("Brake: " + brake_percentage);
		    break;
                case SimGlobals.START_SIM:
                    speed = SpeedInterface.getSpeed();
                    gear = GearInterface.getGear();
                    break;
                case SimGlobals.ACTIVATE_BRAKE:
                    _isActive = true;
                    break;
                case SimGlobals.DEACTIVATE_BRAKE:
                    _isActive = false;
                    pressure_set = false;
                    break;
            }
        }
    }
}

