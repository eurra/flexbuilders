

package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface BeanSetterInput extends Delegate
{
    BeanSetterInput setMethodName(String methodName) throws BuildException;
    BeanSetterInput setArgType(Class argType) throws BuildException;
}
