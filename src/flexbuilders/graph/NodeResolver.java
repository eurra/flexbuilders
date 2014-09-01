
package flexbuilders.graph;

import flexbuilders.core.BuildException;

/**
 *
 * @author Enrique Urra C.
 */
public interface NodeResolver
{
    <T> void resolve(NodeProvider<T> provider, NodeId<T> requested, BuilderGraph source) throws BuildException;
}