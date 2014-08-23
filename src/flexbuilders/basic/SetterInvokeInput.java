
package flexbuilders.basic;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface SetterInvokeInput extends Delegate
{
    SetterInvokeInput values(NestedBuilder... values);
    SetterInvokeInput valueIn(String fieldName, NestedBuilder value);
    SetterInvokeInput valueIn(NestedBuilder<String> fieldName, NestedBuilder value);
}
