
package flexbuilders.graph.old;

import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuilderInputException;
import flexbuilders.core.WrapperBuilder;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultReferenceBuilder// extends WrapperBuilder
{
    /*private final NodeId dest;
    private final DefaultGraphHandler ownerBuilder;
    private NestedBuilder refBuildable;

    public DefaultReferenceBuilder(NodeId dest, DefaultGraphHandler ownerBuilder)
    {
        this.dest = dest;
        this.ownerBuilder = ownerBuilder;
    }

    public NodeId getId()
    {
        return dest;
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("Node Ref").
            addStateData("dest", dest);
    }

    @Override
    public void onWrappedBuild(NestedBuilder wrappedBuilder) throws BuildException
    {
        refBuildable = ownerBuilder.getNodeByReference(dest);
        
        if(refBuildable == null)
            throw new BuilderInputException("Reference not solved (" + dest + ")");
        
        setWrappedBuilder(refBuildable);
    }*/
}
