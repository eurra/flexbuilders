
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 * @param <T>
 */
public interface NodeProvider<T>
{
    void provideNode(NestedBuilder<T> builder) throws BuildException;
    void provideNode(NestedBuilder<T> builder, NodePriority priority) throws BuildException;
}
