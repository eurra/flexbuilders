
package flexbuilders.core;

/**
 *
 * @author Enrique Urra C.
 */
public interface NestedBuilder<T> extends Builder<T>
{
    T build(BuildSession session) throws BuildException;
}
