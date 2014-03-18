
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface InterfaceSetterInput extends Delegate
{
    InterfaceSetterInput setInjector(Class injector) throws BuildException;
}
