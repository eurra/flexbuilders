
package flexbuilders.graph;

import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface NestableGraph extends BuilderGraph, DisposableGraph
{
    /**
     * Gets the value (in builder form) currently stored in this graph with the 
     * specified id. If no value is currently associated with the id, a null 
     * value is returned, therefore, this is not a null-safe operation.
     * @param <T> The type of the buildable entity.
     * @param id The node id.
     * @return The builder instance, or null if the id has not been added.
     */
    <T> NestedBuilder<T> getValue(NodeId<T> id);
}
