
package flexbuilders.basic;

import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface ArrayBuilder<T> extends NestedBuilder, ArrayInput<T>
{
    @Override ArrayBuilder<T> setLength(int length);
    @Override ArrayBuilder<T> elem(NestedBuilder<? extends T> elem);
    @Override ArrayBuilder<T> elem(int index, NestedBuilder<? extends T> elem);
}
