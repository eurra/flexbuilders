
package flexbuilders.basic;

import optefx.util.metadata.MetadataManager;
import optefx.util.metadata.Metadata;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import flexbuilders.core.Builder;
import flexbuilders.core.BuilderInputException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultMetadataBuilder<T> extends AbstractBuilder<T> implements MetadataBuilder<T>
{
    private NestedBuilder<T> buildableTarget;
    private final List<Builder> metadataList;

    public DefaultMetadataBuilder()
    {
        this.metadataList = new ArrayList<>();
    }

    @Override
    public MetadataBuilder<T> setTarget(NestedBuilder<T> target)
    {
        this.buildableTarget = target;
        return this;
    }
    
    @Override
    public MetadataBuilder<T> attachData(Builder<? extends Metadata> data)
    {
        if(data != null)
            metadataList.add(data);
        
        return this;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        if(buildableTarget == null)
            throw new BuilderInputException("The buildable target has not been set");
        
        T target = buildableTarget.build(session);
        session.registerResult(this, target);
        MetadataManager manager = MetadataManager.getInstance();
        
        for(Builder<Metadata> metadataBuildable : metadataList)
            manager.attachData(target, metadataBuildable.build());
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("Metadata");
    }
}
