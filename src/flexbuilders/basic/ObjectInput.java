
package flexbuilders.basic;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface ObjectInput extends Delegate
{
    ObjectInput nextArgument(NestedBuilder arg);
}
