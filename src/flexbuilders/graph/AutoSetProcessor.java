
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public class AutoSetProcessor<T> implements NodeProcessor<T>
{
    private final NodeId<T>[] ids;

    public AutoSetProcessor(NodeId<T>... ids)
    {
        this.ids = new NodeId[ids.length];
        
        for(int i = 0; i < ids.length; i++)
        {
            if(ids[i] == null)
                throw new NullPointerException("Null node id at position " + i);
            
            this.ids[i] = ids[i];
        }
    }
    
    @Override
    public void process(NestedBuilder<T> value, BuilderGraph graph) throws BuildException
    {
        if(ids.length > 0)
            graph.setValue(ids[0], value, NodePriority.HIGH);
        
        if(ids.length > 1)
            graph.setValue(ids[1], value, NodePriority.NORMAL);
        
        if(ids.length > 2)
        {            
            for(int i = 2; i < ids.length; i++)
                graph.setValue(ids[i], value, NodePriority.LOW);
        }
    }
}
