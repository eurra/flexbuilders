
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface NodeLoader<T>
{
    NestedBuilder<T> load(BuilderGraph gh) throws BuildException;
}
