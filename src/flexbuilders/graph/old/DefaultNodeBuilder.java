
package flexbuilders.graph.old;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.BuilderInputException;
import flexbuilders.core.WrapperBuilder;
import flexbuilders.graph.NodeId;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultNodeBuilder extends WrapperBuilder implements NodeBuilder
{
    private NodeId id;
    private NestedBuilder value;
    private boolean writable = true;
    private boolean readable = true;
    private boolean accessConfigured = false;
    private boolean hidden = false;
    private boolean root;
    
    public DefaultNodeBuilder setId(NodeId id)
    {
        this.id = id;
        return this;
    }
    
    public DefaultNodeBuilder setRoot()
    {
        this.root = true;
        return this;
    }

    public NodeId getId()
    {
        return id;
    }

    @Override
    public DefaultNodeBuilder value(NestedBuilder buildable)
    {
        if(!writable)
            throw new BuilderInputException("This node is not writable and the value cannot be set");

        value = buildable;
        return this;
    }

    @Override
    public boolean hasValue()
    {
        return value != null;
    }
    
    @Override
    public NestedBuilder getValue()
    {
        if(value == null)
            throw new BuilderInputException("The value has not been set");
        
        return value;
    }
    
    @Override
    public <T extends NestedBuilder> T getValueAs(Class<T> buildableType)
    {
        if(!readable)
            throw new BuilderInputException("The node is not readable and the value cannot be obtained");
        
        if(buildableType == null)
            throw new BuilderInputException("The value type cannot be null");
        
        NestedBuilder buildableCheck = getValue();
        
        if(!buildableType.isAssignableFrom(buildableCheck.getClass()))
            throw new BuilderInputException("Cannot get the value as an instance of '" + buildableType + "'");
        
        return (T)buildableCheck;
    }
    
    @Override
    public boolean isRoot()
    {
        return root;
    }

    @Override
    public boolean isWritable()
    {
        return writable;
    }

    @Override
    public boolean isReadable()
    {
        return readable;
    }

    @Override
    public boolean isAccessConfigured()
    {
        return accessConfigured;
    }

    @Override
    public void open()
    {
        if(writable && readable)
            return;

        if(accessConfigured)
            throw new BuilderInputException("Cannot change the access of the node: it is already configured");

        this.accessConfigured = true;
    }

    @Override
    public void readOnly()
    {
        if(!writable && readable)
            return;

        if(accessConfigured)
            throw new BuilderInputException("Cannot change the access of the node: it is already configured");

        if(value == null)
            throw new BuilderInputException("Cannot set the node read-only: The value has not been configured yet");

        this.writable = false;
        this.accessConfigured = true;
    }

    @Override
    public void seal()
    {
        if(!writable && !readable)
            return;

        if(accessConfigured)
            throw new BuilderInputException("Cannot change the access of the node: it is already configured");

        if(value == null)
            throw new BuilderInputException("Cannot close the node: The value has not been configured yet");

        this.writable = false;
        this.readable = false;
        this.accessConfigured = true;
    }

    @Override
    public void hide()
    {
        hidden = true;
    }

    @Override
    public NodeBuilder addToSet(NodeBuilderSet set)
    {
        if(set == null)
            throw new BuilderInputException("Null set");
        
        set.addNode(this);
        return this;
    }
    
    public boolean isHidden()
    {
        return hidden;
    }

    @Override
    public NestedBuilder getWrappedForBuild() throws BuildException
    {
        if(value == null)
            throw new BuilderInputException("The value has not been set");
        
        return value;
    }
    
    
    @Override
    public <K> K buildAs(Class<K> type) throws BuildException
    {
        Object res = build();
        
        if(!type.isAssignableFrom(res.getClass()))
            throw new BuildException("Cannot convert the result to '" + type + "'");
        
        return (K)res;
    }
    
    @Override
    public <K> K buildAs(Class<K> type, BuildSession session) throws BuildException
    {
        Object res = build(session);
        
        if(!type.isAssignableFrom(res.getClass()))
            throw new BuildException("Cannot convert the result to '" + type + "'");
        
        return (K)res;
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        String valueClass;
        String valueObj;
        
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
