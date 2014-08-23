
package flexbuilders.basic;

import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface SetterInvokeBuilder<T> extends NestedBuilder<T>, SetterInvokeInput
{
    @Override SetterInvokeBuilder<T> values(NestedBuilder... values);
    @Override SetterInvokeBuilder<T> valueIn(String fieldName, NestedBuilder value);
    @Override SetterInvokeBuilder<T> valueIn(NestedBuilder<String> fieldName, NestedBuilder value);
}
