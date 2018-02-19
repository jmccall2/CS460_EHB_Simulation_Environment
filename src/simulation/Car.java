package simulation;

import interfaces.GearTypes;
import interfaces.GearInterface;
import interfaces.SpeedInterface;
import simulation.engine.*;

public class Car extends RenderEntity
{

    Helper helper = new Helper();
    private Animation _animationSequence;
    private double speed;
    private GearTypes gear;
    //acceleration due to engine, max ~ 5 m/s^2
    private boolean _isActive;
    private double applied_brake_force = 0;
    private double actual_brake_force;
    private double brake_percentage;

    private static final double mass = 1600; // in kg
    private static final double h = 1/60; // update rate
    private static final double drag_c = 2; // drag coefficient
    private boolean _tractionLossLevel = false;
    private static final double uk = .68; // coefficient of kinetic friction
    private static final double us = .9; // coefficient of static friction
    private static final double friction_threshold = us * 9.81 * mass;

    public Car()
    {
        // The second parameter to the Animation is the rate of change
        // If the rate is 5.0, it means that every 5 seconds we move
        // to a new animation frame
        _animationSequence = new Animation(this, 0.03);
        _buildFrames();
        setLocationXYDepth(0, 215, -1);
        setSpeedXY(100, 0); // There is something wrong with the scaling of the background and seed, i.e this does not look like 100 mph
        setWidthHeight(200, 100);
        Engine.getMessagePump().signalInterest(SimGlobals.ACTIVATE_BRAKE, helper);
        Engine.getMessagePump().signalInterest(SimGlobals.DEACTIVATE_BRAKE,helper);
        Engine.getMessagePump().signalInterest(SimGlobals.SET_PRESSURE,helper);
        Engine.getMessagePump().signalInterest(SimGlobals.GEAR_CHANGE,helper);
        Engine.getMessagePump().signalInterest(SimGlobals.START_SIM,helper);

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

    private void update(){
        float idle_a = 1;
        if(gear == GearTypes.REVERSE){
            idle_a = -2.196f;
        } else if(gear == GearTypes.NEUTRAL){
            idle_a = 0.0f;
        } else if(gear == GearTypes.DRIVE){
            idle_a = 2.196f;
        } else if(gear == GearTypes.PARK){
            idle_a = 0.0f;
        }

        int speedMod = 1;
        if(speed < 0) speedMod = -1;

        // actual brake force
        if(applied_brake_force < friction_threshold) actual_brake_force = applied_brake_force;
	    else {
            _tractionLossLevel = true;
            actual_brake_force = uk * mass * 9.81;
        }

        double actual_acceleration;

//        if(actual_brake_force > 0) { //|| gear== GearTypes.NEUTRAL
//            actual_acceleration = speedMod*(-(drag_c * speed * speed) - (actual_brake_force / mass) - (.02 * 9.8))+idle_a;
//        }
//        else {
//            actual_acceleration = 0;
//        }

        int brake;
        if(Math.abs(speed) < .01) brake = 0;
        else brake = 1;

        actual_acceleration = speedMod*(-(drag_c * speed * speed) - brake*(actual_brake_force / mass) - brake*(.02 * 9.8))+idle_a;

        speed = speed + actual_acceleration * h;
    }

    // This variables are just for an example. TEMPORARY until data is available.
    // The tire track in the future will have have tire tracks with different curvatures
    // for different levels of traction loss.
    int delay = 0;
    int xOffset = 0;
    @Override
    public void pulse(double deltaSeconds) {
        _animationSequence.update(deltaSeconds); // Make sure we call this!
        update();
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_SPEED,this.speed));
        if(_isActive)
        {
            delay++;
            if (delay == 25) {
                delay = 0;
                System.out.println("adding tire track.");
                xOffset = 5;
                TireTrack tt = new TireTrack(this.getLocationX() +xOffset , this.getLocationY() + 55, 1);
                tt.addToWorld();
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
                    gear = (GearTypes) message.getMessageData();
		    break;
                case SimGlobals.SET_PRESSURE:
                    brake_percentage = (Double) message.getMessageData();
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
                    break;
            }
        }
    }
}

