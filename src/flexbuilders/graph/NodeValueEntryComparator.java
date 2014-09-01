
package flexbuilders.graph;

import java.util.Comparator;

/**
 *
 * @author Enrique Urra C.
 */
class NodeValueEntryComparator implements Comparator<NodeValueEntry>
{
    private final boolean invertedOrder;

    public NodeValueEntryComparator()
    {
        this(false);
    }
    
    public NodeValueEntryComparator(boolean invertedOrder)
    {
        this.invertedOrder = invertedOrder;
    }

    @Override
    public int compare(NodeValueEntry o1, NodeValueEntry o2)
    {
        if(o1.getPriority().getPriority() < o2.getPriority().getPriority())
        {
            return 1;
        }
        else if(o1.getPriority().getPriority() == o2.getPriority().getPriority())
        {
            if((!invertedOrder && o1.getOrder() > o2.getOrder()) || (invertedOrder && o1.getOrder() < o2.getOrder()))
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
