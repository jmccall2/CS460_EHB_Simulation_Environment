package simulation.engine;

/**
 * Represents an individual key-value console variable and
 * corresponding helper functions. A console variable
 * is a very simple means of storing some piece of data
 * which is meant to be modified from a variety of different
 * input streams. Such input streams include:
 *
 *      Command Line
 *      External Config Files
 *      Other Objects
 *
 * On top of storing the data as a raw string, a console variable
 * will also attempt to cast the string to an integer, double, and
 * boolean. You can then retrieve easily retrieve the pre-casted
 * value via a couple of helper functions.
 *
 * If it fails to cast the data, this is not an issue. It will simply
 * maintain the data as a raw string for later use/manual casting.
 *
 * @author Justin Hall
 */
public class ConsoleVariable {
    private String _cvarName;
    private String _defaultValue;
    private String _cvarValue; // Raw String value
    private int _cvarIntVal; // Defaults to -1 if _cvarValue cannot be casted
    private double _cvarFloatVal; // Defaults to -1.0 if _cvarValue cannot be casted
    private boolean _cvarBoolVal = false; // Defaults to false
    private int _numEdits = 0; // Number of times this variable was edited

    /**
     * Creates a new console variable. Keep in mind that the actual value of the
     * console variable will be determined by the default value given here.
     * @param name name of the variable
     * @param defaultValue default value in the form of a string (used during console variable reset)
     */
    public ConsoleVariable(String name, String defaultValue)
    {
        _cvarName = name;
        //Singleton.simulation.engine.getMessagePump().registerMessage(new Message(_cvarName + "_WAS_CHANGED"));
        _defaultValue = defaultValue;
        _setValueNoMessageDispatch(_defaultValue);
    }

    /**
     * Creates a new, fully-specified console variable
     * @param name name of the console variable
     * @param defaultValue default value in the form of a string (used during console variable reset)
     * @param value starting value of the console variable which is separate from the default value
     */
    public ConsoleVariable(String name, String defaultValue, String value)
    {
        _cvarName = name;
        _defaultValue = defaultValue;
        _setValueNoMessageDispatch(value);
    }

    /**
     * Resets the console variable to its default value
     */
    public void reset()
    {
        setValue(_defaultValue);
    }

    /**
     * @return total number of times this console variable has been edited
     */
    public int getEditCount()
    {
        return _numEdits;
    }

    /**
     * Gets the cvar name
     */
    public String getcvarName()
    {
        return _cvarName;
    }

    /**
     * Gets the raw cvar value as a string (Ex: "127.26")
     */
    public String getcvarValue()
    {
        return _cvarValue;
    }

    public String getcvarDefault()
    {
        return _defaultValue;
    }

    /**
     * Gets cvar value as int (Ex: 127)
     */
    public int getcvarAsInt()
    {
        return _cvarIntVal;
    }

    /**
     * Gets cvar value as double (Ex: 127.26)
     * @return
     */
    public double getcvarAsFloat()
    {
        return _cvarFloatVal;
    }

    public boolean getcvarAsBool()
    {
        return _cvarBoolVal;
    }

    /**
     * Sets the value of the console variable (Ex: "127")
     */
    public void setValue(String value)
    {
        _setValueNoMessageDispatch(value);
        // Notify anyone who is interested that this variable was changed
        //Singleton.simulation.engine.getMessagePump().sendMessage(_cvarName + "_WAS_CHANGED");
    }

    /**
     * This determines what the cvar resets to if reset() is called
     */
    public void setDefault(String defaultValue)
    {
        _defaultValue = defaultValue;
    }

    private void _setValueNoMessageDispatch(String value)
    {
        ++_numEdits;
        _cvarValue = value;
        try
        {
            _cvarIntVal = Integer.parseInt(_cvarValue);
            _cvarFloatVal = Double.parseDouble(_cvarValue);
        }
        catch (Exception e)
        {
            _cvarIntVal = -1;
            _cvarFloatVal = -1.0;
        }
        // Try to cast it to a boolean
        try
        {
            _cvarBoolVal = Boolean.parseBoolean(_cvarValue);
        }
        catch (Exception e)
        {
            _cvarBoolVal = false;
        }
    }

    @Override
    public int hashCode() {
        return _cvarName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ConsoleVariable && ((ConsoleVariable)obj)._cvarName.equals(_cvarName);
    }

    @Override
    public String toString() {
        return "name: " + _cvarName + "; value: " + _cvarValue;
    }
}
