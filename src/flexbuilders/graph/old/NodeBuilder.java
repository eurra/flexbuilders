
package flexbuilders.graph.old;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.BuildSession;

/**
 *
 * @author Enrique Urra C.
 */
public interface NodeBuilder extends NestedBuilder, AccessConfigurable
{
    NodeBuilder value(NestedBuilder source);
    boolean hasValue();
    NestedBuilder getValue();
    <K extends NestedBuilder> K getValueAs(Class<K> buildableType);
    NodeBuilder addToSet(NodeBuilderSet set);
    boolean isRoot();
    boolean isWritable();
    boolean isReadable();
    boolean isAccessConfigured();
    <K> K buildAs(Class<K> type) throws BuildException;
    <K> K buildAs(Class<K> type, BuildSession session) throws BuildException;
}
