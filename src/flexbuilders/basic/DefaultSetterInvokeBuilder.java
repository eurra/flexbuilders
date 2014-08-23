
package flexbuilders.basic;

import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.BuilderInputException;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Enrique Urra C.
 */
public class DefaultSetterInvokeBuilder<T> extends AbstractBuilder<T> implements SetterInvokeBuilder<T>
{
    private static class SetEntry
    {
        private final NestedBuilder<String> fieldName;
        private final NestedBuilder argument;

        public SetEntry(NestedBuilder<String> fieldName, NestedBuilder argument)
        {
            if(fieldName == null && argument == null)
                throw new BuilderInputException("Null field name and argument at same time");
            
            this.fieldName = fieldName;
            this.argument = argument;
        }

        public NestedBuilder getArgument()
        {
            return argument;
        }

        public NestedBuilder<String> getFieldName()
        {
            return fieldName;
        }
    }
    
    private final NestedBuilder<T> target;
    private final List<SetEntry> entries;

    public DefaultSetterInvokeBuilder(NestedBuilder<T> target)
    {
        if(target == null)
            throw new BuilderInputException("Null target");
        
        this.target = target;
        this.entries = new ArrayList<>();
    }

    @Override
    public SetterInvokeBuilder<T> values(NestedBuilder... values)
    {
        Arrays.stream(values).forEach((value) ->
            valueIn((NestedBuilder<String>)null, value)
        );
        
        return this;
    }

    @Override
    public SetterInvokeBuilder<T> valueIn(String fieldName, NestedBuilder value)
    {
        return valueIn(BasicBuilders.builderFor(fieldName), value);
    }
    
    @Override
    public SetterInvokeBuilder<T> valueIn(NestedBuilder<String> fieldName, NestedBuilder value)
    {
        entries.add(new SetEntry(fieldName, value));
        return this;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        T targetObject = target.build(session);
        session.registerResult(this, targetObject);
        
        Class targetType = targetObject.getClass();
        Method[] toSearchMethods = targetType.getMethods();
        Set<Method> currentlySetted = new HashSet<>(toSearchMethods.length);
        
        for(SetEntry entry : entries)
        {
            Object argument = (entry.getArgument() == null ? null : entry.getArgument().build(session));
            String baseName = (entry.getFieldName() == null ? null : entry.getFieldName().build(session));
            
            if(baseName != null && baseName.isEmpty())
                throw new BuilderInputException("The field name is empty");
            
            String targetMethodName = (baseName == null ? null : "set" + baseName.substring(0, 1).toUpperCase() + (baseName.length() > 1 ? baseName.substring(1) : ""));

            List<Method> foundMethods = Arrays.stream(toSearchMethods).
                filter(method -> !currentlySetted.contains(method)).
                filter(method -> method.getReturnType().equals(Void.TYPE)).
                filter(method -> {
                    Class[] methodArgsTypes = method.getParameterTypes();
                    String methodName = targetMethodName;
                    
                    if(methodName != null)
                    {
                        if(method.getName().equals(methodName))
                            return true;
                    }
                    else if(method.getName().startsWith("set") && methodArgsTypes[0].isAssignableFrom(argument.getClass()))
                    {
                        return true;
                    }
                    
                    return false;
                }).
                sorted((m1, m2) -> {
                    SetPriority priorityM1 = m1.getAnnotation(SetPriority.class);
                    SetPriority priorityM2 = m2.getAnnotation(SetPriority.class);
                    
                    int p1 = priorityM1 == null ? Integer.MAX_VALUE : priorityM1.value();
                    int p2 = priorityM2 == null ? Integer.MAX_VALUE : priorityM2.value();
                    
                    if(p1 < 0) p1 = 0;
                    if(p2 < 0) p2 = 0;
                    
                    if(p1 > p2)
                        return 1;
                    else if(p1 < p2)
                        return -1;
                    else
                        return 0;
                }).
                collect(Collectors.toList());

            if(foundMethods.isEmpty())
            {
                throw new BuildException(
                    "Cannot find a suitable setter method (or it is already setted) " + 
                    (targetMethodName == null ? "" : "with name '" + targetMethodName + "' ") + 
                    "for type '" + targetType + "' " + 
                    "using as argument '" + (argument == null ? "NULL" : argument.getClass()) + "'"
                );
            }
            else
            {
                Method toSet = foundMethods.get(0);
                
                try
                {
                    if(!toSet.isAccessible())
                        toSet.setAccessible(true);
                    
                    toSet.invoke(targetObject, argument);
                    currentlySetted.add(toSet);
                }
                catch(SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException ex)
                {
                    String cause = null;

                    if(ex.getCause() != null)
                        cause = ex.getCause().getMessage();

                    throw new BuildException("Error while invoking the setter '" + toSet + "' with argument type '" + (argument == null ? "null" : argument.getClass()) + "'" + (cause != null ? ": " + cause : ""), ex);
                }
            }
            
            foundMethods.clear();
        }
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().setName("SetterInvoke");
    }
}
