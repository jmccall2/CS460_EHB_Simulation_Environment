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
    private GearTypes _gear;
    // is the brake on?
    private boolean _isActive;
    private boolean _simulationOn = true;
    private double _applied_brake_force = 0;
    private double _actual_brake_force;
    private double _brake_percentage;
    private boolean _sim_is_active = false;
    private double _engine_acceleration;
    private double _previous_acceleration;
    // this is the acceleration we want from the engine. Prevents sudden acceleration
    private double _target_acceleration;
    private float _g = 9.81f;
    private int START_Y = 215;
    private double _wobbleMinInput = 0.0;
    private double _wobbleMaxInput = Math.PI*2;
    private double _wobbleCurrentInput = _wobbleMinInput;
    private double _wobbleInputStepSize = _wobbleMaxInput / 500;
    private HashMap<GearTypes,Double> idle_accelerations = new HashMap();
    private BarEntity _SpeedGauge;
    private BarEntity _PressureGauge;
    private double _jerk = 0.0;
    private double _prevJerk = 0.0;

    private static final double _mass = 1600; // in kg
    private static final double _drag_c = 2; // drag coefficient
    private boolean _startTractionLossAnimation = false;
    // Coefficient of kinetic friction. this is constant
    private static final double _uk = .68; // coefficient of kinetic friction
    // Coefficient of static friction. this changes with velocity
    private static double _us; // coefficient of static friction
    // boundary between kinetic and static friction
    private static double _friction_threshold;
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

        idle_accelerations.put(GearTypes.DRIVE,(4.0f*(_drag_c / _mass)) + (9.81f*.02f));
        idle_accelerations.put(GearTypes.REVERSE,(-4.0f*(_drag_c / _mass)) - (9.81f*.02f));
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

    /**
     *  updates engine acceleration based on current acceleration, target acceleration and current gear
     * @param current_acc the current acceleration of the engine
     * @param target_acc the target acceleration of the engine
     * @param gear current gear
     * @return
     */
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
        // sim is not active: return
        if(!_sim_is_active) return;
        // idle acceleration
        if(Math.abs(speed) < 2){
            if (_gear == GearTypes.REVERSE) {
                _engine_acceleration = -(float) ((Math.pow(4,2) * (_drag_c / _mass)) + (9.81f * .02f));
            } else if (_gear == GearTypes.NEUTRAL) {
                _engine_acceleration = 0.0f;
            } else if (_gear == GearTypes.DRIVE) {
                _engine_acceleration = (float) ((Math.pow(4,2) * (_drag_c / _mass)) + (9.81f * .02f));
            } else if (_gear == GearTypes.PARK) {
                _engine_acceleration = 0.0f;
            }
        }

        // set animation of wheels
        if(speed >= 0) _animationSequence.setCategory("car_drive");
        if(speed < 0) _animationSequence.setCategory("car_reverse");

        // speed mod is used to determine if negative forces are in play and their direction
        int speedMod = 1;
        if(speed < 0) speedMod = -1;
        else if(speed == 0) speedMod = 0;

        // convert user input to actual force
        _applied_brake_force = 167 * _brake_percentage;

        // found this by interpolating between known _us/speed points
        // this is the coefficient of static friction, which depends on speed
        _us = .9125 - .002796*speed;
        _friction_threshold = _us * 9.81 * _mass;

        // are we in the kinetic or static friction ?
        if (_applied_brake_force < _friction_threshold) _actual_brake_force = _applied_brake_force;
        else _actual_brake_force = _uk * _mass * _g;

        // Used for animation: determine when control is lost
        if(_isActive) {
            if (_applied_brake_force > _friction_threshold) _startTractionLossAnimation = true;
            else _startTractionLossAnimation = false;
        } else _startTractionLossAnimation = false;

        // actual car acceleration based on all forces
        double actual_acceleration;

        // is brake force active?
        int brake = 1;

        // is rolling friction active?
        int rolling_friction = 1;

        if(speed==0){
            rolling_friction = 0;
            brake = 0;
        }

        // brake is not active
        if(!_isActive) brake = 0;

        // change drag depending on speed
        double drag_c_ = _drag_c;
        if(Math.abs(speed) < 2) drag_c_ = 0;

        // smooth engine acceleration
        _engine_acceleration = nextAcc(_engine_acceleration, _target_acceleration, _gear);

        actual_acceleration = speedMod*(-(drag_c_ * Math.pow(speed,2))/ _mass - brake*(_actual_brake_force / _mass) - rolling_friction*(.02 * _g))+ _engine_acceleration;

        // what is the proposed next speed
        double nextSpeed = speed + actual_acceleration * deltaSeconds;

        // save last speed to calculate jerk
        double lastSpeed = speed;

        // if speed is at zero and brake is activated, it shouldnt move backward
        if(brake == 1){
            if(speed <= 0 && nextSpeed > 0)speed = 0;
            else if(speed >= 0 && nextSpeed < 0)speed = 0;
            else speed = nextSpeed;
        } else {
            speed = nextSpeed;
        }

        // keep speed at 0
        if(speedMod == 0 && _applied_brake_force > 0) speed = 0;

        // calculate jerk
        _jerk = (_previous_acceleration-(speed-lastSpeed))/deltaSeconds;
        //if (brake_percentage > 0.0) setRotation(getSpeedX()*jerk);
        //else setRotation(0);
        //System.out.println("JERK " + jerk);

        _previous_acceleration = (speed-lastSpeed);

        // display stuff
        double speedToDisplay = speed/0.448;
        guiRef.setSpeed(speedToDisplay);
        guiRef.setPressure(_brake_percentage);
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

    private void _generateWhiplash(double deltaSeconds)
    {
        final double magicConstant = 0.8;
        if (_brake_percentage > 0)
        {
            _prevJerk = _jerk * magicConstant;
            setRotation(getRotation() + _prevJerk);
        }
        /*
        else if (brake_percentage == 0.0)
        {
            _prevJerk -= magicConstant2*deltaSeconds;
            if (_prevJerk <= 0.0)
            {
                _prevJerk = 0.0;
                setRotation(0.0);
            }
            else setRotation(getRotation() - magicConstant2*deltaSeconds);
        }
        */
    }


    int xOffset = 0;
    @Override
    public void pulse(double deltaSeconds) {
        if(_simulationOn) {
            _animationSequence.update(deltaSeconds); // Make sure we call this!
            update(deltaSeconds);
            Engine.getMessagePump().sendMessage(new Message(SimGlobals.SPEED, speed));
            if(_brake_percentage > 0) Engine.getMessagePump().sendMessage(new Message(SimGlobals.JERK, _jerk));
            setSpeedXY(speed * 45, 0);
            _animationSequence.setAnimationRate(Math.abs(1.91 / (13 * ((speed == 0) ? 0.0001 : speed))));
//            System.out.println(1.91 / (13 * ((speed == 0) ? 0.0001 : speed)));
            _SpeedGauge.updateState(speed);
            _PressureGauge.updateState(_brake_percentage);
            if (Math.abs(speed) > 5 && _startTractionLossAnimation) {
                new TireTrack(this.getLocationX() + xOffset, this.getLocationY() + 55, 1).addToWorld();
                _wobble();
            }
            else if (Math.abs(speed) > 5) _generateWhiplash(deltaSeconds);
            else setRotation(0);
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
                    _gear = (GearTypes) message.getMessageData();
                    if(_gear == GearTypes.REVERSE){
                        _target_acceleration = -(float)((speed*speed*(_drag_c / _mass)) + (_g *.02f));
                    } else if(_gear == GearTypes.NEUTRAL){
                        _target_acceleration = 0.0f;
                    } else if(_gear == GearTypes.DRIVE){
                        _target_acceleration = (float)(speed*speed*(_drag_c / _mass)) + (_g *.02f);
                    } else if(_gear == GearTypes.PARK){
                        _target_acceleration = 0.0f;
                    }
		            break;
                case SimGlobals.SET_PRESSURE:
                    _brake_percentage = (Double) message.getMessageData();
		            break;
                case SimGlobals.START_SIM:
                    speed = SpeedInterface.getSpeed();
                    _gear = GearInterface.getGear();
                    if(_gear == GearTypes.REVERSE){
                        _engine_acceleration = -(float)((speed*speed*(_drag_c / _mass)) + (_g *.02f));
                    } else if(_gear == GearTypes.NEUTRAL){
                        _engine_acceleration = 0.0f;
                    } else if(_gear == GearTypes.DRIVE){
                        _engine_acceleration = (float)(speed*speed*(_drag_c / _mass)) + (_g *.02f);
                    } else if(_gear == GearTypes.PARK){
                        _engine_acceleration = 0.0f;
                    }
                    _target_acceleration = _engine_acceleration;
                    _sim_is_active = true;
                    _simulationOn = true;
                    break;
                case SimGlobals.ACTIVATE_BRAKE:
                    if(_gear == GearTypes.REVERSE){
                        _target_acceleration = -(float)((2*(_drag_c / _mass)) + (_g *.02f));
                    } else if(_gear == GearTypes.NEUTRAL){
                        _target_acceleration = 0.0f;
                    } else if(_gear == GearTypes.DRIVE){
                        _target_acceleration = (float)(2*(_drag_c / _mass)) + (_g *.02f);
                    } else if(_gear == GearTypes.PARK){
                        _target_acceleration = 0.0f;
                    }
                    _isActive = true;
                    break;
                case SimGlobals.DEACTIVATE_BRAKE:
                    _isActive = false;
                    if(_gear == GearTypes.REVERSE){
                        _target_acceleration = -(float)((speed*speed*(_drag_c / _mass)) + (_g *.02f));
                    } else if(_gear == GearTypes.NEUTRAL){
                        _target_acceleration = 0.0f;
                    } else if(_gear == GearTypes.DRIVE){
                        _target_acceleration = (float)(speed*speed*(_drag_c / _mass)) + (_g *.02f);
                    } else if(_gear == GearTypes.PARK){
                        _target_acceleration = 0.0f;
                    }
                    break;
                case SimGlobals.RESET_SIM:
                    _simulationOn = false;
                    _sim_is_active = false;
            }
        }
    }
}

