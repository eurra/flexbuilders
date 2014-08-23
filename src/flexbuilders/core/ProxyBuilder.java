
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
 * @param <T>
 */
public final class ProxyBuilder<T extends BuildHandler> extends AbstractBuilder<T>
{
    public static <T extends BuildHandler> ProxyBuilder<T> createProxyFor(Class<T> mainBuildType)
    {
        return new ProxyBuilder<>(mainBuildType);
    }
    
    private static class AdapterBuilderHandler implements InvocationHandler
    {
        private final Class mainBuildType;
        private final Delegate[] delegates;

        public AdapterBuilderHandler(Class mainBuildType, Delegate[] delegates)
        {
            this.mainBuildType = mainBuildType;
            this.delegates = delegates;
        }
        
        @Override
        public Object invoke(Object mainBuilder, Method method, Object[] args)
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
            catch(IllegalAccessException | IllegalArgumentException ex)
            {
                throw new BuildException("Cannot execute the delegate method '" + proxyMethod + "'", ex);
            }
            catch(InvocationTargetException ex)
            {
                Throwable cause = ex.getCause();
                
                if(cause instanceof BuildException)
                    throw (BuildException)ex.getCause();
                
                throw new BuildException("The execution of the delegate method '" + proxyMethod + "' has failed: " + cause.getLocalizedMessage(), cause);
            }
            
            if(method.getReturnType().equals(mainBuildType))
                return mainBuilder;
            else
                return result;
        }
    }
            
    private final Class mainBuildInterface;
    private Set<Delegate> delegates;
    private Map<Class, Delegate> delegatesMap;
    private List<Class> orderedDelegateTypes;
    
    private ProxyBuilder(Class mainBuildType)
    {
        if(mainBuildType == null)
            throw new BuilderInputException("Null main build type");
        
        if(!mainBuildType.isInterface())
            throw new BuilderInputException("The main build type must be interface");
                
        this.mainBuildInterface = mainBuildType;
        this.delegates = new HashSet<>();
        this.delegatesMap = new HashMap<>();
        this.orderedDelegateTypes = new ArrayList<>();
    }
    
    public ProxyBuilder<T> addDelegate(Delegate delegate)
    {
        if(!delegates.contains(delegate))
            delegates.add(delegate);
        
        return this;
    }
    
    public <K extends Delegate> ProxyBuilder<T> addDelegate(K impl, Class<? super K>... types)
    {
        if(impl == null)
            throw new BuilderInputException("The delegate implementation cannot be null");
        
        if(types == null || types.length == 0)
            throw new BuilderInputException("At least one valid delegate type must be provided");
        
        for (Class<? super K> type : types)
        {
            if (!type.isInterface())
                throw new BuilderInputException("The delegate type '" + type + "' is invalid: it must be an interface");
        }
        
        for (Class<? super K> type : types)
        {
            if (!delegatesMap.keySet().contains(type))
                orderedDelegateTypes.add(type);
            
            delegatesMap.put(type, impl);
        }
        
        addDelegate(impl);
        return this;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        Class[] proxyTypes = new Class[1 + orderedDelegateTypes.size()];
        proxyTypes[0] = mainBuildInterface;
        int index = 1;
        
        for(Class delegateType : orderedDelegateTypes)
            proxyTypes[index++] = delegateType;
        
        Delegate[] allDelegates = delegates.toArray(new Delegate[0]);
        T proxy;
        
        try
        {
            proxy = (T)Proxy.newProxyInstance(
                mainBuildInterface.getClassLoader(),
                proxyTypes,
                new AdapterBuilderHandler(mainBuildInterface, allDelegates)
            );
            
            session.registerResult(this, proxy);
        }
        catch(IllegalArgumentException ex)
        {
            throw new BuildException("Cannot create the builder proxy based on the provided interfaces", ex);
        }
        
        for (Delegate allDelegate : allDelegates)
        {
            if (allDelegate instanceof ProxyDelegate)
                ((ProxyDelegate)allDelegate).setProxy(proxy);
        }
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().setName("Proxy");
    }
}
