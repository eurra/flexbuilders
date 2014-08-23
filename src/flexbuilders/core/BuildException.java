
package flexbuilders.core;

/**
 * Implements an exception that is thrown when an error occurs in the build
 * process carried out by multiple mutable builders. An build error severity can
 * be specified through the constructors, and the {@link BuildError#FATAL}
 * is used by default. This severity may help clients to determine the following
 * steps on the build handling after receiving an error.
 * 
 * @author Enrique Urra C.
 */
public class BuildException extends RuntimeException
{
    private BuildErrorType severity = BuildErrorType.FATAL;
    
    public BuildException(String message)
    {
        super(message);
    }
    
    public BuildException(Throwable thrwbl)
    {
        super(thrwbl);
    }
    
    public BuildException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }
    
    public BuildException severity(BuildErrorType severity)
    {
        this.severity = severity;
        return this;
    }

    public BuildErrorType getSeverity()
    {
        return severity;
    }
    
    @Override
    public String getLocalizedMessage()
    {       
        return "BUILD ERROR (severity " + severity + "): " + super.getLocalizedMessage();
    }
}