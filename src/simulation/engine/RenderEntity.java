package simulation.engine;

import simulation.engine.math.Vector3;

/**
 * Implement this class for each object you want to be able to add to
 * the world, render it and have it move around.
 */
public abstract class RenderEntity implements PulseEntity {
    private Vector3 _location = new Vector3(0, 0, 0);
    private Vector3 _speed = new Vector3(0, 0, 0);
    private Vector3 _acceleration = new Vector3(0, 0, 0);
    private double _rotationDeg = 0.0;
    private double _width = 0.0, _height = 0.0;
    private String _texture;

    /**
     * This function ensures that the render entity is added to the world. After
     * calling this it will be regularly called by the Engine and its movement
     * will be calculated by the Renderer and it will be drawn on the screen.
     */
    public void addToWorld()
    {
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.ADD_PULSE_ENTITY, this));
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.ADD_RENDER_ENTITY, this));
    }

    /**
     * After calling this the entity will no longer be drawn and its update function will
     * not be called
     */
    public void removeFromWorld()
    {
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.REMOVE_PULSE_ENTITY, this));
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.REMOVE_RENDER_ENTITY, this));
    }

    /**
     * This determines what image is associated with the render entity
     * @param texture filename of image to load
     */
    public void setTexture(String texture)
    {
        _texture = texture;
        // Make sure the texture gets registered with the system
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.REGISTER_TEXTURE, _texture));
    }

    public String getTexture()
    {
        return _texture;
    }

    /*
     * The following methods should be used to set and get certain attributes of this render entity.
     * Using these ensures that the rendering system can properly move the entity within the world.
     */

    public void setRotationAngle(double angleDeg)
    {
        _rotationDeg = angleDeg;
    }

    public void setWidthHeight(double width, double height)
    {
        _width = width;
        _height = height;
    }

    public double getRotationAngle()
    {
        return _rotationDeg;
    }

    public double getWidth()
    {
        return _width;
    }

    public double getHeight()
    {
        return _height;
    }

    /**
     * Allows you to set the location of the render entity along with
     * its depth.
     *
     * The depth component tells the renderer how far back it is in relation
     * to other objects. While this will not influence the scale of the object,
     * it will ultimately help determine its rendering order.
     *
     * Example:
     *      Object 1: depth -100
     *      Object 2: depth 0
     *      Object 3: depth 100
     *
     *      The renderer will draw them to the screen in this order: Object 3,
     *      then Object 2, then Object 1 --> Object 1 will be on top
     */
    public void setLocationXYDepth(double x, double y, double depth)
    {
        _location.setXYZ(x, y, depth);
    }

    public void setSpeedXY(double speedX, double speedY)
    {
        _speed.setXYZ(speedX, speedY, 0.0);
    }

    public void setAcceleration(double accelX, double accelY)
    {
        _acceleration.setXYZ(accelX, accelY, 0.0);
    }

    public double getLocationX()
    {
        return _location.x();
    }

    public double getLocationY()
    {
        return _location.y();
    }

    public double getDepth()
    {
        return _location.z();
    }

    public double getSpeedX()
    {
        return _speed.x();
    }

    public double getSpeedY()
    {
        return _speed.y();
    }

    public double getAccelerationX()
    {
        return _acceleration.x();
    }

    public double getAccelerationY()
    {
        return _acceleration.y();
    }
}
