
package flexbuilders.tree;

import flexbuilders.core.BuildException;

/**
 *
 * @author Enrique Urra C.
 */
public final class TreeBuilders
{
    public static TreeBuilder tree() throws BuildException
    {
        return new TreeBuilderImpl();
    }
}
