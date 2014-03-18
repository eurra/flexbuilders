
package flexbuilders.tree;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;

/**
 *
 * @author Enrique Urra C.
 */
public interface TreeBuilder<T> extends Builder, Buildable<T>, TreeInput
{
    @Override TreeBuilder<T> dispose() throws BuildException;
}
