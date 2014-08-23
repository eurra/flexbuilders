
package flexbuilders.graph;

public interface NodeId<T>
{ 
    /**
     * Parses an input string and returns its equivalent node object instance.
     * In this case, the default classloader is used.
     * @param enumString the string that represent the {@link NodeId} constant.
     * @return the equivalent {@link NodeId} instance.
     * @see NodeId#fromEnumString(java.lang.String, java.lang.ClassLoader) 
     */
    /*public static NodeId fromEnumString(String enumString) throws IllegalArgumentException
    {
        return fromEnumString(enumString, NodeId.class.getClassLoader());
    }*/
    
    /**
     * Parses an input string and returns its equivalent node object instance.
     * It is expected that the input string will refer to an enum constant, and whose 
     * related type implements the {@link NodeId} interface. For example, if there is an
     * enum constant named "CONST" within an enum class located in "some.package.EnumClass",
     * then its associated string representation should be "some.package.EnumClass.CONST".
     * @param enumString the string that represent the {@link NodeId} constant.
     * @param cl the classloader in which the enum type is loaded.
     * @return the equivalent {@link NodeId} instance.
     * @throws IllegalArgumentException if the string do not follow the specified
     *  naming standard, if the enum class cannot be found, if it is not an enum or 
     *  it doesn't implement the {@link NodeId} interface.
     */
    /*public static NodeId fromEnumString(String enumString, ClassLoader cl) throws IllegalArgumentException
    {
        if(enumString == null)
            throw new IllegalArgumentException("Null enum string");
        
        int lastDotPos = enumString.lastIndexOf(".");
        String className = enumString.substring(0, lastDotPos);
        String enumMemberName = enumString.substring(lastDotPos + 1, enumString.length());
        
        Class enumClass;
        
        try
        {
            enumClass = Class.forName(className, true, cl);
        }
        catch(ClassNotFoundException ex)
        {
            throw new IllegalArgumentException("The class '" + className + "' was not found", ex);
        }
        
        if(!enumClass.isEnum())
            throw new IllegalArgumentException("The class '" + className + "' not corresponds to a enum type");
        
        if(!NodeId.class.isAssignableFrom(enumClass))
            throw new IllegalArgumentException("The enum class '" + className + "' not implements the '" + NodeId.class + "' class");
        
        return (NodeId)Enum.valueOf(enumClass, enumMemberName);
    }*/
}
