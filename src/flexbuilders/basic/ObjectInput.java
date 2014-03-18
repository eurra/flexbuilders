
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface ObjectInput extends Delegate
{
    ObjectInput nextArgument(Buildable arg) throws BuildException;
}
