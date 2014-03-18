
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.StackBuildable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
class BeanSetterBuilderImpl extends StackBuildable<Setter> implements BeanSetterBuilder
{
    private class BeanSetter implements Setter
    {
        private String methodName;
        private Class argType;

        public BeanSetter(String methodName, Class argType)
        {
            this.methodName = methodName;
            this.argType = argType;
        }

        @Override
        public void set(Object objTarget, Object argument) throws BuildException
        {
            if(objTarget == null)
                throw new BuildException("The target object cannot be null");
            
            Class objTargetClass = objTarget.getClass();
            Method[] allMethods = objTargetClass.getMethods();
            List<Method> foundMethods = new ArrayList<>();
            
            for(int i = 0; i < allMethods.length; i++)
            {
                if(!allMethods[i].getReturnType().equals(Void.TYPE))
                    continue;
                
                Class[] methodArgsTypes = allMethods[i].getParameterTypes();
                
                if(methodArgsTypes.length != 1)
                    continue;
                
                Class actualArgType = methodArgsTypes[0];
                String checkMethodName = allMethods[i].getName();
                
                if((methodName != null && !checkMethodName.equals(methodName)) || (methodName == null && !checkMethodName.startsWith("set")))
                    continue;
                
                if(!actualArgType.isPrimitive() && ((argType != null && !actualArgType.equals(argType)) || !actualArgType.isAssignableFrom(argument.getClass())))
                    continue;
                
                foundMethods.add(allMethods[i]);
            }
            
            boolean set = false;
            Method currMethod = null;
            
            try
            {
                for(Method setterMethod : foundMethods)
                {
                    currMethod = setterMethod;
                    
                    if(!setterMethod.isAccessible())
                        setterMethod.setAccessible(true);
                
                    setterMethod.invoke(objTarget, argument); 
                    set = true;
                }
            }
            catch(InvocationTargetException ex)
            {
                String cause = null;
                
                if(ex.getCause() != null)
                    cause = ex.getCause().getMessage();
                
                throw new BuildException("Error while invoking the setter '" + currMethod + "'" + (cause != null ? cause : ""), ex);
            }
            catch(IllegalAccessException | IllegalArgumentException | SecurityException ex)
            {
            }
            
            if(!set)
            {
                StringBuilder sb = new StringBuilder("\"Cannot find any suitable setter method to set using the following data: ");
                sb.append("target-type=").append(objTarget.getClass());

                String toShowArgType;

                if(argType != null)
                    toShowArgType = argType.toString();
                else
                    toShowArgType = argument.getClass().toString();

                sb.append("; argument-type=").append(toShowArgType);

                if(methodName != null)
                    sb.append("; method-name=").append(methodName);

                throw new BuildException(sb.toString());
            }
        }
    }
    
    private BeanSetter setter;
    private String methodName;
    private Class argType;

    @Override
    public BeanSetterBuilderImpl setMethodName(String methodName) throws BuildException
    {
        this.methodName = methodName;
        return this;
    }

    @Override
    public BeanSetterBuilderImpl setArgType(Class argType) throws BuildException
    {
        this.argType = argType;
        return this;
    }

    @Override
    public void buildInstance() throws BuildException
    {
        setter = new BeanSetter(methodName, argType);
    }

    @Override
    public Setter getInstance() throws BuildException
    {
        return setter;
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("BeanSetter").
            addStateData("methodName", methodName).
            addStateData("argType", argType == null ? null : argType.getSimpleName());
    }
}
