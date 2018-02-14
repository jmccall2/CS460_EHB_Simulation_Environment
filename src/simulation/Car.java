package simulation;
import interfaces.Gear;
import simulation.engine.Animation;
import simulation.engine.RenderEntity;

public class Car extends RenderEntity
{

    //private ArrayList<String> carFrames = new ArrayList<>();
    //private int _currentCarFrame = 0;
    //private int _delay = 0;
    private Animation _animationSequence;
    private double speed;
    private Gear gear;
    private double engine_force;
    private double brake_pressure;
    // might move this to physics engine since they should't change
    private static final double wheel_radius = .2286;
    private static final double weight = 400; // per wheel in kg
    private static final double coeff = .9;

    public Car()
    {
        // The second parameter to the Animation is the rate of change
        // If the rate is 5.0, it means that every 5 seconds we move
        // to a new animation frame
        _animationSequence = new Animation(this, 0.03);
        _buildFrames();
        setLocationXYDepth(0, 200, -1);
        setSpeedXY(50, 0);
        setWidthHeight(200, 100);
    }

    // they can change gear at runtime
    public void set_gear(Gear gear){
        this.gear = gear;
    }

    // they can change force at runtime
    public void set_force(double force) {
        this.engine_force = force;
    }

    // use this to set before simulation starts
    public void init_status(double speed, Gear gear, double force) {
        this.speed = speed;
        this.gear = gear;
        this.engine_force = force;
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

    public String getLatestFrontWheelFrame()
    {
        //_currentCarFrame++;
        //if(_currentCarFrame == carFrames.size()) _currentCarFrame = 0;
        //return carFrames.get(_currentCarFrame);
        return "";
    }

    @Override
    public void pulse(double deltaSeconds) {
        _animationSequence.update(deltaSeconds); // Make sure we call this!
    }
}

