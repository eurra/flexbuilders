
package flexbuilders.tree;

import flexbuilders.core.BuildException;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface TreeInput extends Delegate, TreeHandler
{
    TreeInput dispose() throws BuildException;
}
