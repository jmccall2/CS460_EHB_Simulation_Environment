package simulation.engine;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * These are essentially global variables accessible to the
 * entire simulation.engine/application. They can be in part default-initialized
 * via code, part overwritten by command line arguments, and part overwritten
 * by values read in from a file.
 *
 * @author Justin Hall
 */
public class ConsoleVariables {
    private HashMap<String, ConsoleVariable> _cvars = new HashMap<>();
    private HashMap<ConsoleVariable, Integer> _cvarEditCounts = new HashMap<>(); // Keeps track of how many times the cvars were edited
    private ArrayList<ConsoleVariable> _editedCvars = new ArrayList<>();

    public LinkedList<ConsoleVariable> getAllConsoleVariables()
    {
        LinkedList<ConsoleVariable> cvars = new LinkedList<>();
        for (Map.Entry<String, ConsoleVariable> entry : _cvars.entrySet())
        {
            cvars.add(entry.getValue());
        }
        return cvars;
    }

    public void printAllConsoleVariables()
    {
        System.out.println("---Console Variable Listing---");
        for (Map.Entry<String, ConsoleVariable> entry : _cvars.entrySet())
        {
            System.out.println(entry.getValue());
        }
    }

    /**
     * Registers a console variable with the cvar system
     */
    public void registerVariable(ConsoleVariable cvar)
    {
        // If it already exists then do nothing except potentially
        // override the default value if it is different
        if (contains(cvar.getcvarName()))
        {
            find(cvar.getcvarName()).setDefault(cvar.getcvarDefault());
        }
        else
        {
            System.out.println("Registering console variable (" + cvar + ")");
            _cvars.put(cvar.getcvarName(), cvar);
            _cvarEditCounts.put(cvar, cvar.getEditCount());
        }
    }

    /**
     * Determines which variables have been changed since the last time this
     * method was called
     * @return list containing all variables that were changed
     */
    public ArrayList<ConsoleVariable> getVariableChangesSinceLastCall()
    {
        _editedCvars.clear();
        for (Map.Entry<String, ConsoleVariable> entry : _cvars.entrySet())
        {
            ConsoleVariable cvar = entry.getValue();
            if (_cvarEditCounts.get(cvar) != cvar.getEditCount())
            {
                _editedCvars.add(cvar);
                _cvarEditCounts.put(cvar, cvar.getEditCount());
            }
        }
        return _editedCvars;
    }

    public void unregisterVariable(String cvar)
    {
        _cvars.remove(cvar);
    }

    public boolean contains(String cvar)
    {
        return _cvars.containsKey(cvar);
    }

    /**
     * Warning! This can return null!
     */
    public ConsoleVariable find(String cvar)
    {
        if (contains(cvar)) return _cvars.get(cvar);
        return null;
    }
}
