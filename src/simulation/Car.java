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
        setLocationXYDepth(0, 215, -1);
        setSpeedXY(100, 0); // There is something wrong with the scaling of the background and seed, i.e this does not look like 100 mph
        setWidthHeight(200, 100);

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
    public void set_force(double force) {
        this.engine_force = force;
    }

    // use this to set before simulation starts
    public void init_status(double speed, Gear gear, double force) {
        this.speed = speed;
        this.gear = gear;
        this.engine_force = force;
    }

    // This variables are just for an example.
    // The tire track in the future will have have tire tracks with different curvatures
    // for different levels of traction loss.
    int delay = 0;
    int brakeToggle = 0;
    boolean APPLY_BRAKES = false;
    @Override
    public void pulse(double deltaSeconds) {
        _animationSequence.update(deltaSeconds); // Make sure we call this!
        brakeToggle++;
        if (brakeToggle > 350)
        {
            brakeToggle = 0;
            APPLY_BRAKES = !APPLY_BRAKES;
        }

        delay++;
            if (delay == 10) {
                delay = 0;
                if(APPLY_BRAKES) {
                System.out.println("adding tire track.");
                TireTrack tt = new TireTrack(this.getLocationX() + 5, this.getLocationY() + 55);
                tt.addToWorld();
            }
        }
    }
}

