
package flexbuilders.basic;

import optefx.util.metadata.MetadataManager;
import optefx.util.metadata.Metadata;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.StackBuildable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
class MetadataBuilderImpl<T> extends StackBuildable<T> implements MetadataBuilder<T>
{
    private T target;
    private Buildable<T> buildableTarget;
    private List<Buildable> metadataList;

    public MetadataBuilderImpl()
    {
        this.metadataList = new ArrayList<>();
    }

    @Override
    public MetadataBuilder<T> setTarget(Buildable<T> target) throws BuildException
    {
        this.buildableTarget = target;
        return this;
    }
    
    @Override
    public MetadataBuilder<T> attachData(Buildable<? extends Metadata> data) throws BuildException
    {
        if(data != null)
            metadataList.add(data);
        
        return this;
    }
    
    @Override
    public void buildInstance() throws BuildException
    {
        if(buildableTarget == null)
            throw new BuildException("The buildable target has not been set");
        
        target = buildableTarget.build();
        MetadataManager manager = MetadataManager.getInstance();
        
        for(Buildable<Metadata> metadataBuildable : metadataList)
            manager.attachData(target, metadataBuildable.build());
    }

    @Override
    public T getInstance() throws BuildException
    {
        return target;
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("Metadata");
    }
}
