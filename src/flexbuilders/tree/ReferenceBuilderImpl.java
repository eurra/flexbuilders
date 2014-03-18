
package flexbuilders.tree;

import flexbuilders.core.BuildException;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.Builder;
import flexbuilders.core.StackBuildable;

/**
 *
 * @author Enrique Urra C.
 */
class ReferenceBuilderImpl extends StackBuildable implements Builder, Buildable
{
    private String dest;
    private Buildable refBuildable;
    
    public ReferenceBuilderImpl setId(String dest) throws BuildException
    {
        this.dest = dest;
        return this;
    }

    public String getId()
    {
        return dest;
    }

    public ReferenceBuilderImpl setReferenced(Buildable refBuildable) throws BuildException
    {
        this.refBuildable = refBuildable;
        return this;
    }
    
    public Buildable getReferenced()
    {
        return refBuildable;
    }
    
    public boolean isSolved()
    {
        return refBuildable != null;
    }

    @Override
    public void buildInstance() throws BuildException
    {
        if(refBuildable == null)
            throw new BuildException("The reference has not been solved");
    }

    @Override
    public Object getInstance() throws BuildException
    {
        return refBuildable.build();
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("Ref").
            addStateData("dest", dest);
    }
}
