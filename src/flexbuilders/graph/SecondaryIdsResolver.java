
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
public final class SecondaryIdsResolver<T> implements NodeResolver
{    
    @Override
    public <T> void resolve(NodeProvider<T> provider, NodeId<T> requested, BuilderGraph source) throws BuildException
    {
        NodeGroupData graphSetData = source.getData(NodeGroupData.class);
        
        if(graphSetData != null)
        {
            List<NodeId> providerIds = graphSetData.getAllIds();
            
            for(NodeId<T> idToCheck : providerIds)
            {
                SecondaryIdsData setData = idToCheck.getData(SecondaryIdsData.class);

                if(setData != null)
                {
                    int count = setData.getIdsCount();

                    for(int i = 0; i < count; i++)
                    {
                        NodeId<T> currId = setData.getId(i);
                        
                        if(requested.equals(currId))
                        {
                            NodePriority priority;

                            if(i == 0)
                                priority = NodePriority.HIGH;
                            else if(i == 1)
                                priority = NodePriority.NORMAL;
                            else
                                priority = NodePriority.LOW;

                            provider.provideNode(source.node(idToCheck), priority);
                        }
                    }
                }
            }
        
            
        }
    }
}
