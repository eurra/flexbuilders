
package flexbuilders.graph;

import optefx.util.metadata.MetadataManager;
import optefx.util.metadata.MetadataProxy;

/**
 *
 * @author Enrique Urra C.
 */
public final class GraphFactory
{
    public static ExtensibleGraph graph()
    {
        return new DefaultExtensibleGraph();
    }
    
    public static <T> NodeId<T> nodeId(NodeData<? extends T>... metaData)
    { 
        return nodeId(null, metaData);
    }
    
    public static <T> NodeId<T> nodeId(String nodeName, NodeData<? extends T>... metaData)
    {
        NodeId<T> node = new NodeId<T>()
        {
            private final MetadataProxy<NodeData> innerProxy = new MetadataProxy(this);

            @Override
            public <T extends NodeData> T getData(Class<T> dataType)
            {
                return innerProxy.getData(dataType);
            }

            @Override
            public <T extends NodeData> T[] getAllData(Class<T> dataType)
            {
                return innerProxy.getAllData(dataType);
            }

            @Override
            public boolean hasData(Class<? extends NodeData> dataType)
            {
                return innerProxy.hasData(dataType);
            }
            
            @Override
            public String toString()
            {
                if(nodeName == null || nodeName.isEmpty())
                    return "(no-name)";
                
                return nodeName;
            }
        };
        
        MetadataManager dataManager = MetadataManager.getInstance();
        
        for(int i = 0; i < metaData.length; i++)
            dataManager.attachData(node, metaData[i]);
        
        return node;
    }

    private GraphFactory()
    {
    }
}
