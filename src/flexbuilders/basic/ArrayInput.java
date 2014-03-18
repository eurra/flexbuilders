
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface ArrayInput<T> extends Delegate
{
    ArrayInput<T> setLength(int length) throws BuildException;
    ArrayInput<T> elem(Buildable<? extends T> elem) throws BuildException;
    ArrayInput<T> elem(int index, Buildable<? extends T> elem) throws BuildException;
    Buildable<T[]> asArray();
}
