package simulation;

import interfaces.GearInterface;
import interfaces.GearTypes;
import interfaces.SpeedInterface;
import javafx.scene.paint.Color;
import simulation.engine.Animation;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.RenderEntity;

import java.util.HashMap;

public class Car extends RenderEntity
{

    Helper helper = new Helper();
    private Animation _animationSequence;
    private double speed;
    private GearTypes gear;
    private GearTypes previous_gear;
    //acceleration due to engine, max ~ 5 m/s^2
    private boolean _isActive;
    private boolean _simulationOn = true;
    private double applied_brake_force = 0;
    private double actual_brake_force;
    private double brake_percentage;
    private boolean pressure_set = false;
    private boolean first_start = false;
    private boolean sim_is_active = false;
    private double engine_acceleration;
    private double previous_acceleration;
    private double target_acceleration;
    private double idle_s;
    private float g = 9.81f;
    private int START_Y = 215;
    private double _wobbleMinInput = 0.0;
    private double _wobbleMaxInput = Math.PI*2;
    private double _wobbleCurrentInput = _wobbleMinInput;
    private double _wobbleInputStepSize = _wobbleMaxInput / 500;
    private HashMap<GearTypes,Double> idle_accelerations = new HashMap();
    private BarEntity _SpeedGauge;
    private BarEntity _PressureGauge;

    private static final double mass = 1600; // in kg
 //   private static final double h = 1.0/60; // update rate
    private static final double drag_c = 2; // drag coefficient
    private boolean _startTractionLossAnimation = false;
    // Coefficient of kinetic friction. this is constant
    private static final double uk = .68; // coefficient of kinetic friction
    // Coefficient of static friction. this changes with velocity
    private static double us; // coefficient of static friction
    private static double friction_threshold;
    private GUI guiRef;


