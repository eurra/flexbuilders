
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;

/**
 *
 * @author Enrique Urra C.
 */
public interface InterfaceSetterBuilder extends Builder, Buildable<Setter>, InterfaceSetterInput
{
    @Override InterfaceSetterBuilder setInjector(Class injector) throws BuildException;
}
