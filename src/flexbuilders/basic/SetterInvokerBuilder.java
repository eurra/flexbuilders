
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;

/**
 *
 * @author Enrique Urra C.
 */
public interface SetterInvokerBuilder<T> extends Builder, Buildable<T>, SetterInvokerInput
{
    @Override SetterInvokerBuilder<T> set(Buildable<? extends Setter> setter, Buildable arg) throws BuildException;
}
