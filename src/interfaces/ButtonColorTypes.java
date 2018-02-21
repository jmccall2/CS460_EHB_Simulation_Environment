package interfaces;

/* We can add however many colors we want, we just have to map them to actual FX colors later. */

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
