
package flexbuilders.tree;

import java.util.ArrayList;
import flexbuilders.core.BuildErrorType;
import flexbuilders.core.BuildException;

/**
 * Implements a non-fatal exception that is thrown when unsolved references are 
 * detected in the object building process.
 * 
 * @author Enrique Urra C.
 */
public class UnsolvedReferencesException extends BuildException
{
    private ArrayList<String> list;
    
    /**
     * Constructor, which provide an exception message.
     * @param message The message as String.
     */
    public UnsolvedReferencesException(String message)
    {
        super(message);
        severity(BuildErrorType.NONFATAL);
    }

    /**
     * Constructor, which builds from another exception.
     * @param thrwbl The base exception as throwable.
     */
    public UnsolvedReferencesException(Throwable thrwbl)
    {
        super(thrwbl);
        severity(BuildErrorType.NONFATAL);
    }

    /**
     * Constructor with a message and a parent exception.
     * @param string The message.
     * @param thrwbl The parent exception.
     */
    public UnsolvedReferencesException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
        severity(BuildErrorType.NONFATAL);
    }
    
    /**
     * Adds a new unsolved reference to the exception info.
     * @param description The description of the unsolved reference.
     */
    public void addUnsolvedReference(String description)
    {
        if(description == null)
            return;
        
        if(list == null)
            list = new ArrayList<>();
        
        list.add(description);
    }

    @Override
    public String getLocalizedMessage()
    {
        return super.getLocalizedMessage() + getReferencesDetail();
    }
    
    /**
     * Generate a detailed report of the unsolved references.
     * @return The report as String.
     */
    public String getReferencesDetail()
    {
        String res = "\n";
        int count = list.size();
        int i = 0;
        
        for(String entry : list)
        {
            i++;
            res += "* " + entry + (i == count ? "" : "\n");
        }
        
        return res;
    }
}
