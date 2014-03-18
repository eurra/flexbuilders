
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;

/**
 *
 * @author Enrique Urra C.
 */
public final class SetterBuilders
{    
    public static <T> SetterInvokerBuilder<T> setterInvoker(Buildable<T> target) throws BuildException
    {
        return new SetterInvokerBuilderImpl<>(target);
    }
    
    public static BeanSetterBuilder beanSetter() throws BuildException
    {
        return new BeanSetterBuilderImpl();
    }
    
    public static InterfaceSetterBuilder interfaceSetter() throws BuildException
    {
        return new InterfaceSetterBuilderImpl();
    }
}
