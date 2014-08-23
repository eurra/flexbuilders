
package flexbuilders.core;

/**
 *
 * @author Enrique Urra C.
 */
public class ProxyDelegate implements Delegate
{
    private BuildHandler proxy;

    void setProxy(BuildHandler proxy)
    {
        this.proxy = proxy;
    }
    
    protected BuildHandler getProxy()
    {
        if(proxy == null)
            throw new BuilderInputException("The internal builder has not been set");
        
        return proxy;
    }
    
    protected <T extends Delegate> T getProxyAs(Class<T> delegateType)
    {
        BuildHandler innerProxy = getProxy();
        
        if(!delegateType.isAssignableFrom(innerProxy.getClass()))
            throw new BuilderInputException("The internal builder is not compatible with the '" + delegateType + "' type");
        
        return delegateType.cast(innerProxy);
    }
}
