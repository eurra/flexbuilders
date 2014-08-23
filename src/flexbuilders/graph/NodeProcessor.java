
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface NodeProcessor<T>
{
    void process(NestedBuilder<T> value, BuilderGraph graph) throws BuildException;
}