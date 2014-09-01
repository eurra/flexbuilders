
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.WrapperBuilder;
import java.util.TreeSet;

/**
 *
 * @author Enrique Urra C.
 */
class PriorityNodeBuilder<T> extends WrapperBuilder<T> implements NodeBuilder<T>
{
    private final NodeId<T> id;
    private NodeValueEntry manuallySetEntry;
    private final TreeSet<NodeValueEntry> valueEntries;
    
    public PriorityNodeBuilder(NodeId<T> id)
    {
        this.id = id;
        this.valueEntries = new TreeSet<>(new NodeValueEntryComparator());
    }

    @Override
    public PriorityNodeBuilder<T> setValue(NestedBuilder<T> buildable)
    {
        return setValue(buildable, NodePriority.MANUALLY_SET);
    }
    
    @Override
    public PriorityNodeBuilder<T> setValue(NestedBuilder<T> buildable, NodePriority priority)
    {
        if(buildable == null)
            throw new NullPointerException("Null buildable");
        
        NodeValueEntry entry;
        
        if(priority == NodePriority.MANUALLY_SET)
        {
            if(manuallySetEntry == null)
            {
                manuallySetEntry = entry = new NodeValueEntry();
                manuallySetEntry.setOrder(valueEntries.size());
                manuallySetEntry.setPriority(priority);
                
                valueEntries.add(entry);
            }
            else
            {
                entry = manuallySetEntry;
            }
        }
        else
        {
            entry = new NodeValueEntry();
            entry.setOrder(valueEntries.size());
            entry.setPriority(priority);
            
            valueEntries.add(entry);
        }
        
        entry.setValue(buildable);
        return this;
    }
    
    public NestedBuilder<T>[] getAllAdded()
    {
        NestedBuilder[] builders = new NestedBuilder[valueEntries.size()];
        int ind = 0;
        
        for(NodeValueEntry valueEntry : valueEntries)
            builders[ind++] = valueEntry.getValue();
        
        return builders;
    }
    
    @Override
    public NestedBuilder<T> getWrappedForBuild() throws BuildException
    {
        return valueEntries.last().getValue();
    }
    
    public NodeValueEntry<T> getFirstPriority()
    {
        return valueEntries.last();
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        String valueClass;
        String valueObj;
        NestedBuilder<T> value = valueEntries.isEmpty() ? null : valueEntries.last().getValue();
        
        if(value != null)
        {
            valueObj = "" + System.identityHashCode(value);
            valueClass = value.getClass().getSimpleName();
        }
        else
        {
            valueObj = "null";
            valueClass = "null";
        }
        
        return new DefaultStateInfo().
            setName("Node").
            addStateData("id", id).
            addStateData("value-obj", valueObj).
            addStateData("value-type", valueClass);
    }
}
