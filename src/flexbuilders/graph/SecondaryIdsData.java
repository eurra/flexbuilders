
package flexbuilders.graph;

/**
 *
 * @author Enrique Urra C.
 */
public final class SecondaryIdsData<T> implements NodeData<T>
{
    private final NodeId<T>[] ids;

    public SecondaryIdsData(NodeId<T>... ids)
    {
        this.ids = new NodeId[ids.length];
        
        for(int i = 0; i < ids.length; i++)
        {
            if(ids[i] == null)
                throw new NullPointerException("Null node id at position " + i);
            
            this.ids[i] = ids[i];
        }
    }
    
    public int getIdsCount()
    {
        return ids.length;
    }
    
    public NodeId<T> getId(int pos) throws IndexOutOfBoundsException
    {
        return ids[pos];
    }
}
