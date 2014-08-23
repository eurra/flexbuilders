
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.BuilderInputException;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.WrapperBuilder;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Enrique Urra C.
 */
final class DefaultNestableGraph implements NestableGraph
{
    private static class LoaderWrapper<T> extends WrapperBuilder<T>
    {
        private NestedBuilder<T> value;

        @Override
        public NestedBuilder<T> getWrappedForBuild() throws BuildException
        {
            return value;
        }

        @Override
        public BuildStateInfo getStateInfo()
        {
            return new DefaultStateInfo().
                setName("NodeIdLoaderWrapper");
        }
    }
    
    private HashMap<NodeId, PriorityNodeBuilder> nodes;
    private HashMap<NodeLoader, LoaderWrapper> loadableNodes;
    private HashSet<SubGraphLoader> loadedSubGraphs;
    private NestableGraph[] dependencies;
    private boolean disposed;
    
    public DefaultNestableGraph(NestableGraph... dependencies)
    {
        this.dependencies = new NestableGraph[dependencies.length];
        
        for(int i = 0; i < dependencies.length; i++)
        {
            if(dependencies[i] == null)
                throw new NullPointerException("Null graph at position " + i);
            
            this.dependencies[i] = dependencies[i];
        }
        
        this.nodes = new HashMap<>();
        this.loadableNodes = new HashMap<>();
        this.loadedSubGraphs = new HashSet<>();
    }

    private void checkDisposed()
    {
        if(disposed)
            throw new BuilderInputException("This graph builder is disposed and cannot be modified");
    }
    
    private <T> PriorityNodeBuilder<T> getCustomNode(NodeId<T> id)
    {
        checkDisposed();
        
        if(id == null)
            throw new BuilderInputException("Null node id");
        
        PriorityNodeBuilder node = nodes.get(id);
        
        if(node == null)
        {
            node = new PriorityNodeBuilder(id, this);
            nodes.put(id, node);
        }
        
        return node;
    }

    @Override
    public <T> NestedBuilder<T> loadNode(NodeLoader<T> loader)
    {
        checkDisposed();
        
        if(loader == null)
            throw new BuilderInputException("Null node loader");
        
        if(loadableNodes.containsKey(loader))
        {
            return loadableNodes.get(loader);
        }
        else
        {
            LoaderWrapper wrapper = new LoaderWrapper();
            loadableNodes.put(loader, wrapper);
            NestedBuilder value = loader.load(this);

            if(value == null)
                throw new BuilderInputException("The node loader returns a null builder value");

            wrapper.value = value;
            return wrapper;
        }
    }
    
    @Override
    public void loadSubGraph(SubGraphLoader loader) throws BuildException
    {
        checkDisposed();
        
        if(loader == null)
            throw new NullPointerException("Null subgraph loader");
        
        if(!loadedSubGraphs.contains(loader))
        {
            loadedSubGraphs.add(loader);
            loader.loadSubGraph(this);
        }
    }
    
    @Override
    public <T> NestedBuilder<T> getNode(NodeId<T> id)
    {
        return getCustomNode(id);
    }

    @Override
    public <T> NestedBuilder<T> getValue(NodeId<T> id)
    {
        return getCustomNode(id).getValue();
    }

    @Override
    public <T> NestedBuilder<T[]> getAllFromNode(NodeId<T> id, Class<T> arrayType)
    {
        return getCustomNode(id).asArray(arrayType);
    }

    @Override
    public <T> NestedBuilder<T> setValue(NodeId<T> id, NestedBuilder<T> value)
    {
        return setValue(id, value, NodePriority.MANUALLY_SET);
    }

    @Override
    public <T> NestedBuilder<T> setValue(NodeId<T> id, NestedBuilder<T> value, NodePriority priority)
    {
        checkDisposed();
        
        if(value == null)
            throw new BuilderInputException("Null builder");
        
        PriorityNodeBuilder<T> node = getCustomNode(id);
        node.setValue(value, priority);
        
        return node;
    }

    @Override
    public <T> NodeBuilder<T> forNode(NodeId<T> id)
    {
        return getCustomNode(id);
    }
    
    NestableGraph[] getDependencies()
    {
        return dependencies;
    }
    
    @Override
    public DefaultNestableGraph dispose()
    {
        if(!disposed)
        {
            disposed = true;
            nodes = null;
            loadableNodes = null;
            dependencies = null;
            loadedSubGraphs = null;
        }
        
        return this;
    }
}
