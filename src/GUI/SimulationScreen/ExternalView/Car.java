package GUI.SimulationScreen.ExternalView;
import engine.RenderEntity;

import java.util.ArrayList;

public class Car extends RenderEntity
{

    private ArrayList<String> carFrames = new ArrayList<>();
    private int _currentCarFrame = 0;
    private int _delay = 0;


    public Car()
    {
        _buildFrontWheelFrames();
        setLocation(350, 200);
        setSpeed(10, 0);
        // setAcceleration(175, 0);
        setWidthHeight(200, 100);
        setTexture("resources/img/car/car1.png");
    }

    private void _buildFrontWheelFrames()
    {
        for(int i = 1; i <= 13; i++) carFrames.add("resources/img/car/car" + i + ".png");
    }

    public String getLatestFrontWheelFrame()
    {
        _currentCarFrame++;
        if(_currentCarFrame == carFrames.size()) _currentCarFrame = 0;
        return carFrames.get(_currentCarFrame);

    }

    @Override
    public void pulse(double deltaSeconds) {
        _delay++;
        if(getLocationX() > 750) setSpeed(0,0);
        if(_delay == 10) {
            setTexture(getLatestFrontWheelFrame());
            _delay = 0;
        }
    }
}

