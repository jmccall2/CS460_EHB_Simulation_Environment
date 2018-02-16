package interfaces;

public enum ButtonSound
{
  ENGAGED,
  DISENGAGED;

  @Override
  public String toString() {
    switch(this)
    {
      case ENGAGED:
        return "Engaged";
      case DISENGAGED:
        return "Disengaged";
      default:
        return "NULL";
    }
  }
}
