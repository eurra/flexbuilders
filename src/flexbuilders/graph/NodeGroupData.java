
package flexbuilders.graph;

import flexbuilders.core.BuildException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import optefx.util.metadata.Metadata;

/**
 *
 * @author Enrique Urra C.
 */
public final class NodeGroupData implements Metadata
{
    private static final HashMap<Class, ArrayList<NodeId>> setsCache = new HashMap<>();
    
    private static ArrayList<NodeId> getCacheFor(Class setType)
    {
        if(setsCache.containsKey(setType))
            return setsCache.get(setType);
        
        Field[] fields = setType.getFields();
        ArrayList<NodeId> list = new ArrayList<>(fields.length);
        
        for(int i = 0; i < fields.length; i++)
        {
            Field currField = fields[i];
            Class fieldType = currField.getType();

            if(NodeId.class.isAssignableFrom(fieldType) && Modifier.isStatic(currField.getModifiers()))
            {
                NodeId<?> foundId;
                
                try
                {
                    foundId = (NodeId)currField.get(null);
                }
                catch(IllegalAccessException | IllegalArgumentException ex)
                {
                    throw new BuildException("Cannot retrieve the node loader of field '" + currField.toGenericString() + "': " + ex.getLocalizedMessage(), ex);
                }
                
                list.add(foundId);
            }
        }
        
        setsCache.put(setType, list);
        return list;
    }
    
    private ArrayList<Class> setTypes;

    public NodeGroupData(Class<? extends NodeGroup>... setTypes)
    {
        if(setTypes.length == 0)
            throw new IllegalArgumentException("Zero-length group types");
                
        this.setTypes = new ArrayList<>(setTypes.length);
        
        for(int i = 0; i < setTypes.length; i++)
        {
            if(setTypes[i] == null)
                throw new NullPointerException("Null group type at position " + i);
            
            this.setTypes.add(setTypes[i]);
        }
    }
    
    public boolean containsId(NodeId id)
    {
        for(Class setType : setTypes)
        {
            if(getCacheFor(setType).contains(id))
                return true;
        }
        
        return false;
    }
    
    public List<NodeId> getAllIds()
    {
        ArrayList<NodeId> allIds = new ArrayList<>();
        
        for(Class setType : setTypes)
            allIds.addAll(getCacheFor(setType));
        
        return allIds;
    }
}
