package simulation;

/**
 * Enum holding GUI simulation initialization statuses.
 */
public enum InitStatus
{
    NON_NUMBER_ERROR,
    INVALID_DRIVE_NEUTRAL_SPEED_ERROR,
    INVALID_REVERSE_SPEED_ERROR,
    INVALID_PARK_SPEED_ERROR,
    NO_INPUT_ENTERED_ERROR,
    GOOD;

    /**
     *  Enums toString.
     * @return Description of error
     */
    @Override
    public String toString() {
        switch(this)
        {
            case NON_NUMBER_ERROR:
                return "Non-numeric input in the set speed field.";
            case INVALID_DRIVE_NEUTRAL_SPEED_ERROR:
                return "Speed must be between 0 and 140 MPH when starting in drive or neutral.";
            case INVALID_REVERSE_SPEED_ERROR:
                return "Speed must be between 0 and 40 when starting in reverse.";
            case INVALID_PARK_SPEED_ERROR:
                return "You must have a speed of 0 to start in park.";
            case NO_INPUT_ENTERED_ERROR:
                return "No speed provided.";
            default:
                return "NULL";
        }
    }
}
