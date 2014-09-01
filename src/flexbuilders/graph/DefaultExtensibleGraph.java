
package flexbuilders.graph;

import flexbuilders.basic.ArrayBuilder;
import static flexbuilders.basic.BasicBuilders.array;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.BuilderInputException;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.WrapperBuilder;
import java.util.HashMap;
import java.util.HashSet;
import optefx.util.metadata.Metadata;
import optefx.util.metadata.MetadataProxy;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultExtensibleGraph implements ExtensibleGraph
{
    private static class SimpleNodeReference<T> extends WrapperBuilder<T>
    {
        private final NodeId<T> refId;
        private final DefaultExtensibleGraph target;

        public SimpleNodeReference(NodeId<T> refId, DefaultExtensibleGraph target)
        {
            this.refId = refId;
            this.target = target;
        }

        @Override
        public NestedBuilder<T> getWrappedForBuild() throws BuildException
        {
            NestedBuilder<T> toBuild = target.findValue(refId);
        
            if(toBuild == null)
                throw new BuildException("No value with id " + refId + " has been added in the graph and its dependencies");

            return toBuild;
        }

        @Override
        public BuildStateInfo getStateInfo()
        {
            return new DefaultStateInfo().
                setName("SimpleNodeReference").
                addStateData("refId", refId);
        }
    }
    
    private static class MultiNodeReference<T> extends WrapperBuilder<T[]>
    {
        private final NodeId<T> refId;
        private final DefaultExtensibleGraph target;
        private final Class<T> type;

        public MultiNodeReference(NodeId<T> refId, DefaultExtensibleGraph target, Class<T> type)
        {
            this.refId = refId;
            this.target = target;
            this.type = type;
        }

        @Override
        public NestedBuilder<T[]> getWrappedForBuild() throws BuildException
        {
            NestedBuilder[] allValues = target.findAllValues(refId);
        
            if(allValues.length == 0)
                throw new BuildException("No value with id " + refId + "  has been added in the graph and its dependencies");
            
            ArrayBuilder arrBuilder = array(type);
            
            for(int i = 0; i < allValues.length; i++)
                arrBuilder.elem(allValues[i]);
            
            return arrBuilder;
        }

        @Override
        public BuildStateInfo getStateInfo()
        {
            return new DefaultStateInfo().
                setName("MultiNodeReference").
                addStateData("refId", refId);
        }
    }
    
    private static class InnerNodeBuilder<T> extends WrapperBuilder<T> implements NodeBuilder<T>
    {        
        private final NodeId<T> refId;
        private final DefaultExtensibleGraph target;
        private final SimpleNodeReference<T> innerRef;

        public InnerNodeBuilder(NodeId<T> refId, DefaultExtensibleGraph target)
        {
            this.refId = refId;
            this.target = target;
            this.innerRef = new SimpleNodeReference<>(refId, target);
        }

        @Override
        public NodeBuilder<T> setValue(NestedBuilder<T> value)
        {
            target.setValue(refId, value);
            return this;
        }

        @Override
        public NodeBuilder<T> setValue(NestedBuilder<T> value, NodePriority priority)
        {
            target.setValue(refId, value, priority);
            return this;
        }
        
        @Override
        public NestedBuilder<T> getWrappedForBuild() throws BuildException
        {
            return innerRef.getWrappedForBuild();
        }
        
        @Override
        public BuildStateInfo getStateInfo()
        {
            return innerRef.getStateInfo();
        }
    }
    
    private HashMap<NodeId, PriorityNodeBuilder> nodes;
    private HashSet<NestableGraph> dependencies;
    private HashSet<NodeResolver> resolvers;
    private MetadataProxy<Metadata> dataProvider;
    private boolean disposed;
    
    public DefaultExtensibleGraph()
    {
        this.nodes = new HashMap<>();
        this.dependencies = new HashSet<>();
        this.resolvers = new HashSet<>();
        this.dataProvider = new MetadataProxy<>(this);
    }

    private void checkDisposed()
    {
        if(disposed)
            throw new BuilderInputException("This graph builder is disposed and cannot be modified");
    }

    @Override
    public ExtensibleGraph addDependencies(NestableGraph... dependencies)
    {
        for(int i = 0; i < dependencies.length; i++)
        {
            if(dependencies[i] == null)
                throw new NullPointerException("Null graph at position " + i);
            
            this.dependencies.add(dependencies[i]);
        }
        
        return this;
    }
    
    @Override
    public DefaultExtensibleGraph addResolver(NodeResolver resolver)
    {
        checkDisposed();
        
        if(resolver == null)
            throw new NullPointerException("Null processor type");
        
        resolvers.add(resolver);
        return this;
    }
    
    @Override
    public <T> NestedBuilder<T> node(NodeId<T> id)
    {
        checkDisposed();
        return new SimpleNodeReference<>(id, this);
    }

    @Override
    public <T> NestedBuilder<T> node(NodeId<T> id, NestedBuilder<T> defaultValue)
    {
        if(defaultValue != null)
            setValue(id, defaultValue, NodePriority.DEFAULT_VALUE);
        
        return node(id);
    }
    
    @Override
    public <T> NestedBuilder<T[]> multiNode(NodeId<T> id, Class<T> type)
    {
        checkDisposed();
        return new MultiNodeReference<>(id, this, type);
    }

    <T> void findInGraph(NodeId<T> id, NodeProvider<T> provider)
    {        
        if(nodes.containsKey(id))
        {
            NodeValueEntry<T> entry = nodes.get(id).getFirstPriority();
            provider.provideNode(entry.getValue(), entry.getPriority());
        }
        
        for(NodeResolver resolver : resolvers)
            resolver.resolve(provider, id, this);
    }
    
    @Override
    public <T> NestedBuilder<T> findValue(NodeId<T> id)
    {
        checkDisposed();
        
        DefaultNodeSearcher search = new DefaultNodeSearcher(id);
        findValue(id, search);
        
        return search.getResult();
    }
    
    @Override
    public <T> void findValue(NodeId<T> id, NodeSearcher<T> search)
    {
        checkDisposed();
        
        if(search.canStartSearchFor(this))
        {
            findInGraph(id, search.startSearchFor(this));
        
            for(NestableGraph dependency : dependencies)
                dependency.findValue(id, search);
        }
    }

    @Override
    public <T> NestedBuilder<T>[] findAllValues(NodeId<T> id)
    {
        checkDisposed();
        
        DefaultNodeSearcher search = new DefaultNodeSearcher(id);
        findAllValues(id, search);
        
        return search.getAllResults();
    }

    @Override
    public <T> void findAllValues(NodeId<T> id, NodeSearcher<T> search)
    {
        checkDisposed();
        
        if(search.canStartSearchFor(this))
        {
            findInGraph(id, search.startSearchFor(this));
        
            for(NestableGraph dependency : dependencies)
                dependency.findAllValues(id, search);
        }
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
        
        if(id == null)
            throw new BuilderInputException("Null node id");
        
        if(value == null)
            throw new BuilderInputException("Null builder");
        
        if(priority == null)
            throw new BuilderInputException("Null priority");
        
        PriorityNodeBuilder node = nodes.get(id);
        
        if(node == null)
        {
            node = new PriorityNodeBuilder(id);
            nodes.put(id, node);
        }
        
        node.setValue(value, priority);
        return node;
    }

    @Override
    public <T> NodeBuilder<T> forNode(NodeId<T> id)
    {
        return new InnerNodeBuilder<>(id, this);
    }

    @Override
    public DefaultExtensibleGraph addData(Metadata... data)
    {
        checkDisposed();
        dataProvider.addData(data);
        
        return this;
    }
    
    @Override
    public <T extends Metadata> T getData(Class<T> dataType)
    {
        checkDisposed();
        return dataProvider.getData(dataType);
    }

    @Override
    public <T extends Metadata> T[] getAllData(Class<T> dataType)
    {
        checkDisposed();
        return dataProvider.getAllData(dataType);
    }

    @Override
    public boolean hasData(Class<? extends Metadata> dataType)
    {
        checkDisposed();
        return dataProvider.hasData(dataType);
    }
    
    @Override
    public DefaultExtensibleGraph dispose()
    {
        if(!disposed)
        {
            disposed = true;
            nodes = null;
            dependencies = null;
            resolvers = null;
            dataProvider = null;
        }
        
        return this;
    }
}
