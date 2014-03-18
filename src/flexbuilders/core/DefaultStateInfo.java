
package flexbuilders.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
public class DefaultStateInfo implements BuildStateInfo
{
    private String name;
    private Map<String, Object> stateMap;

    public DefaultStateInfo()
    {
        this.stateMap = new HashMap<>();
    }
    
    public DefaultStateInfo setName(String name)
    {
        this.name = name;
        return this;
    }
    
    public DefaultStateInfo addStateData(String key, Object val)
    {
        stateMap.put(key, val);
        return this;
    }

    @Override
    public String getInfo()
    {
        StringBuilder sb = new StringBuilder("Entity ").append(name == null ? "(no-name)" : "'" + name + "'");
        
        if(stateMap.size() > 0)
        {
            sb.append(" (");
            
            for(String stateKey : stateMap.keySet())
                sb.append(stateKey).append("=").append(stateMap.get(stateKey)).append(";");
            
            sb.append(")");
        }

        return sb.toString();
    }
}
