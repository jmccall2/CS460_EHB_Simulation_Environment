package simulation;

import interfaces.Gear;
import simulation.engine.*;

public class Car extends RenderEntity
{

    Helper helper = new Helper();
    private Animation _animationSequence;
    private double speed;
    private Gear gear;
    private double brake_pressure;
    //acceleration due to engine, max ~ 5 m/s^2
    private double acceleration;
    private boolean _isActive;

    private static final double mass = 1600; // in kg
    private static final double h = 1/60; // update rate
    private static final double drag_c = 1.4; // drag coefficient
    private int _tractionLossLevel = 0;

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

    // they can change gear at runtime
    public void set_gear(Gear gear){
        this.gear = gear;
    }

    // they can change force at runtime
    public void set_force(double acceleration) {
        this.acceleration = acceleration;
    }

    // use this to set before simulation starts
    public void init_status(double speed, Gear gear, double force) {
        this.speed = speed;
        this.gear = gear;
        this.acceleration = acceleration;
    }

    private void update(){
        double force = mass*acceleration - drag_c*speed*speed - 30*drag_c*speed;
        double actual_acceleration = force/mass;
        speed = speed + actual_acceleration*h;
    }

    // This variables are just for an example. TEMPORARY until data is available.
    // The tire track in the future will have have tire tracks with different curvatures
    // for different levels of traction loss.
    int delay = 0;
    int xOffset = 0;
    @Override
    public void pulse(double deltaSeconds) {
        _animationSequence.update(deltaSeconds); // Make sure we call this!
        if(_isActive)
        {
            delay++;
            if (delay == 25) {
                delay = 0;
                System.out.println("adding tire track.");
                xOffset = _tractionLossLevel == 1 ? 5 : 1;
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

