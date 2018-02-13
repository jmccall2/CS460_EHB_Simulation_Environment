package simulation;

import simulation.engine.RenderEntity;

/**
 * @Hector - I'm thinking the physics will get implemented in a class a lot like this
 */
public class ExampleMovingEntity extends RenderEntity {
    public ExampleMovingEntity()
    {
        setLocation(300, 200);
        setSpeed(0, 0);
        setAcceleration(175, 0);
        setWidthHeight(100, 100);
        setTexture("resources/rock_texture.png");
    }

    @Override
    public void pulse(double deltaSeconds) {
        double maxSpeed = 300;
        double maxAccel = 175;
        if (getSpeedX() >= maxSpeed)
        {
            setAcceleration(-maxAccel, 0);
        }
        else if (getSpeedX() <= -maxSpeed)
        {
            setAcceleration(maxAccel, 0);
        }
    }
}
