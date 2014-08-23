
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface BuilderGraph
{
    <T> NestedBuilder<T> loadNode(NodeLoader<T> id);
    
    void loadSubGraph(SubGraphLoader loader) throws BuildException;
    
    /**
     * Gets the node (in builder form) related with the specified id. A node always
     * exists even if no value has been added with the id, therefore, this is a
     * null-safe operation. The entity finally builded by the returned node depends
     * on the graph implementation and should be one of the many added values,
     * according to their particular priority.
     * @param <T> The type of the buildable entity.
     * @param id The node id related to the id.
     * @return The node instance.
     */
    <T> NestedBuilder<T> getNode(NodeId<T> id);
    
    <T> NestedBuilder<T[]> getAllFromNode(NodeId<T> id, Class<T> arrayType);
    
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
