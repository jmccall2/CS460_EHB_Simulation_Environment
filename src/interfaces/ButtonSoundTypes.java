package interfaces;

/**
 * This enum represents the set of sounds that are recognized by
 * the system. Each of these maps directly to an internal sound file.
 */
public enum ButtonSoundTypes
{
  ENGAGED,
  DISENGAGED,
  LONG_BEEP_A,
  LONG_BEEP_B,
  LONG_BEEP_C,
  SHORT_BEEP_A,
  SHORT_BEEP_B;

  @Override
  public String toString() {
    switch(this)
    {
      case ENGAGED:
        return "/resources/sounds/engaged.wav";
      case DISENGAGED:
        return "/resources/sounds/disengaged.wav";
      case LONG_BEEP_A:
        return "/resources/sounds/longBeepA.wav";
      case LONG_BEEP_B:
        return "/resources/sounds/longBeepB.wav";
      case LONG_BEEP_C:
        return "/resources/sounds/longBeepC.wav";
      case SHORT_BEEP_A:
        return "/resources/sounds/shortBeepA.wav";
      case SHORT_BEEP_B:
        return "/resources/sounds/shortBeepB.wav";
      default:
        return "NULL";
    }
  }
}
