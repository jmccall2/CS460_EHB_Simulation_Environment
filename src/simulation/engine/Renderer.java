package simulation.engine;

import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import simulation.engine.math.Vector3;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Updates the drawable portion of the screen and ensures
 * that all actors properly move within the world based on
 * their speed & acceleration.
 *
 * @author Justin Hall
 */
public class Renderer implements MessageHandler {
    private GraphicsContext _gc;
    private HashMap<String, ImageView> _textures = new HashMap<>();
    private HashSet<RenderEntity> _entities = new HashSet<>();
    private Camera _worldCamera;

    public void init(GraphicsContext gc)
    {
        _gc = gc;
        Singleton.engine.getMessagePump().signalInterest(Singleton.ADD_RENDER_ENTITY, this);
        Singleton.engine.getMessagePump().signalInterest(Singleton.REMOVE_RENDER_ENTITY, this);
        Singleton.engine.getMessagePump().signalInterest(Singleton.REGISTER_TEXTURE, this);
        Singleton.engine.getMessagePump().signalInterest(Singleton.SET_MAIN_CAMERA, this);
    }

    public void render(double deltaSeconds)
    {
        _gc.setFill(Color.WHITE);
        _gc.fillRect(0, 0,
                     Singleton.engine.getConsoleVariables().find(Singleton.SCR_WIDTH).getcvarAsFloat(),
                     Singleton.engine.getConsoleVariables().find(Singleton.SCR_HEIGHT).getcvarAsFloat());

        // What values to offset everything in the world by
        double xOffset = 0.0;
        double yOffset = 0.0;
        // If the world camera was set then we need to deal with its entity first
        if (_worldCamera != null)
        {
            RenderEntity entity = _worldCamera.getEntity();
            double accelX = entity.getAccelerationX();
            double accelY = entity.getAccelerationY();
            double speedX = entity.getSpeedX();
            double speedY = entity.getSpeedY();
            double locX = entity.getLocationX();
            double locY = entity.getLocationY();
            speedX += (accelX * deltaSeconds);
            speedY += (accelY * deltaSeconds);
            entity.setSpeed(speedX, speedY);
            locX += (speedX * deltaSeconds);
            locY += (speedY * deltaSeconds);
            entity.setLocation(locX, locY);
            Vector3 translate = _worldCamera.getWorldTranslate();
            xOffset = translate.x();
            yOffset = translate.y();
        }
        System.out.println(xOffset + " " + yOffset);
        for (RenderEntity entity : _entities)
        {
            double accelX = entity.getAccelerationX();
            double accelY = entity.getAccelerationY();
            double speedX = entity.getSpeedX();
            double speedY = entity.getSpeedY();
            double locX = entity.getLocationX();
            double locY = entity.getLocationY();
            double width = entity.getWidth();
            double height = entity.getHeight();
            double screenX = 0.0;
            double screenY = 0.0;
            if (_worldCamera == null || entity != _worldCamera.getEntity()) {
                speedX += (accelX * deltaSeconds);
                speedY += (accelY * deltaSeconds);
                entity.setSpeed(speedX, speedY);
                locX += (speedX * deltaSeconds);
                locY += (speedY * deltaSeconds);
                screenX = locX + xOffset;
                screenY = locY + yOffset;
                entity.setLocation(locX, locY);
            }
            else if (_worldCamera != null && entity == _worldCamera.getEntity())
            {
                Vector3 location = _worldCamera.getEntityLocation();
                locX = location.x();
                locY = location.y();
                screenX = locX;
                screenY = locY;
            }
            if (_textures.containsKey(entity.getTexture()))
            {
                ImageView imageView = _textures.get(entity.getTexture());
                imageView.setRotate(entity.getRotationAngle());
                //imageView.setFitWidth(width);
                //imageView.setFitHeight(height);
                _gc.drawImage(imageView.getImage(), screenX, screenY, width, height);
            }
            else
            {
                _gc.setFill(Color.RED);
                _gc.fillRect(screenX, screenY, width, height);
            }
        }
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getMessageName())
        {
            case Singleton.ADD_RENDER_ENTITY:
                _entities.add((RenderEntity)message.getMessageData());
                break;
            case Singleton.REMOVE_RENDER_ENTITY:
                _entities.remove((RenderEntity)message.getMessageData());
                break;
            case Singleton.REGISTER_TEXTURE: {
                String texture = (String)message.getMessageData();
                if (!_textures.containsKey(texture)) {
                    try {
                        Image image = new Image(texture);
                        ImageView imageView = new ImageView(image);
                        imageView.setRotationAxis(new Point3D(0.0, 0.0, 1.0));
                        _textures.put((String) message.getMessageData(), imageView);
                    } catch (Exception e) {
                        System.err.println("ERROR: Unable to load " + texture);
                    }
                }
                break;
            }
            case Singleton.SET_MAIN_CAMERA:
                _worldCamera = (Camera)message.getMessageData();
                break;

        }
    }
}
