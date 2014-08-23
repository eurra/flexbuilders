
package flexbuilders.graph.old;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.BuilderInputException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultGraphHandler// implements DisposableGraphHandler
{
    /*private static class DefaultNodeMap implements NodeMap
    {
        private final HashMap<NodeId, Object> innerMap;

        public DefaultNodeMap(HashMap<NodeId, Object> innerMap)
        {
            this.innerMap = innerMap;
        }

        @Override
        public Object get(NodeId id) throws NoSuchElementException
        {
            if(innerMap == null || !innerMap.containsKey(id))
                throw new NoSuchElementException();
            
            return innerMap.get(id);
        }

        @Override
        public <T> T getAs(NodeId id, Class<T> type) throws NoSuchElementException, ClassCastException
        {
            Object res = get(id);
            return type.cast(res);
        }
    }*/
    
    /*private Map<NodeId, DefaultNodeBuilder> nodes;
    private boolean disposed;
    
    public DefaultGraphHandler()
    {
        this.nodes = new HashMap<>();
    }

    private void checkDisposed()
    {
        if(disposed)
            throw new BuilderInputException("This graph builder is disposed and cannot be modified");
    }
    
    @Override
    public NodeBuilder loadNode(NodeId id)
    {
        checkDisposed();
        
        if(id == null)
            throw new BuilderInputException("The provided id cannot be null");

        DefaultNodeBuilder node = nodes.get(id);
        
        if(node == null)
        {
            node = new DefaultNodeBuilder().setId(id);
            nodes.put(id, node);
        }
        else if(node.isHidden())
        {
            throw new BuilderInputException("The specified id is hidden and cannot be retrieved");
        }
        
        return node;
    }
    
    NodeBuilder getNodeByReference(NodeId id)
    {
        checkDisposed();
        return nodes.get(id);
    }

    @Override
    public boolean hasNode(NodeId id)
    {
        checkDisposed();
        return nodes.containsKey(id);
    }

    @Override
    public NestedBuilder createRef(NodeId destId)
    {
        checkDisposed();

        if(destId == null)
            throw new BuilderInputException("The destination id cannot be null");

        return new DefaultReferenceBuilder(destId, this);
    }

    @Override
    public DefaultGraphHandler dispose()
    {
        if(!disposed)
        {
            disposed = true;
            nodes = null;
        }
        
        return this;
    }*/
}
