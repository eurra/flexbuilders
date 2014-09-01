
package flexbuilders.graph;

import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface MutableGraph extends BuilderGraph
{
    /**
     * Sets a value related to the specified id in the graph, returning a value
     * builder with the id.
     * @param <T> The type of the buildable entity.
     * @param id The node id.
     * @param value The value to set.
     * @return The value builder instance related to the added value.
     */
    <T> NestedBuilder<T> setValue(NodeId<T> id, NestedBuilder<T> value);
    
    <T> NestedBuilder<T> setValue(NodeId<T> id, NestedBuilder<T> value, NodePriority priority);
    
    <T> NodeBuilder<T> forNode(NodeId<T> id);
}
