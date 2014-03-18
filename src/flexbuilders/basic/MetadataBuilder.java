
package flexbuilders.basic;

import optefx.util.metadata.Metadata;
import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;

/**
 *
 * @author Enrique Urra C.
 */
public interface MetadataBuilder<T> extends Builder, Buildable<T>, MetadataInput<T>
{
    @Override MetadataBuilder<T> setTarget(Buildable<T> target) throws BuildException;
    @Override MetadataBuilder<T> attachData(Buildable<? extends Metadata> data) throws BuildException;
}
