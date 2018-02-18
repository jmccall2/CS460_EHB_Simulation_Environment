package simulation.engine;

import simulation.ApplicationEntryPoint;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;

public class Engine extends Application implements PulseEntity, MessageHandler {
    private static Engine _engine; // Self-reference
    private static boolean _isInitialized = false;
    // Package private
    static final String R_RENDER_SCENE = "r_render_screen";
    static final String R_UPDATE_ENTITIES = "r_update_entities";

    private HashSet<PulseEntity> _pulseEntities;
    private ApplicationEntryPoint _application;
    private MessagePump _messageSystem;
    private ConsoleVariables _cvarSystem;
    private Window _window;
    private Renderer _renderer;
    private int _maxFrameRate;
    private long _lastFrameTimeMS;
    private boolean _isRunning = false;
    private boolean _updateEntities = true; // If false, nothing is allowed to move

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
                //if (deltaSeconds <= (1.0 / 60)) return;
                //System.out.println(deltaSeconds);
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
        // Check if any console variables changed and send messages for any that have
        ArrayList<ConsoleVariable> changedVars = _cvarSystem.getVariableChangesSinceLastCall();
        for (ConsoleVariable cvar : changedVars)
        {
            _messageSystem.sendMessage(new Message(Singleton.CONSOLE_VARIABLE_CHANGED, cvar));
        }
        // Make sure these two get added so that all entities are updated and
        // the screen is refreshed
        if (_updateEntities) _messageSystem.sendMessage(new Message(Engine.R_UPDATE_ENTITIES, deltaSeconds));
        _messageSystem.sendMessage(new Message(Engine.R_RENDER_SCENE, deltaSeconds));
        // Make sure we keep the messages flowing
        _messageSystem.dispatchMessages();
        for (PulseEntity entity : _pulseEntities)
        {
            entity.pulse(deltaSeconds);
        }
        // Tell the renderer to update the screen
        //_renderer.render(deltaSeconds);
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
            case Singleton.CONSOLE_VARIABLE_CHANGED:
            {
                ConsoleVariable cvar = (ConsoleVariable)message.getMessageData();
                if (cvar.getcvarName().equals(Singleton.CALCULATE_MOVEMENT))
                {
                    _updateEntities = Boolean.parseBoolean(cvar.getcvarValue());
                }
                break;
            }
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
        _cvarSystem.loadConfigFile("src/resources/engine.cfg");
        _registerDefaultCVars();
        _updateEntities = Boolean.parseBoolean(_cvarSystem.find(Singleton.CALCULATE_MOVEMENT).getcvarValue());
        _messageSystem = new MessagePump();
        // Make sure we register all of the message types
        _registerMessageTypes();
        // Signal interest in the things the simulation.engine needs to know about
        _messageSystem.signalInterest(Singleton.ADD_PULSE_ENTITY, this);
        _messageSystem.signalInterest(Singleton.REMOVE_PULSE_ENTITY, this);
        _messageSystem.signalInterest(Singleton.CONSOLE_VARIABLE_CHANGED, this);
        _pulseEntities = new HashSet<>();
        _window = new Window();
        _renderer = new Renderer();
        _application = new ApplicationEntryPoint();
        _isRunning = true;
        _lastFrameTimeMS = System.currentTimeMillis();
        GraphicsContext gc = _window.init(stage);
        _renderer.init(gc);
        _application.init();
        _maxFrameRate = _cvarSystem.find(Singleton.ENG_MAX_FPS).getcvarAsInt();
    }

    private void _registerDefaultCVars()
    {
        _cvarSystem.registerVariable(new ConsoleVariable(Singleton.ENG_MAX_FPS, "60", "60"));
        _cvarSystem.registerVariable(new ConsoleVariable(Singleton.ENG_LIMIT_FPS, "true", "true"));
        _cvarSystem.registerVariable(new ConsoleVariable(Singleton.WORLD_START_X, "0", "0"));
        _cvarSystem.registerVariable(new ConsoleVariable(Singleton.WORLD_START_Y, "0", "0"));
        _cvarSystem.registerVariable(new ConsoleVariable(Singleton.WORLD_WIDTH, "1000", "0"));
        _cvarSystem.registerVariable(new ConsoleVariable(Singleton.WORLD_HEIGHT, "1000", "0"));
        _cvarSystem.registerVariable(new ConsoleVariable(Singleton.CALCULATE_MOVEMENT, "true", "true"));
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
        _messageSystem.registerMessage(new Message(Singleton.CONSOLE_VARIABLE_CHANGED));
        _messageSystem.registerMessage(new Message(R_RENDER_SCENE));
        _messageSystem.registerMessage(new Message(R_UPDATE_ENTITIES));
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

    public static void main(String[] args) {
        launch(args);
    }
}
