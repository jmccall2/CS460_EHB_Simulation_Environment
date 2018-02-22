package interfaces;

/**
 * This enum represents the set of valid color types that can
 * be used to set the color of the brake button.
 */
public enum ButtonColorTypes
{
    BLUE,
    RED,
    GREEN,
    ORANGE,
    YELLOW,
    LIGHTBLUE;

    @Override
    public String toString() {
        switch(this)
        {
            case BLUE:
                return "blue";
            case RED:
                return "red";
            case GREEN:
                return "green";
            case ORANGE:
                return "orange";
            case YELLOW:
                return "yellow";
            case LIGHTBLUE:
                return "lightblue";
            default:
                return "NULL";
        }
    }
}
