
package flexbuilders.basic;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface ArrayInput<T> extends Delegate
{
    ArrayInput<T> setLength(int length);
    ArrayInput<T> elem(NestedBuilder<? extends T> elem);
    ArrayInput<T> elem(int index, NestedBuilder<? extends T> elem);
    NestedBuilder<T[]> asArray();
}
