
package flexbuilders.graph;

import flexbuilders.basic.ArrayBuilder;
import static flexbuilders.basic.BasicBuilders.array;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.WrapperBuilder;
import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 * @author Enrique Urra C.
 */
class PriorityNodeBuilder<T> extends WrapperBuilder<T> implements NodeBuilder<T>
{
    private static final ValueComparator comparatorInstance = new ValueComparator();
    
    private static class ValueComparator implements Comparator<ValueEntry>
    {
        @Override
        public int compare(ValueEntry o1, ValueEntry o2)
        {
            if(o1.priority.getPriority() < o2.priority.getPriority())
            {
                return 1;
            }
            else if(o1.priority == o2.priority)
            {
                if(o1.order > o2.order)
                    return 1;
                else
                    return -1;
            }
            else
            {
                return -1;
            }
        }
    }
    
    private static class ValueEntry<T>
    {
        private NestedBuilder<T> value;
        private final int order;
        private final NodePriority priority;
        
        public ValueEntry(int order, NodePriority priority)
        {
            this.order = order;
            this.priority = priority;
        }
    }
    
    private static class ArrayWrapper<T> extends WrapperBuilder<T[]>
    {
        private final Class<T> type;
        private final PriorityNodeBuilder<T> innerBuilder;

        public ArrayWrapper(Class<T> type, PriorityNodeBuilder<T> innerBuilder)
        {
            this.type = type;
            this.innerBuilder = innerBuilder;
        }

        @Override
        public NestedBuilder<T[]> getWrappedForBuild() throws BuildException
        {
            ArrayBuilder arrBuilder = array(type);
            
            for(ValueEntry valueEntry : innerBuilder.valueEntries)
                arrBuilder.elem(valueEntry.value);
            
            return arrBuilder;
        }

        @Override
        public BuildStateInfo getStateInfo()
        {
            return new DefaultStateInfo().
                setName("NodeArrayWrapper").
                addStateData("values-count", innerBuilder.valueEntries.size());
        }
    }
    
    private final NodeId<T> id;
    private final DefaultNestableGraph owner;
    private ValueEntry manuallySetEntry;
    private final TreeSet<ValueEntry> valueEntries;
    
    public PriorityNodeBuilder(NodeId<T> id, DefaultNestableGraph owner)
    {
        this.id = id;
        this.owner = owner;
        this.valueEntries = new TreeSet<>(comparatorInstance);
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
        
        ValueEntry entry;
        
        if(priority == NodePriority.MANUALLY_SET)
        {
            if(manuallySetEntry == null)
            {
                manuallySetEntry = entry = new ValueEntry(valueEntries.size(), priority);
                valueEntries.add(entry);
            }
            else
            {
                entry = manuallySetEntry;
            }
        }
        else
        {
            entry = new ValueEntry(valueEntries.size(), priority);
            valueEntries.add(entry);
        }
        
        entry.value = buildable;
        return this;
    }

    NestedBuilder<T> getValue()
    {
        if(valueEntries.isEmpty())
        {
            NestedBuilder<T> result = null;
            NestableGraph[] dependencies = owner.getDependencies();
            
            for(int i = 0; i < dependencies.length && result == null; i++)
                result = dependencies[i].getValue(id);
            
            return result;
        }
        else
        {
            return valueEntries.last().value;
        }
    }
    
    public NestedBuilder<T[]> asArray(Class<T> type)
    {
        return new ArrayWrapper<>(type, this);
    }
    
    @Override
    public NestedBuilder<T> getWrappedForBuild() throws BuildException
    {
        NestedBuilder<T> toBuild = getValue();
        
        if(toBuild == null)
            throw new BuildException("No value has been configured in the graph and its dependencies");
        
        return toBuild;
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        String valueClass;
        String valueObj;
        NestedBuilder<T> value = getValue();
        
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
