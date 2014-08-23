
package flexbuilders.basic;

import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public final class BasicBuilders
{    
    public static <T> ObjectBuilder<T> object(Class<? extends T> objType)
    {
        return new DefaultObjectBuilder(objType);
    }
    
    public static <T> ObjectBuilder<T> object(Class<? extends T> objType, NestedBuilder... constructArgs)
    {
        ObjectBuilder res = object(objType);
        
        for(int i = 0; i < constructArgs.length; i++)
            res.nextArgument(constructArgs[i]);
        
        return res;
    }
    
    public static <T> ArrayBuilder<T> array(Class<? extends T> arrType)
    {
        return rawArray(arrType);
    }
    
    public static ArrayBuilder rawArray(Class arrType)
    {
        return new DefaultArrayBuilder(arrType);
    }
    
    public static <T> NestedBuilder<T> builderFor(T value)
    {
        return new InstanceBuilderImpl(value);
    }
    
    public static <T> SetterInvokeBuilder<T> setFor(NestedBuilder<? extends T> target)
    {
        return new DefaultSetterInvokeBuilder(target);
    }
    
    public static GetterInvokeBuilder getFrom(NestedBuilder source)
    {
        return new DefaultGetterInvokeBuilder(source, null);
    }
    
    public static <T> GetterInvokeBuilder<T> getFrom(Class<? extends T> returnType, NestedBuilder source)
    {
        return new DefaultGetterInvokeBuilder(source, returnType);
    }
}
