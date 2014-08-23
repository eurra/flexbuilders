
package flexbuilders.graph.old;

import flexbuilders.core.NestedBuilder;
import flexbuilders.graph.NodeId;

/**
 *
 * @author Enrique Urra C.
 */
public interface ReferenceHandler
{
    NestedBuilder createRef(NodeId destId);
}
