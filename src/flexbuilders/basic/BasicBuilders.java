
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;

/**
 *
 * @author Enrique Urra C.
 */
public final class BasicBuilders
{    
    public static <T> ObjectBuilder<T> object(Class<T> objType) throws BuildException
    {
        return new ObjectBuilderImpl<>(objType);
    }
    
    public static <T> ArrayBuilder<T> array(Class<T> arrType) throws BuildException
    {
        return rawArray(arrType);
    }
    
    public static ArrayBuilder rawArray(Class arrType) throws BuildException
    {
        return new ArrayBuilderImpl(arrType);
    }
    
    public static <T> Buildable<T> value(T value) throws BuildException
    {
        return new InstanceBuilderImpl<>(value);
    }
}
