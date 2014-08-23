
package flexbuilders.core;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class WrapperBuilder<T> implements NestedBuilder<T>, StateBuilder
{    
    @Override
    public final T build(BuildSession session) throws BuildException
    {
        try
        {
            NestedBuilder<T> wrappedBuilder = getWrappedForBuild();
            
            if(wrappedBuilder == null)
                throw new BuilderInputException("The inner builder has not been set");
            
            return wrappedBuilder.build(session);
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
    }

    @Override
    public final T build() throws BuildException
    {
        try
        {
            NestedBuilder<T> wrappedBuilder = getWrappedForBuild();
            
            if(wrappedBuilder == null)
                throw new BuilderInputException("The value has not been set");
            
            return wrappedBuilder.build();
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
    
    public abstract NestedBuilder<T> getWrappedForBuild() throws BuildException;
}
