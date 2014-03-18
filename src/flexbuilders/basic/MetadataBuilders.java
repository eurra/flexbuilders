
package flexbuilders.basic;

import flexbuilders.core.BuildException;

/**
 *
 * @author Enrique Urra C.
 */
public final class MetadataBuilders
{    
    public static  MetadataBuilder metadata() throws BuildException
    {
        return new MetadataBuilderImpl();
    }
}
