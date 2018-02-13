package simulation.engine;

/**
 * All absolutely critical global variables, such as the simulation.engine
 */
public class Singleton {
    // If you are not the Engine class, don't set this variable to be any other Engine object
    // or everything will probably break - the simulation.engine creates itself and sets this at startup
    public static Engine engine;

    /**
     * The following are message types that the message pump is
     * guaranteed to recognize
     *
     * To use them, use Singleton.simulation.engine.getMessagePump().sendMessage(new Message(...))
     */
    // Sets the width of the screen
    public static final String SET_SCR_WIDTH = "set_scr_width";
    public static final String SET_SCR_HEIGHT = "set_scr_height";
    public static final String SET_FULLSCREEN = "set_fullscreen";
    // Adds a UI element to the main window - make sure the data portion of the message
    // is a reference to the UI element
    public static final String ADD_UI_ELEMENT = "add_ui_element";
    public static final String REMOVE_UI_ELEMENT = "remove_ui_element";

    // Adds a Pulse Entity to the simulation.engine, which is an entity that needs to update
    // as frequently as possible. Be sure the include the object as the data portion of
    // the Message, with the Object implementing the "MessageHandler" interface.
    public static final String ADD_PULSE_ENTITY = "add_pulse_entity";
    // Removes the pulse entity (which should be included as the data portion of the message).
    public static final String REMOVE_PULSE_ENTITY = "remove_pulse_entity";
    // Informs the rendering system to add the given entity to the world - the data
    // part of your message should contain the entity to add
    public static final String ADD_RENDER_ENTITY = "add_render_entity";
    public static final String REMOVE_RENDER_ENTITY = "remove_render_entity";
    // Tells the renderer to register the texture and cache it - the data part
    // of your message should be a string to a file
    public static final String REGISTER_TEXTURE = "register_texture";
    // Sets the main camera of the scene - the data part of the object
    // should be a reference to a Camera object
    public static final String SET_MAIN_CAMERA = "set_main_camera";

    /**
     * The following are console variables that will be registered at startup
     * so you an depend on them being there
     *
     * To use them, use Singleton.simulation.engine.getConsoleVariables().find(...)
     */
    // Represents the title
    public static final String SCR_TITLE = "scr_title";
    // This value can be cast to a boolean where "false" means it is not fullscreen
    public static final String SCR_FULLSCREEN = "scr_fullscreen";
    // This value can be cast to an int - changing it will change the screen width
    public static final String SCR_WIDTH = "scr_width";
    // This value can be cast to an int - changing it will change the screen height
    public static final String SCR_HEIGHT = "scr_height";
    // This value can be cast to a boolean - "false" means the screen cannot be resized at runtime
    public static final String SCR_RESIZEABLE = "scr_resizeable";
    // This value can be cast to a boolean - "false" will override the max fps and allow the simulation.engine
    // to run as fast as it possibly can
    public static final String ENG_LIMIT_FPS = "eng_limit_fps";
    // This value can be cast to an int - 60 fps, for example, means the simulation.engine will not
    // update more than 60 times per second
    public static final String ENG_MAX_FPS = "eng_max_fps";

}
