
package flexbuilders.core;

/**
 *
 * @author Enrique Urra C.
 */
public class BuilderInputException extends BuildException
{
    public BuilderInputException(String message)
    {
        super(message);
    }
    
    public BuilderInputException(Throwable thrwbl)
    {
        super(thrwbl);
    }
    
    public BuilderInputException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }
}
