
package flexbuilders.core;

/**
 *
 * @author Enrique Urra C.
 */
public class ProxyDelegate implements Delegate
{
    private Builder proxy;

    void setProxy(Builder proxy)
    {
        this.proxy = proxy;
    }
    
    protected Builder getProxy() throws BuildException
    {
        if(proxy == null)
            throw new BuildException("The internal builder has not been set");
        
        return proxy;
    }
    
    protected <T extends Delegate> T getProxyAs(Class<T> delegateType) throws BuildException
    {
        Builder innerProxy = getProxy();
        
        if(!delegateType.isAssignableFrom(innerProxy.getClass()))
            throw new BuildException("The internal builder is not compatible with the '" + delegateType + "' type");
        
        return delegateType.cast(innerProxy);
    }
}
