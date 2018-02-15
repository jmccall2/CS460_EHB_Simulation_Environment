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
    public static final String SET_ENGAGED_SOUND = "set_engaged_sound";
    public static final String SET_DISENGAGED_SOUND = "set_disengaged_sound";

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
    //Car is put in park.
    public static final String PARK ="Park.";
    //Car is put in neutral.
    public static final String NEUTRAL = "Neutral.";
    //Car is put in drive.
    public static final String DRIVE = "Drive.";
    //Car is put in first gear.
    public static final String FIRST = "First.";
    //Car is put in second gear.
    public static final String SECOND = "Second.";
    //Speed entered by user.
    public static final String SPEED = "Speed.";
}
