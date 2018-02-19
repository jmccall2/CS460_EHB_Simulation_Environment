package interfaces;

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
