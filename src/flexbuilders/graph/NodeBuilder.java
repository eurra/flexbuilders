
package flexbuilders.graph;

import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface NodeBuilder<T> extends NestedBuilder<T>
{
    NodeBuilder<T> setValue(NestedBuilder<T> value);
    NodeBuilder<T> setValue(NestedBuilder<T> value, NodePriority priority);
}
