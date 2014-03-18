
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;

/**
 *
 * @author Enrique Urra C.
 */
public interface BeanSetterBuilder extends Builder, Buildable<Setter>, BeanSetterInput
{
    @Override BeanSetterBuilder setMethodName(String methodName) throws BuildException;
    @Override BeanSetterBuilder setArgType(Class argType) throws BuildException;
}
