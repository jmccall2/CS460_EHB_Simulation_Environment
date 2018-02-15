package simulation.engine;

import ehb.ApplicationEntryPoint;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

public class Engine extends Application implements PulseEntity, MessageHandler {
    private static Engine _engine; // Self-reference
    private static boolean _isInitialized = false;

    private HashSet<PulseEntity> _pulseEntities;
    private ApplicationEntryPoint _application;
    private MessagePump _messageSystem;
    private ConsoleVariables _cvarSystem;
    private Window _window;
    private Renderer _renderer;
    private int _maxFrameRate;
    private long _lastFrameTimeMS;
    private boolean _isRunning = false;

    /**
     * Warning! Do not call the MessagePump's dispatch method!
     *
     * This allows other systems to pass messages/register messages/
     * signal interest in message.
     * @return MessagePump for modification
     */
    public static MessagePump getMessagePump()
    {
        return _engine._messageSystem;
    }

    /**
     * Returns the console variable listing for viewing/modification
     */
    public static ConsoleVariables getConsoleVariables()
    {
        return _engine._cvarSystem;
    }

    @Override
    public void start(Stage stage) {
        // Initialize the simulation.engine
        _init(stage);
        // Initialize the game loop
        new AnimationTimer()
        {
            @Override
            public void handle(long now) {
                if (!_isRunning) System.exit(0); // Need to shut the system down
                long currentTimeMS = System.currentTimeMillis();
                double deltaSeconds = (currentTimeMS - _lastFrameTimeMS) / 1000.0;
                // Don't pulse faster than the maximum frame rate
                if (deltaSeconds < (1/60)) return;
                pulse(deltaSeconds);
                _lastFrameTimeMS = currentTimeMS;
            }
        }.start();
    }

    /**
     * Represents the main game/simulation loop
     */
    @Override
    public void pulse(double deltaSeconds) {
        // Make sure we keep the messages flowing
        _messageSystem.dispatchMessages();
        for (PulseEntity entity : _pulseEntities)
        {
            entity.pulse(deltaSeconds);
        }
        // Tell the renderer to update the screen
        _renderer.render(deltaSeconds);
    }

    @Override
    public void handleMessage(Message message) {
        switch(message.getMessageName())
        {
            case Singleton.ADD_PULSE_ENTITY:
                _registerPulseEntity((PulseEntity)message.getMessageData());
                break;
            case Singleton.REMOVE_PULSE_ENTITY:
                _deregisterPulseEntity((PulseEntity)message.getMessageData());
                break;
        }
    }

    public void shutdown()
    {
        _isRunning = false;
        _application.shutdown();
    }

    private void _init(Stage stage)
    {
        if (_isInitialized) return; // Already initialized
        _isInitialized = true;
        _engine = this; // This is a static variable
        _cvarSystem = new ConsoleVariables();
        _loadEngineConfig("src/resources/engine.cfg");
        _registerDefaultCVars();
        _messageSystem = new MessagePump();
        // Make sure we register all of the message types
        _registerMessageTypes();
        // Signal interest in the things the simulation.engine needs to know about
        _messageSystem.signalInterest(Singleton.ADD_PULSE_ENTITY, this);
        _messageSystem.signalInterest(Singleton.REMOVE_PULSE_ENTITY, this);
        _pulseEntities = new HashSet<>();
        _window = new Window();
        _renderer = new Renderer();
        _application = new ApplicationEntryPoint();
        _isRunning = true;
        _lastFrameTimeMS = System.currentTimeMillis();
        GraphicsContext gc = _window.init(stage);
        _renderer.init(gc);
        _application.init();
    }

    private void _registerDefaultCVars()
    {
        _cvarSystem.registerVariable(new ConsoleVariable(Singleton.ENG_MAX_FPS, "60", "60"));
        _cvarSystem.registerVariable(new ConsoleVariable(Singleton.ENG_LIMIT_FPS, "true", "true"));
    }

    private void _registerMessageTypes()
    {
        _messageSystem.registerMessage(new Message(Singleton.ADD_PULSE_ENTITY));
        _messageSystem.registerMessage(new Message(Singleton.REMOVE_PULSE_ENTITY));
        _messageSystem.registerMessage(new Message(Singleton.ADD_UI_ELEMENT));
        _messageSystem.registerMessage(new Message(Singleton.REMOVE_UI_ELEMENT));
        _messageSystem.registerMessage(new Message(Singleton.SET_FULLSCREEN));
        _messageSystem.registerMessage(new Message(Singleton.SET_SCR_HEIGHT));
        _messageSystem.registerMessage(new Message(Singleton.SET_SCR_WIDTH));
        _messageSystem.registerMessage(new Message(Singleton.ADD_RENDER_ENTITY));
        _messageSystem.registerMessage(new Message(Singleton.REMOVE_RENDER_ENTITY));
        _messageSystem.registerMessage(new Message(Singleton.REGISTER_TEXTURE));
        _messageSystem.registerMessage(new Message(Singleton.SET_MAIN_CAMERA));
    }

    /**
     * Registers a pulse entity, which is an entity which must be updated once
     * per simulation.engine/simulation frame.
     * @param entity entity to update every frame
     */
    private void _registerPulseEntity(PulseEntity entity)
    {
        _pulseEntities.add(entity);
    }

    private void _deregisterPulseEntity(PulseEntity entity)
    {
        _pulseEntities.remove(entity);
    }

    private void _loadEngineConfig(String engineCfgFile)
    {
        try
        {
            FileReader fileReader = new FileReader(engineCfgFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null)
            {
                line = line.replaceAll(" ", "");
                String variable = "";
                String value = "";
                boolean isReadingValue = false;
                for (int i = 0; i < line.length(); ++i)
                {
                    char c = line.charAt(i);
                    if (c == '+') continue;
                    if (c == '/' && (i + 1) < line.length() && line.charAt(i + 1) == '/') break; // Found a comment
                    if (c == '=')
                    {
                        isReadingValue = true;
                        continue;
                    }
                    if (isReadingValue) value += c;
                    else variable += c;
                }
                if (variable.equals("")) continue;
                if (_cvarSystem.contains(variable)) _cvarSystem.find(variable).setValue(value);
                else _cvarSystem.registerVariable(new ConsoleVariable(variable, value));
            }
        }
        catch (Exception e)
        {
            System.err.println("WARNING: Unable to load " + engineCfgFile);
            //System.exit(-1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
