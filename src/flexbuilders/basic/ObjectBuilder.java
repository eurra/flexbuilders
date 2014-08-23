
package flexbuilders.basic;

import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface ObjectBuilder<T> extends NestedBuilder<T>, ObjectInput
{
    @Override ObjectBuilder<T> nextArgument(NestedBuilder arg);
}