    public Car()
    {
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
        Engine.getMessagePump().signalInterest(SimGlobals.RESET_SIM,helper);

        idle_accelerations.put(GearTypes.DRIVE,(4.0f*(drag_c/mass)) + (9.81f*.02f));
        idle_accelerations.put(GearTypes.REVERSE,(-4.0f*(drag_c/mass)) - (9.81f*.02f));
        idle_accelerations.put(GearTypes.PARK,0.0);
        idle_accelerations.put(GearTypes.NEUTRAL,0.0);

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

    private void _buildFrames()
    {
        for(int i = 1; i <= 13; i++) _animationSequence.addAnimationFrame("car_drive", "resources/img/car/car" + i + ".png");
        for(int i = 13; i >= 1; i--) _animationSequence.addAnimationFrame("car_reverse", "resources/img/car/car" + i + ".png");
    }

    private static double nextAcc(double current_acc, double target_acc, GearTypes gear){
        double rate = .015;
        boolean slow = false;
        if(current_acc == target_acc) return target_acc;
        if(current_acc > target_acc && gear == GearTypes.DRIVE){
            slow = true;
        }
        if(current_acc < target_acc && gear == GearTypes.REVERSE){
            slow = true;
        }
        if(gear == GearTypes.DRIVE){
            if(!slow) {
                current_acc += rate;
                if (current_acc > target_acc) current_acc = target_acc;
            }
            else {
                current_acc -= rate;
                if(current_acc < target_acc) current_acc = target_acc;
            }
        }
        if(gear == GearTypes.REVERSE){
            if(!slow){
                current_acc -= rate;
                if(current_acc < target_acc) current_acc = target_acc;
            }
            else {
                current_acc += rate;
                if(current_acc > target_acc) current_acc = target_acc;
            }
        }
        if(gear == GearTypes.NEUTRAL){
            if(current_acc == 0) return current_acc;
            if(current_acc>0){
                current_acc -= rate;
                if (current_acc < target_acc) current_acc = 0;
            }
            if(current_acc<0){
                current_acc += rate;
                if (current_acc > target_acc) current_acc = 0;
            }
        }
        return current_acc;
    }

    private void update(double deltaSeconds){
        deltaSeconds=0.0217;
        if(!sim_is_active) return;
        if(Math.abs(speed) < 2){
            if (gear == GearTypes.REVERSE) {
                engine_acceleration = -(float) ((Math.pow(4,2) * (drag_c / mass)) + (9.81f * .02f));
            } else if (gear == GearTypes.NEUTRAL) {
                engine_acceleration = 0.0f;
            } else if (gear == GearTypes.DRIVE) {
                engine_acceleration = (float) ((Math.pow(4,2) * (drag_c / mass)) + (9.81f * .02f));
            } else if (gear == GearTypes.PARK) {
                engine_acceleration = 0.0f;
            }
        }

        if(speed >= 0) _animationSequence.setCategory("car_drive");
        if(speed < 0) _animationSequence.setCategory("car_reverse");

        // todo disallow negative numbers
        int speedMod = 1;
        if(speed < 0) speedMod = -1;
        else if(speed == 0) speedMod = 0;

        applied_brake_force = 167 * brake_percentage;

        // found this by interpolating between known us/speed points
        us = .9125 - .002796*speed;
        friction_threshold = us * 9.81 * mass;

        if (applied_brake_force < friction_threshold) actual_brake_force = applied_brake_force;
        else actual_brake_force = uk * mass * g;

        // todo change animation
        if(_isActive) {
            if (applied_brake_force > friction_threshold - 4000) _startTractionLossAnimation = true;
            else _startTractionLossAnimation = false;
        } else _startTractionLossAnimation = false;

        double actual_acceleration;

        int brake;

        int rolling_friction = 1;

        brake = 1;
        if(speed==0){
            rolling_friction = 0;
            brake = 0;
        }

        if(!_isActive) brake = 0;

        double drag_c_ = drag_c;
        if(Math.abs(speed) < 2) drag_c_ = 0;

        engine_acceleration = nextAcc(engine_acceleration, target_acceleration, gear);
//        System.out.println(engine_acceleration);

        actual_acceleration = speedMod*(-(drag_c_ * Math.pow(speed,2))/mass - brake*(actual_brake_force / mass) - rolling_friction*(.02 * g))+engine_acceleration;
//        System.out.println("acc :" + actual_acceleration);

//        if(Math.abs(speed) < 1 && brake==1) actual_acceleration = nextAcc(actual_acceleration,0.0, gear);

        double nextSpeed = speed + actual_acceleration * deltaSeconds;

        double lastSpeed = speed;

        if(brake == 1){
            if(speed <= 0 && nextSpeed > 0)speed = 0;
            else if(speed >= 0 && nextSpeed < 0)speed = 0;
            else speed = nextSpeed;

        } else {
            speed = nextSpeed;
        }

        if(speedMod == 0 && applied_brake_force > 0) speed = 0;

//        System.out.println((previous_acceleration-(speed-lastSpeed))/deltaSeconds);

        previous_acceleration = (speed-lastSpeed);

        double speedToDisplay = speed/0.448;
        guiRef.setSpeed(speedToDisplay);
        guiRef.setPressure(brake_percentage);
    }


    public boolean running()
    {
        return _simulationOn;
    }


    private void _wobble()
    {
        double absSpeed = Math.abs(speed);
        double wobblePeriod;
        double wobble;
        _wobbleCurrentInput+=_wobbleInputStepSize;
        if(_wobbleCurrentInput > _wobbleMaxInput) _wobbleCurrentInput = _wobbleMinInput;
        if(absSpeed < 63 && absSpeed > 50) wobblePeriod = 1;
        else if(absSpeed < 50 && absSpeed > 35) wobblePeriod = 5;
        else if (absSpeed < 35 && absSpeed > 20) wobblePeriod = 10;
        else if (absSpeed < 20 && absSpeed > 10) wobblePeriod = 15;
        else wobblePeriod = 20;
        wobble  =(absSpeed/20)*Math.sin(wobblePeriod*_wobbleCurrentInput);
        setRotation(wobble);
        setLocationXYDepth(getLocationX(), getLocationY() + wobble, -1);
    }


    int xOffset = 0;
    @Override
    public void pulse(double deltaSeconds) {
        if(_simulationOn) {
            _animationSequence.update(deltaSeconds); // Make sure we call this!
            update(deltaSeconds);
            Engine.getMessagePump().sendMessage(new Message(SimGlobals.SPEED, speed));
            setSpeedXY(speed * 45, 0);
            _animationSequence.setAnimationRate(Math.abs(1.91 / (13 * ((speed == 0) ? 0.0001 : speed))));
//            System.out.println(1.91 / (13 * ((speed == 0) ? 0.0001 : speed)));
            _SpeedGauge.updateState(speed);
            _PressureGauge.updateState(brake_percentage);
            if (Math.abs(speed) > 5 && _startTractionLossAnimation) {
                new TireTrack(this.getLocationX() + xOffset, this.getLocationY() + 55, 1).addToWorld();
                _wobble();
            }
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
                    previous_gear = gear;
                    gear = (GearTypes) message.getMessageData();
                    if(gear == GearTypes.REVERSE){
                        target_acceleration = -(float)((speed*speed*(drag_c/mass)) + (g*.02f));
                    } else if(gear == GearTypes.NEUTRAL){
                        target_acceleration = 0.0f;
                    } else if(gear == GearTypes.DRIVE){
                        target_acceleration = (float)(speed*speed*(drag_c/mass)) + (g*.02f);
                    } else if(gear == GearTypes.PARK){
                        target_acceleration = 0.0f;
                    }
		            break;
                case SimGlobals.SET_PRESSURE:
                    brake_percentage = (Double) message.getMessageData();
                    if(brake_percentage != 0 && _isActive) {
                        pressure_set = true;
                    }
		            break;
                case SimGlobals.START_SIM:
                    speed = SpeedInterface.getSpeed();
                    gear = GearInterface.getGear();
                    previous_gear = gear;
//                    System.out.println("##### speed: "+ speed);
                    if(gear == GearTypes.REVERSE){
                        engine_acceleration = -(float)((speed*speed*(drag_c/mass)) + (g*.02f));
                    } else if(gear == GearTypes.NEUTRAL){
                        engine_acceleration = 0.0f;
                    } else if(gear == GearTypes.DRIVE){
                        engine_acceleration = (float)(speed*speed*(drag_c/mass)) + (g*.02f);
                    } else if(gear == GearTypes.PARK){
                        engine_acceleration = 0.0f;
                    }
                    target_acceleration = engine_acceleration;
//                    first_start = false;
//                    pressure_set = false;
//                    _isActive = false;
                    sim_is_active = true;
                    _simulationOn = true;
                    break;
                case SimGlobals.ACTIVATE_BRAKE:
//                    System.out.println("activate brake");
                    if(gear == GearTypes.REVERSE){
                        target_acceleration = -(float)((2*(drag_c/mass)) + (g*.02f));
                    } else if(gear == GearTypes.NEUTRAL){
                        target_acceleration = 0.0f;
                    } else if(gear == GearTypes.DRIVE){
                        target_acceleration = (float)(2*(drag_c/mass)) + (g*.02f);
                    } else if(gear == GearTypes.PARK){
                        target_acceleration = 0.0f;
                    }
                    _isActive = true;
                    break;
                case SimGlobals.DEACTIVATE_BRAKE:
                    _isActive = false;
                    if(gear == GearTypes.REVERSE){
                        target_acceleration = -(float)((speed*speed*(drag_c/mass)) + (g*.02f));
                    } else if(gear == GearTypes.NEUTRAL){
                        target_acceleration = 0.0f;
                    } else if(gear == GearTypes.DRIVE){
                        target_acceleration = (float)(speed*speed*(drag_c/mass)) + (g*.02f);
                    } else if(gear == GearTypes.PARK){
                        target_acceleration = 0.0f;
                    }
                    pressure_set = false;
                    break;
                case SimGlobals.RESET_SIM:
                    _simulationOn = false;
//                    first_start = false;
                    sim_is_active = false;
//                    speed = 0;
//                    pressure_set = false;
//                    _isActive = false;
            }
        }
    }
}

