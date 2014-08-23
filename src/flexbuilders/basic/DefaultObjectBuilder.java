
package flexbuilders.basic;

import java.util.ArrayList;
import java.util.List;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;
import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuilderInputException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultObjectBuilder<T> extends AbstractBuilder<T> implements Delegate, ObjectBuilder<T>
{    
    private Class<T> type;
    private List<NestedBuilder> args;

    public DefaultObjectBuilder(Class<T> type)
    {
        if(type == null)
            throw new BuilderInputException("Null type");
        
        this.type = type;
        this.args = new ArrayList<>();
    }
    
    @Override
    public DefaultObjectBuilder<T> nextArgument(NestedBuilder arg)
    {
        if(arg == null)
            throw new BuilderInputException("The provided buildable cannot be null");
        
        args.add(arg);
        return this;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        Object[] argsArr = new Object[args.size()];
        int index = 0;

        for(NestedBuilder arg : args)
            argsArr[index++] = arg.build(session);

        Constructor[] constructors = type.getDeclaredConstructors();
        T object = null;

        for(int i = 0; object == null && i < constructors.length; i++)
        {
            try
            {
                if(!constructors[i].isAccessible())
                    constructors[i].setAccessible(true);

                object = (T)constructors[i].newInstance(argsArr);
                session.registerResult(this, object);
            }
            catch(ClassCastException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException ex)
            {
            }
            catch(InvocationTargetException ex)
            {
                throw new BuildException("Error when invoking the constructor '" + constructors[i] + "': " + ex.getCause().getLocalizedMessage(), ex);
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
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("Object").
            addStateData("type", type.getSimpleName());
    }
}