
package flexbuilders.basic;

import optefx.util.metadata.Metadata;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Builder;

/**
 *
 * @author Enrique Urra C.
 */
public interface MetadataBuilder<T> extends NestedBuilder<T>, MetadataInput<T>
{
    @Override MetadataBuilder<T> setTarget(NestedBuilder<T> target);
    @Override MetadataBuilder<T> attachData(Builder<? extends Metadata> data);
}
