
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.StackBuildable;

/**
 *
 * @author Enrique Urra C.
 */
class InstanceBuilderImpl<T> implements Buildable<T>
{
    private class InnerBuildable extends StackBuildable<T>
    {
        private T finalInstance;

        @Override
        public void buildInstance() throws BuildException
        {
            finalInstance = instance;
        }

        @Override
        public T getInstance() throws BuildException
        {
            return finalInstance;
        }

        @Override
        public BuildStateInfo getStateInfo()
        {
            return new DefaultStateInfo().
                setName("Instance");            
        }
    }
    
    private T instance;
    private InnerBuildable innerBuildable;

    public InstanceBuilderImpl(T instance)
    {
        this.instance = instance;
        this.innerBuildable = new InnerBuildable();
    }

    @Override
    public T build() throws BuildException
    {
        return innerBuildable.build();
    }
}
