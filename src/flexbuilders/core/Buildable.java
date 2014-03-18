
package flexbuilders.core;

/**
 *
 * @author Enrique Urra C.
 */
public interface Buildable<T>
{
    T build() throws BuildException;
}
