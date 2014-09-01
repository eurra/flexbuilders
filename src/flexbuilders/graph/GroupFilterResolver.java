
package flexbuilders.graph;

import flexbuilders.core.BuildException;

/**
 *
 * @author Enrique Urra C.
 */
public class GroupFilterResolver implements NodeResolver
{
    private final NodeResolver innerResolver;
    private final NodeGroupData customSets;

    public GroupFilterResolver(NodeResolver innerResolver)
    {
        this(innerResolver, null);
    }
    
    public GroupFilterResolver(NodeResolver innerResolver, NodeGroupData customSets)
    {
        if(innerResolver == null)
            throw new NullPointerException("Null inner resolver");
        
        this.innerResolver = innerResolver;
        this.customSets = customSets;
    }

    @Override
    public <T> void resolve(NodeProvider<T> provider, NodeId<T> requested, BuilderGraph source) throws BuildException
    {
        boolean resolveInner = false;
        NodeGroupData graphFilters = source.getData(NodeGroupData.class);
        
        if(graphFilters != null)
            resolveInner = graphFilters.containsId(requested);
        
        if(!resolveInner && customSets != null)
            resolveInner = customSets.containsId(requested);
        
        if(resolveInner)
            innerResolver.resolve(provider, requested, source);
    }
}
