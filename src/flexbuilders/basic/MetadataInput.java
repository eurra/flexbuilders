
package flexbuilders.basic;

import optefx.util.metadata.Metadata;
import flexbuilders.core.Builder;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface MetadataInput<T> extends Delegate
{
    MetadataInput setTarget(NestedBuilder<T> target);
    MetadataInput attachData(Builder<? extends Metadata> data);
}
