
package flexbuilders.core;

/**
 * Implements an exception that is thrown when an error occurs in the build
 * process carred out by multiple mutable builders. An build error severity can
 * be specified through the constructors, and the {@link BuildError#FATAL}
 * is used by default. This severity may help clients to determine the following
 * steps on the build handling after receiving an error.
 * 
 * @author Enrique Urra C.
 */
public class BuildException extends Exception
{
    private BuildErrorType severity = BuildErrorType.FATAL;
    private BuildErrorSource errorSource;
    
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

    public boolean isErrorSourceSet()
    {
        return errorSource != null;
    }
    
    public BuildException setErrorSource(BuildErrorSource errorSource)
    {
        this.errorSource = errorSource; 
        return this;
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
        String errorInfoStr = null;
        
        if(errorSource != null)
        {
            BuildErrorInfo errorInfo = errorSource.getErrorInfo();
            
            if(errorInfo != null)
                errorInfoStr = errorInfo.getInfo();
        }
        
        if(errorInfoStr == null)
            errorInfoStr = "";
        else
            errorInfoStr = ". Error source:\n" + errorInfoStr;
                
        return "BUILD ERROR: " + super.getLocalizedMessage() + errorInfoStr;
    }
}