
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;
import static flexbuilders.graph.NodePriority.AUTOLOADED;
import java.util.HashMap;

/**
 *
 * @author Enrique Urra C.
 */
public class AutoLoadResolver implements NodeResolver
{
    private final HashMap<NodeId, NestedBuilder> loadedResults;

    public AutoLoadResolver()
    {
        this.loadedResults = new HashMap<>();
    }
    
    @Override
    public <T> void resolve(NodeProvider<T> provider, NodeId<T> requested, BuilderGraph source) throws BuildException
    {
        NodeLoaderData<T> data = requested.getData(NodeLoaderData.class);
        
        if(data != null)
        {
            NestedBuilder<T> result = loadedResults.get(requested);
            
            if(result == null)
            {
                result = data.getLoader().load(source);
                loadedResults.put(requested, result);
            }
            
            provider.provideNode(result, AUTOLOADED);
        }
    }
}
