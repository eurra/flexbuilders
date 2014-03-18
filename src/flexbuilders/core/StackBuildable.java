
package flexbuilders.core;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class StackBuildable<T> implements Buildable<T>, BuildErrorSource
{
    private class InnerErrorInfo implements BuildErrorInfo
    {
        @Override
        public String getInfo()
        {
            BuildStateInfo stateInfo = getStateInfo();
            String stateInfoStr = null;

            if(stateInfo != null)
                stateInfoStr = stateInfo.getInfo();
            
            if(stateInfoStr == null || stateInfoStr.isEmpty())
                stateInfoStr = "(no-state)";
            
            String stackInfo = "";
            
            for(int i = 0; i < creationStack.length; i++)
                stackInfo += "* " + creationStack[i] + "\n";
            
            return stateInfoStr + " - created at:\n" + stackInfo;
        }
    }
    
    private boolean builded;
    private BuildErrorInfo errorInfo = new InnerErrorInfo();
    private StackTraceElement[] creationStack;

    public StackBuildable()
    {
        this.creationStack = new Throwable().getStackTrace();
    }
    
    @Override
    public final T build() throws BuildException
    {
        if(!builded)
        {
            builded = true;
            
            try
            {
                buildInstance();
            }
            catch(BuildException ex)
            {
                if(!ex.isErrorSourceSet())
                    ex.setErrorSource(this);
                
                throw ex;
            }
        }
        
        T instance;
        
        try
        {
            instance = getInstance();
        }
        catch(BuildException ex)
        {
            if(!ex.isErrorSourceSet())
                ex.setErrorSource(this);

            throw ex;
        }
        
        if(instance == null)
            throw new BuildException("The build result is not ready yet (maybe a cyclic dependency?)");
        
        return instance;
    }

    @Override
    public final BuildErrorInfo getErrorInfo()
    {
        return errorInfo;
    }
    
    public BuildStateInfo getStateInfo()
    {
        return null;
    }

    @Override
    public String toString()
    {
        BuildStateInfo stateInfo = getStateInfo();
        String stateInfoStr = null;
        
        if(stateInfo != null)
            stateInfoStr = stateInfo.getInfo();
        
        if(stateInfoStr == null)
            return super.toString();
        
        return stateInfoStr;
    }
    
    public abstract void buildInstance() throws BuildException;
    public abstract T getInstance() throws BuildException;
}
