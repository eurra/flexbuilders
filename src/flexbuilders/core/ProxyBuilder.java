
package flexbuilders.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Enrique Urra C.
 */
public final class ProxyBuilder<T extends Builder> implements Buildable<T>
{
    public static <T extends Builder> ProxyBuilder<T> createProxyFor(Class<T> mainBuildType) throws BuildException
    {
        return new ProxyBuilder<>(mainBuildType);
    }
    
    private static class AdapterBuilderHandler implements InvocationHandler
    {
        private Class mainBuildType;
        private Delegate[] delegates;

        public AdapterBuilderHandler(Class mainBuildType, Delegate[] delegates)
        {
            this.mainBuildType = mainBuildType;
            this.delegates = delegates;
        }
        
        @Override
        public Object invoke(Object mainBuilder, Method method, Object[] args) throws BuildException
        {
            String methodName = method.getName();
            Class[] methodArgsTypes = method.getParameterTypes();
            Delegate targetDelegate = null;
            Method proxyMethod = null;
            
            for(int i = 0; proxyMethod == null && i < delegates.length; i++)
            {
                Delegate currDelegate = delegates[i];
                Class currDelegateType = currDelegate.getClass();
                
                try
                {
                    proxyMethod = currDelegateType.getMethod(methodName, methodArgsTypes);
                    targetDelegate = currDelegate;
                }
                catch(NoSuchMethodException | SecurityException ex)
                {
                }
            }
            
            if(proxyMethod == null)
                throw new BuildException("No suitable delegate implementation has been found to invoke the '" + method + "' method in the builder proxy");
            
            Object result = null;
            
            try
            {
                if(!proxyMethod.isAccessible())
                    proxyMethod.setAccessible(true);
                
                result = proxyMethod.invoke(targetDelegate, args);
            }
            catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                if(ex instanceof InvocationTargetException && ex.getCause() instanceof BuildException)
                    throw (BuildException)ex.getCause();
                
                throw new BuildException("Cannot execute the delegate method '" + proxyMethod + "'", ex);
            }
            
            if(method.getReturnType().equals(mainBuildType))
                return mainBuilder;
            else
                return result;
        }
    }
        
    private T proxy;    
    private Class mainBuildInterface;
    private Set<Delegate> delegates;
    private Map<Class, Delegate> delegatesMap;
    private List<Class> orderedDelegateTypes;
    
    private ProxyBuilder(Class mainBuildType)
    {
        if(mainBuildType == null)
            throw new NullPointerException("Null main build type");
        
        if(!mainBuildType.isInterface())
            throw new IllegalArgumentException("The main build type must be interface");
                
        this.mainBuildInterface = mainBuildType;
        this.delegates = new HashSet<>();
        this.delegatesMap = new HashMap<>();
        this.orderedDelegateTypes = new ArrayList<>();
    }
    
    public ProxyBuilder<T> addDelegate(Delegate delegate) throws BuildException
    {
        if(!delegates.contains(delegate))
            delegates.add(delegate);
        
        return this;
    }
    
    public <K extends Delegate> ProxyBuilder<T> addDelegate(K impl, Class<? super K>... types) throws BuildException
    {
        if(impl == null)
            throw new BuildException("The delegate implementation cannot be null");
        
        if(types == null || types.length == 0)
            throw new BuildException("At least one valid delegate type must be provided");
        
        for(int i = 0; i < types.length; i++)
        {
            if(!types[i].isInterface())
                throw new BuildException("The delegate type '" + types[i] + "' is invalid: it must be an interface");
        }
        
        for(int i = 0; i < types.length; i++)
        {
            if(!delegatesMap.keySet().contains(types[i]))
                orderedDelegateTypes.add(types[i]);

            delegatesMap.put(types[i], impl);
        }
        
        addDelegate(impl);
        return this;
    }
        
    @Override
    public T build() throws BuildException
    {
        if(proxy != null)
            return proxy;
        
        Class[] proxyTypes = new Class[1 + orderedDelegateTypes.size()];
        proxyTypes[0] = mainBuildInterface;
        int index = 1;
        
        for(Class delegateType : orderedDelegateTypes)
            proxyTypes[index++] = delegateType;
        
        Delegate[] allDelegates = delegates.toArray(new Delegate[0]);
        
        try
        {
            proxy = (T)Proxy.newProxyInstance(
                mainBuildInterface.getClassLoader(),
                proxyTypes,
                new AdapterBuilderHandler(mainBuildInterface, allDelegates)
            );
        }
        catch(IllegalArgumentException ex)
        {
            throw new BuildException("Cannot create the builder proxy based on the provided interfaces", ex);
        }
        
        for(int i = 0; i < allDelegates.length; i++)
        {
            if(allDelegates[i] instanceof ProxyDelegate)
                ((ProxyDelegate)allDelegates[i]).setProxy(proxy);
        }
        
        return proxy;
    }
}
