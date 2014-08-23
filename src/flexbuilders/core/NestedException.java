
package flexbuilders.core;

import java.util.ArrayList;

/**
 *
 * @author Enrique Urra C.
 */
public class NestedException extends BuildException
{
    private final BuildException mainCause;
    private final ArrayList<NestedBuilder> buildStack;
    
    public NestedException(BuildException mainCause)
    {
        super(mainCause);
        
        this.mainCause = mainCause;
        this.buildStack = new ArrayList<>();
    }
    
    public void addStateInfo(NestedBuilder builder)
    {
        if(builder != null)
            buildStack.add(builder);
    }

    @Override
    public String getLocalizedMessage()
    {
        StringBuilder sb = new StringBuilder("\n==== BUILD SESSION STACK ====\n");            
        buildStack.stream().forEach((nested) -> sb.append(nested).append(" [").append(System.identityHashCode(nested)).append("]").append("\n"));
        
        return mainCause.getLocalizedMessage() + sb;
    }
}
