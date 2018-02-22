package interfaces;

/**
 * This enum represents is used to represent the set of valid gear states
 * that the car can be in. No other gear states are possible for this simulation.
 */
public enum GearTypes
{
    PARK,
    REVERSE,
    NEUTRAL,
    DRIVE;

    @Override
    public String toString() {
        switch(this)
        {
            case PARK:
                return "Park";
            case REVERSE:
                return "Reverse";
            case NEUTRAL:
                return "Neutral";
            case DRIVE:
                return "Drive";
            default:
                return "NULL";
        }
    }
}
