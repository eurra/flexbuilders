
package flexbuilders.basic;

import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuilderInputException;

/**
 *
 * @author Enrique Urra C.
 */
class InstanceBuilderImpl<T> extends AbstractBuilder<T>
{    
    private final T instance;

    public InstanceBuilderImpl(T instance)
    {
        if(instance == null)
            throw new BuilderInputException("A builder instance cannot be null");
        
        this.instance = instance;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        session.registerResult(this, instance);
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().setName("Instance").addStateData("value", instance);
    }
}
