
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;

/**
 *
 * @author Enrique Urra C.
 */
public interface ObjectBuilder<T> extends Builder, Buildable<T>, ObjectInput
{
    @Override ObjectBuilder<T> nextArgument(Buildable arg) throws BuildException;
}
