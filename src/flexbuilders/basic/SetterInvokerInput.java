
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface SetterInvokerInput extends Delegate
{
    SetterInvokerInput set(Buildable<? extends Setter> setter, Buildable arg) throws BuildException;
}
