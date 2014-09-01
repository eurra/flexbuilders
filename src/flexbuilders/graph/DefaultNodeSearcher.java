
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;
import java.util.HashSet;
import java.util.TreeSet;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultNodeSearcher<T> implements NodeSearcher<T>, NodeProvider<T>
{
    private final TreeSet<NodeValueEntry> entries;
    private final HashSet<NestableGraph> startedSearchs;

    public DefaultNodeSearcher(NodeId<T> id)
    {
        this.entries = new TreeSet<>(new NodeValueEntryComparator(true));
        this.startedSearchs = new HashSet<>();
    }

    @Override
    public boolean canStartSearchFor(NestableGraph graph)
    {
        return !startedSearchs.contains(graph);
    }
    
    @Override
    public NodeProvider<T> startSearchFor(NestableGraph graph) throws BuildException
    {
        if(!canStartSearchFor(graph))
            throw new BuildException("The search is already started for the provided graph");
        
        startedSearchs.add(graph);
        return this;
    }

    @Override
    public void provideNode(NestedBuilder<T> builder) throws BuildException
    {
        provideNode(builder, NodePriority.NORMAL);
    }
    
    @Override
    public void provideNode(NestedBuilder<T> builder, NodePriority priority) throws BuildException
    {
        if(builder == null)
            throw new NullPointerException("Null builder");

        if(priority == null)
            throw new NullPointerException("Null priority");
        
        NodeValueEntry entry = new NodeValueEntry();
        entry.setValue(builder);
        entry.setPriority(priority);
        entry.setOrder(entries.size());
        
        entries.add(entry);
    }

    @Override
    public NestedBuilder<T> getResult()
    {
        if(entries.isEmpty())
            return null;
        
        return entries.last().getValue();
    }

    @Override
    public NestedBuilder<T>[] getAllResults()
    {
        if(entries.isEmpty())
            return new NestedBuilder[0];
        
        NestedBuilder[] res = new NestedBuilder[entries.size()];
        int ind = 0;
        
        for(NodeValueEntry entry : entries)
            res[ind++] = entry.getValue();
        
        return res;
    }
}
