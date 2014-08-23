
package flexbuilders.core;

/**
 *
 * @author Enrique Urra C.
 */
public interface Builder<T> extends BuildHandler<T>
{
    T build() throws BuildException;
}
