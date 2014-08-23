
package flexbuilders.basic;

import static flexbuilders.basic.BasicBuilders.builderFor;
import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.BuilderInputException;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Enrique Urra C.
 */
public class DefaultGetterInvokeBuilder<T> extends AbstractBuilder<T> implements GetterInvokeBuilder<T>
{
    private final NestedBuilder source;
    private final Class<T> resultType;
    private NestedBuilder<String> fieldName;    

    public DefaultGetterInvokeBuilder(NestedBuilder source, Class<T> resultType)
    {
        if(source == null)
            throw new BuilderInputException("Null source");
        
        this.source = source;
        this.resultType = resultType;
    }
    
    @Override
    public NestedBuilder<T> valueIn(String fieldName)
    {
        return valueIn(builderFor(fieldName));
    }

    @Override
    public NestedBuilder<T> valueIn(NestedBuilder<String> fieldName)
    {
        this.fieldName = fieldName;
        return this;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        if(fieldName == null)
            throw new BuilderInputException("The field has not been provided");

        Object sourceObj = source.build(session);
        String baseName = fieldName.build(session);
        String targetMethodName = "get" + baseName.substring(0, 1).toUpperCase() + (baseName.length() > 1 ? baseName.substring(1) : "");            
        Class sourceClass = sourceObj.getClass();
        Method[] allMethods = sourceClass.getMethods();
        Method foundMethod = null;

        for(int i = 0; i < allMethods.length && foundMethod == null; i++)
        {
            if(!allMethods[i].getName().equals(targetMethodName))
                continue;

            if(allMethods[i].getReturnType().equals(Void.TYPE))
                continue;

            if(allMethods[i].getParameterTypes().length == 0)
                foundMethod = allMethods[i];
        }

        if(foundMethod == null)
        {
            throw new BuildException("Cannot find any suitable getter method '" + targetMethodName + "' for type '" + sourceClass + "'");
        }
        else
        {
            try
            {
                if(!foundMethod.isAccessible())
                    foundMethod.setAccessible(true);
                
                Object toGetRaw = foundMethod.invoke(sourceObj);
                
                if(resultType != null && !resultType.isAssignableFrom(toGetRaw.getClass()))
                    throw new BuildException("The getter '" + foundMethod + "' do not provide a result compatible with '" + resultType + "'" );
                    
                session.registerResult(this, (T)toGetRaw);
            }
            catch(IllegalAccessException ex)
            {
                throw new BuildException("Cannot access to the getter '" + foundMethod + "'", ex);
            }
            catch(IllegalArgumentException ex)
            {
                throw new BuildException("Error while invoking the getter '" + foundMethod + "': " + ex.getLocalizedMessage(), ex);
            }
            catch(InvocationTargetException ex)
            {
                Throwable cause = ex.getCause();
                throw new BuildException("Error while executing the getter '" + foundMethod + "': " + cause.getLocalizedMessage(), cause);
            }
        }
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().setName("GetterInvoke");
    }
}
