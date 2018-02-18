package simulation;


public class SimGlobals
{
    // Button activated/unactivated colors.
    public static final String SET_ACTIVATED_COLOR = "set_active_button_color";
    public static final String SET_UNACTIVATED_COLOR = "set_unactive_button_color";


    // Set values that are needed from the 'interfaces'
    // Such as speed and pressure
    public static final String SET_SPEED = "set_speed";
    public static final String SET_PRESSURE = "set_pressure";
    public static final String SET_BUTTON_COLOR = "set_button_color";


    /**
     * The following messages are tied to GUI actions and are registered at startup
     * in GUI.java.
     */
    //Start the simulation.
    public static final String START_SIM = "Start simulation.";
    //Stop the simulation.
    public static final String STOP_SIM = "Stop simulation.";
    //Hand brake activated.
    public static final String ACTIVATE_BRAKE = "Activate brake.";
    //Hand brake deactivated.
    public static final String DEACTIVATE_BRAKE = "Deactivate brake.";
    //GearTypes changed.
    public static final String GEAR_CHANGE ="GearTypes change";
    //Speed entered by user.
    public static final String SPEED = "Speed.";
}
