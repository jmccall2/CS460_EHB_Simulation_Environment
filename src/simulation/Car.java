package simulation;
import simulation.engine.Animation;
import simulation.engine.RenderEntity;

public class Car extends RenderEntity
{

    //private ArrayList<String> carFrames = new ArrayList<>();
    //private int _currentCarFrame = 0;
    //private int _delay = 0;
    private Animation _animationSequence;

    public Car()
    {
        // The second parameter to the Animation is the rate of change
        // If the rate is 5.0, it means that every 5 seconds we move
        // to a new animation frame
        _animationSequence = new Animation(this, 0.25);
        _buildFrames();
        setLocationXYDepth(0, 200, -1);
        setSpeedXY(50, 0);
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

