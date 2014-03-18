
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.StackBuildable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Enrique Urra C.
 */
class InterfaceSetterBuilderImpl extends StackBuildable<Setter> implements InterfaceSetterBuilder
{
    private static class InterfaceSetter implements Setter
    {
        private Class injector;
        private Method injectorMethod;

        public InterfaceSetter(Class injector, Method injectorMethod)
        {
            this.injector = injector;
            this.injectorMethod = injectorMethod;
        }

        @Override
        public void set(Object target, Object argument) throws BuildException
        {
            if(target == null)
                throw new BuildException("The provided target cannot be null");
            
            if(!injector.isAssignableFrom(target.getClass()))
                throw new NullPointerException("The provided type '" + target.getClass() + "' is not compatible with the injector type '" + injector + "'");
            
            try
            {
                injectorMethod.invoke(target, argument);
            }
            catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                throw new BuildException("Cannot invoke the injector method '" + injectorMethod + "' with the argument type '" + (argument != null ? argument.getClass() : "null") + "' on the target type '" + target.getClass() + "'", ex);
            }
        }
    }
    
    private InterfaceSetter setter;
    private Class injector;
    
    @Override
    public InterfaceSetterBuilderImpl setInjector(Class injector) throws BuildException
    {
        this.injector = injector;
        return this;
    }

    private Method checkInjectorMethod(Class injector) throws BuildException
    {
        if(injector == null)
            throw new BuildException("Null injector");

        if(!injector.isInterface())
            throw new BuildException("The injector type must be an interface ('" + injector + "' was found')");

        Method[] injectorMethods = injector.getMethods();

        if(injectorMethods.length != 1)
            throw new BuildException("The injector type '" + injector + "' does not provide a single setter method");

        Method setterMethod = injectorMethods[0];

        if(!setterMethod.getReturnType().equals(Void.TYPE))
            throw new BuildException("The method '" + setterMethod + "' of the injector type '" + injector + "' does not return void");

        Class[] argTypes = setterMethod.getParameterTypes();

        if(argTypes.length != 1)
            throw new BuildException("The method '" + setterMethod + "' of the injector type '" + injector + "' does not define one single argument");

        return setterMethod;
    }

    @Override
    public void buildInstance() throws BuildException
    {
        if(injector == null)
            throw new BuildException("The injector type has not been set");

        Method injectorMethod = checkInjectorMethod(injector);
        setter = new InterfaceSetter(injector, injectorMethod);
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
            setName("InterfaceSetter").
            addStateData("injector", injector == null ? null : injector.getSimpleName());
    }
}
