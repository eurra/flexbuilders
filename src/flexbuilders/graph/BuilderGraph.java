
package flexbuilders.graph;

import flexbuilders.core.NestedBuilder;
import optefx.util.metadata.Metadata;
import optefx.util.metadata.MetadataProvider;

/**
 *
 * @author Enrique Urra C.
 */
public interface BuilderGraph extends MetadataProvider<Metadata>
{
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
    <T> NestedBuilder<T> node(NodeId<T> id);
    
    <T> NestedBuilder<T> node(NodeId<T> id, NestedBuilder<T> defaultValue);
    
    <T> NestedBuilder<T[]> multiNode(NodeId<T> id, Class<T> type);
}
