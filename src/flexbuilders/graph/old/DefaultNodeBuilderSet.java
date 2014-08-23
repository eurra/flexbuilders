
package flexbuilders.graph.old;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultNodeBuilderSet implements NodeBuilderSet
{
    private final Set<NodeBuilder> nodes;

    public DefaultNodeBuilderSet()
    {
        this.nodes = new HashSet<>();
    }
    
    @Override
    public void addNode(NodeBuilder node)
    {
        if(node != null && !nodes.contains(node))
            nodes.add(node);
    }
    
    @Override
    public void open()
    {
        for(NodeBuilder node : nodes)
            node.open();
    }
    
    @Override
    public void readOnly()
    {
        for(NodeBuilder node : nodes)
            node.readOnly();
    }
    
    @Override
    public void seal()
    {
        for(NodeBuilder node : nodes)
            node.seal();
    }

    @Override
    public void hide()
    {
        for(NodeBuilder node : nodes)
            node.hide();
    }
}
