
package flexbuilders.basic;

import flexbuilders.core.BuildException;

/**
 *
 * @author Enrique Urra C.
 */
public interface Setter
{
    void set(Object target, Object argument) throws BuildException;
}
