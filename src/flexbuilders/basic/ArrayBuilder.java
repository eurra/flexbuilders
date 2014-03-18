
package flexbuilders.basic;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;

/**
 *
 * @author Enrique Urra C.
 */
public interface ArrayBuilder<T> extends Builder, Buildable, ArrayInput<T>
{
    @Override ArrayBuilder<T> setLength(int length) throws BuildException;
    @Override ArrayBuilder<T> elem(Buildable<? extends T> elem) throws BuildException;
    @Override ArrayBuilder<T> elem(int index, Buildable<? extends T> elem) throws BuildException;
}
