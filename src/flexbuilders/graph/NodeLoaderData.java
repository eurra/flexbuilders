
package flexbuilders.graph;

/**
 *
 * @author Enrique Urra C.
 */
public final class NodeLoaderData<T> implements NodeData<T>
{
    private final NodeLoader<T> loader;

    public NodeLoaderData(NodeLoader<T> loader)
    {
        if(loader == null)
            throw new NullPointerException("Null node loader");
        
        this.loader = loader;
    }

    public NodeLoader<T> getLoader()
    {
        return loader;
    }
}
