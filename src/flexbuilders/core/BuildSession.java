
package flexbuilders.core;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 
 * @author Enrique Urra C.
 */
public final class BuildSession
{
    public static BuildSession newSesion()
    {
        return new BuildSession();
    }
    
    private final HashSet<NestedBuilder> startedSessions;
    private final HashMap<NestedBuilder, Object> sessionResults;

    private BuildSession()
    {
        this.startedSessions = new HashSet<>();
        this.sessionResults = new HashMap<>();
    }
    
    boolean start(NestedBuilder builder)
    {
        if(builder == null)
            throw new NullPointerException("Null builder");
        
        if(!startedSessions.contains(builder))
        {
            startedSessions.add(builder);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public <T> void registerResult(NestedBuilder<T> builder, T result) throws BuildException
    {
        if(!startedSessions.contains(builder))
            throw new BuildException("The builder has not started the session");
        
        if(sessionResults.containsKey(builder))
            throw new BuildException("The builder has already registered its result");
        
        sessionResults.put(builder, result);
    }
    
    <T> T retrieveResult(NestedBuilder<T> builder)
    {
        if(!sessionResults.containsKey(builder))
            return null;
        
        return (T)sessionResults.get(builder);
    }
}
