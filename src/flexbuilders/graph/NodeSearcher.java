
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface NodeSearcher<T>
{
    boolean canStartSearchFor(NestableGraph graph);
    NodeProvider<T> startSearchFor(NestableGraph graph) throws BuildException;
    NestedBuilder<T> getResult();
    NestedBuilder<T>[] getAllResults();
}
