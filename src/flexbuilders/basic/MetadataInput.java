
package flexbuilders.basic;

import optefx.util.metadata.Metadata;
import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface MetadataInput<T> extends Delegate
{
    MetadataInput setTarget(Buildable<T> target) throws BuildException;
    MetadataInput attachData(Buildable<? extends Metadata> data) throws BuildException;
}
