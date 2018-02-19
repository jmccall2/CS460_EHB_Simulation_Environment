package interfaces;

/* We can add however many colors we want, we just have to map them to actual FX colors later. */

public enum ButtonColorTypes
{
    CYAN,
    BLUE,
    RED,
    GREEN,
    PURPLE;

    public String convertToHex()
    {
        switch(this)
        {
            case CYAN:
                return "#16FFF4";
            case BLUE:
                return "#0032FF";
            case RED:
                return "#FF1B00";
            case GREEN:
                return "#34D13E";
            case PURPLE:
                return "#9B00FF";
            default:
                return "#FFFFFF";
        }
    }

    @Override
    public String toString() {
        switch(this)
        {
            case CYAN:
                return "cyan";
            case BLUE:
                return "blue";
            case RED:
                return "red";
            case GREEN:
                return "green";
            case PURPLE:
                return "purple";
            default:
                return "NULL";
        }
    }
}
