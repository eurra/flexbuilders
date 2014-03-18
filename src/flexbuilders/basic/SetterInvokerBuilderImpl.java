
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.StackBuildable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
class SetterInvokerBuilderImpl<T> extends StackBuildable<T> implements SetterInvokerBuilder<T>
{
    private T target;
    private Buildable<T> targetBuildable;
    private Map<Buildable<? extends Setter>, Buildable> settersToInvoke;

    public SetterInvokerBuilderImpl(Buildable<T> target)
    {
        if(target == null)
            throw new NullPointerException("Null target");
        
        this.targetBuildable = target;
        this.settersToInvoke = new HashMap<>();
    }

    @Override
    public SetterInvokerBuilderImpl<T> set(Buildable<? extends Setter> setter, Buildable arg) throws BuildException
    {
        settersToInvoke.put(setter, arg);
        return this;
    }

    @Override
    public void buildInstance() throws BuildException
    {
        target = targetBuildable.build();

        for(Buildable<? extends Setter> setter : settersToInvoke.keySet())
            setter.build().set(target, settersToInvoke.get(setter).build());
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
            setName("SetterInvoker");
    }
}
