
package flexbuilders.graph;

import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public final class GraphFactory
{
    public static NestableGraph graph(NestableGraph... dependencies)
    {
        return new DefaultNestableGraph(dependencies);
    }
    
    public static ScannableGraph scannerGraph(NestableGraph... dependencies)
    {
        return new DefaultScannableGraph(dependencies);
    }
    
    public static <T> NodeId<T> nodeId()
    {
        return new NodeId<T>() {};
    }
    
    public static <T> NodeLoader<T> customLoader(NodeLoader<T> loader, NodeProcessor<T>... triggers)
    {
        if(loader == null)
            throw new NullPointerException("Null loader");
        
        NodeProcessor[] newTriggers = new NodeProcessor[triggers.length];
        
        for(int i = 0; i < triggers.length; i++)
        {
            if(triggers[i] == null)
                throw new NullPointerException("Null trigger at position " + i);
            
            newTriggers[i] = triggers[i];
        }
        
        return (BuilderGraph gh) ->
        {
            NestedBuilder<T> value = loader.load(gh);
            
            for(int i = 0; i < newTriggers.length; i++)
            {
                if(newTriggers[i] == null)
                    throw new NullPointerException("Null trigger at position " + i);
                
                newTriggers[i].process(value, gh);
            }
            
            return value;
        };
    }
    
    private GraphFactory()
    {
    }
}
