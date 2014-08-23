
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;
import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuilderInputException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultArrayBuilder<T> extends AbstractBuilder implements Delegate, ArrayBuilder<T>
{
    private Class<T> type;
    private int length;
    private Map<Integer, NestedBuilder> builders;
    
    public DefaultArrayBuilder(Class<T> type)
    {        
        if(type == null)
            throw new BuilderInputException("Null type");
        
        this.type = type;
        this.builders = new HashMap<>();
    }

    @Override
    public DefaultArrayBuilder<T> setLength(int length)
    {
        if(length < 0)
            throw new BuilderInputException("The provided length cannot be negative");
        
        if(length < this.length)
            throw new BuilderInputException("The provided length must be greater than the current count (" + length + ")");
        
        this.length = length;
        return this;
    }

    private void checkIndex(int index)
    {
        if(index < 0)
            throw new BuilderInputException("The specified index must be positive ('" + index + "' was provided)");
        
        if(!isIndexAvailable(index))
            throw new BuilderInputException("Such index (" + index + ") is already in use");
        
        if(index >= length)
            length = index + 1;
    }

    private int findNextIndex()
    {
        int count = length;
        
        for(int i = 0; i < count; i++)
        {
            if(isIndexAvailable(i))
                return i;
        }
        
        return count;
    }

    private boolean isIndexAvailable(int index)
    {
        return builders.get(index) == null;
    }

    @Override
    public DefaultArrayBuilder<T> elem(NestedBuilder<? extends T> elem)
    {
        return elem(findNextIndex(), elem); 
    }

    @Override
    public DefaultArrayBuilder<T> elem(int index, NestedBuilder<? extends T> elem)
    {
        if(elem != null)
        {
            checkIndex(index);
            builders.put(index, elem);
        }
        
        return this;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        Object array = Array.newInstance(type, length);
        session.registerResult(this, array);
        
        T elem = null;

        try
        {
            for(Integer pos : builders.keySet())
            {
                NestedBuilder<T> buildable = builders.get(pos);
                elem = buildable.build(session);
                Array.set(array, pos, elem);
            }
        }
        catch(IllegalArgumentException ex)
        {
            throw new BuildException("Wrong element (" + (elem != null ? elem.getClass() : elem) + ") for the array type (" + type + ")", ex);
        }
    }

    @Override
    public NestedBuilder<T[]> asArray()
    {
        return this;
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("Array").
            addStateData("type", type.getSimpleName()).
            addStateData("length", length + "");
    }
}
