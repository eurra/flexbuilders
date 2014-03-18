
package flexbuilders.tree;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.StackBuildable;

/**
 *
 * @author Enrique Urra C.
 */
public class DefaultBranchBuilder extends StackBuildable implements BranchBuilder
{
    private String id;
    private Buildable buildable;
    private boolean writable = true;
    private boolean readable = true;
    private boolean frozen = false;
    private boolean root;
    
    public DefaultBranchBuilder setId(String id) throws BuildException
    {
        this.id = id;
        return this;
    }
    
    public DefaultBranchBuilder setRoot() throws BuildException
    {
        this.root = true;
        return this;
    }

    public String getId()
    {
        return id;
    }

    @Override
    public DefaultBranchBuilder setBuildable(Buildable buildable) throws BuildException
    {
        if(!writable)
            throw new BuildException("This branch is not writable and the buildable cannot be set");

        this.buildable = buildable;
        return this;
    }

    @Override
    public <T extends Buildable> T getBuildableAs(Class<T> buildableType) throws BuildException
    {
        if(!readable)
            throw new BuildException("The branch is not readable and the buildable cannot be obtained");
        
        if(buildableType == null)
            throw new BuildException("The buildable type cannot be null");
        
        if(!buildableType.isAssignableFrom(buildable.getClass()))
            throw new BuildException("Cannot get the buildable as an instance of '" + buildableType + "'");
        
        return (T)buildable;
    }
    
    @Override
    public Buildable getBuildable()
    {
        return buildable;
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
    public boolean isFrozen()
    {
        return frozen;
    }

    @Override
    public DefaultBranchBuilder open() throws BuildException
    {
        if(writable && readable)
            return this;

        if(frozen)
            throw new BuildException("Cannot change the access of the branch: it is frozen");

        this.frozen = true;
        return this;
    }

    @Override
    public DefaultBranchBuilder readOnly() throws BuildException
    {
        if(!writable && readable)
            return this;

        if(frozen)
            throw new BuildException("Cannot change the access of the branch: it is frozen");

        if(buildable == null)
            throw new BuildException("Cannot set the branch read-only: The buildable has not been configured yet");

        this.writable = false;
        this.frozen = true;
        
        return this;
    }

    @Override
    public DefaultBranchBuilder seal() throws BuildException
    {
        if(!writable && !readable)
            return this;

        if(frozen)
            throw new BuildException("Cannot change the access of the branch: it is frozen");

        if(buildable == null)
            throw new BuildException("Cannot close the branch: The buildable has not been configured yet");

        this.writable = false;
        this.readable = false;
        this.frozen = true;
        
        return this;
    }

    @Override
    public DefaultBranchBuilder addTo(BranchBuilderSet set) throws BuildException
    {
        if(set != null)
            set.addBranch(this);
        
        return this;
    }

    @Override
    public void buildInstance() throws BuildException
    {
        if(buildable == null)
            throw new BuildException("The buildable has not been set");
    }

    @Override
    public Object getInstance() throws BuildException
    {
        return buildable.build();
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("Branch").
            addStateData("id", id).
            addStateData("buildable-type", buildable == null ? null : buildable.getClass().getSimpleName());
    }
}
