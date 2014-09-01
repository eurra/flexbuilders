
package flexbuilders.graph;

import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
class NodeValueEntry<T>
{
    private NestedBuilder<T> value;
    private int order;
    private NodePriority priority;

    public void setValue(NestedBuilder<T> value)
    {
        this.value = value;
    }

    public NestedBuilder<T> getValue()
    {
        return value;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public int getOrder()
    {
        return order;
    }

    public void setPriority(NodePriority priority)
    {
        this.priority = priority;
    }

    public NodePriority getPriority()
    {
        return priority;
    }
}
