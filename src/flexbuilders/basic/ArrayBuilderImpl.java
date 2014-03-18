
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import flexbuilders.core.StackBuildable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
class ArrayBuilderImpl<T> extends StackBuildable implements Delegate, ArrayBuilder<T>
{    
    private Object array;
    private Class<T> type;
    private int length;
    private Map<Integer, Buildable> builders;
    
    public ArrayBuilderImpl(Class<T> type)
    {        
        if(type == null)
            throw new NullPointerException("Null type");
        
        this.type = type;
        this.builders = new HashMap<>();
    }

    @Override
    public ArrayBuilderImpl<T> setLength(int length) throws BuildException
    {
        if(length < 0)
            throw new BuildException("The provided length cannot be negative");
        
        if(length < this.length)
            throw new BuildException("The provided length must be greater than the current count (" + length + ")");
        
        this.length = length;
        return this;
    }

    private void checkIndex(int index) throws BuildException
    {
        if(index < 0)
            throw new BuildException("The specified index must be positive ('" + index + "' was provided)");
        
        if(!isIndexAvailable(index))
            throw new BuildException("Such index (" + index + ") is already in use");
        
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
    public ArrayBuilderImpl<T> elem(Buildable<? extends T> elem) throws BuildException
    {
        return elem(findNextIndex(), elem); 
    }

    @Override
    public ArrayBuilderImpl<T> elem(int index, Buildable<? extends T> elem) throws BuildException
    {
        if(elem != null)
        {
            checkIndex(index);
            builders.put(index, elem);
        }
        
        return this;
    }

    @Override
    public void buildInstance() throws BuildException
    {
        array = Array.newInstance(type, length);        
        T elem = null;

        try
        {
            for(Integer pos : builders.keySet())
            {
                Buildable<T> buildable = builders.get(pos);
                elem = buildable.build();
                Array.set(array, pos, elem);
            }
        }
        catch(IllegalArgumentException ex)
        {
            throw new BuildException("Wrong element (" + (elem != null ? elem.getClass() : elem) + ") for the array type (" + type + ")", ex);
        }
    }

    @Override
    public Buildable<T[]> asArray()
    {
        return this;
    }
    
    @Override
    public Object getInstance() throws BuildException
    {
        return array;
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
