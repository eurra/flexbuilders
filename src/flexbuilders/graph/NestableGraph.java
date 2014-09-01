
package flexbuilders.graph;

import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface NestableGraph extends MutableGraph
{
    NestableGraph addDependencies(NestableGraph... dependencies);
    <T> NestedBuilder<T> findValue(NodeId<T> id);
    <T> void findValue(NodeId<T> id, NodeSearcher<T> search);
    <T> NestedBuilder<T>[] findAllValues(NodeId<T> id);
    <T> void findAllValues(NodeId<T> id, NodeSearcher<T> search);
}
