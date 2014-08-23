
package flexbuilders.core;

/**
 *
 * @author Enrique Urra C.
 * @param <T>
 */
public abstract class AbstractBuilder<T> implements NestedBuilder<T>, StateBuilder
{
    @Override
    public final T build() throws BuildException
    {
        BuildSession newSession = BuildSession.newSesion();
        return build(newSession);
    }

    @Override
    public final T build(BuildSession session) throws BuildException
    {
        if(session == null)
            throw new NullPointerException("Null session");
        
        T result;
        
        try
        {
            if(!session.start(this))
            {
                result = session.retrieveResult(this);
                
                if(result == null)
                    throw new BuildException("Circular dependency not resolved");
            }
            else
            {
                buildInstance(session);
                result = session.retrieveResult(this);
                
                if(result == null)
                    throw new BuildException("Build result not provided in the 'buildInstance' method'");
            }
        }
        catch(NestedException ex)
        {
            ex.addStateInfo(this);
            throw ex;
        }
        catch(BuildException ex)
        {
            NestedException newEx = new NestedException(ex);
            newEx.addStateInfo(this);
            
            throw newEx;
        }
        
        return result;
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
    
    public abstract void buildInstance(BuildSession session) throws BuildException;
}
