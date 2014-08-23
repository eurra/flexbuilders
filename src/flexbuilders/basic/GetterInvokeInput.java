
package flexbuilders.basic;

import flexbuilders.core.Delegate;
import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface GetterInvokeInput<T> extends Delegate
{
    NestedBuilder<T> valueIn(String fieldName);
    NestedBuilder<T> valueIn(NestedBuilder<String> fieldName);
}
