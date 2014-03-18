
package flexbuilders.basic;

import java.util.ArrayList;
import java.util.List;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import flexbuilders.core.StackBuildable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Enrique Urra C.
 */
class ObjectBuilderImpl<T> extends StackBuildable<T> implements Delegate, ObjectBuilder<T>
{    
    private Class<T> type;
    private List<Buildable> args;
    private T object;

    public ObjectBuilderImpl(Class<T> type)
    {
        if(type == null)
            throw new NullPointerException("Null type");
        
        this.type = type;
        this.args = new ArrayList<>();
    }
    
    @Override
    public ObjectBuilderImpl<T> nextArgument(Buildable arg) throws BuildException
    {
        if(arg == null)
            throw new BuildException("The provided buildable cannot be null");
        
        args.add(arg);
        return this;
    }

    @Override
    public void buildInstance() throws BuildException
    {
        Object[] argsArr = new Object[args.size()];
        int index = 0;

        for(Buildable arg : args)
            argsArr[index++] = arg.build();

        Constructor[] constructors = type.getDeclaredConstructors();
        object = null;

        for(int i = 0; object == null && i < constructors.length; i++)
        {
            try
            {
                if(!constructors[i].isAccessible())
                    constructors[i].setAccessible(true);

                object = (T)constructors[i].newInstance(argsArr);
            }
            catch(ClassCastException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                continue;
            }
        }

        if(object == null)
        {
            String argsStr = "";

            for(int i = 0; i < argsArr.length; i++)
                argsStr += argsArr[i].getClass() + (i == argsArr.length - 1 ? "" : ", ");

            throw new BuildException("No constructor has been found in the '" + type.getName() + "' class " + (argsStr.isEmpty() ? "with no arguments" : "with the following arguments: " + argsStr));
        }
    }

    @Override
    public T getInstance() throws BuildException
    {
        return object;
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("Object").
            addStateData("type", type.getSimpleName());
    }
}