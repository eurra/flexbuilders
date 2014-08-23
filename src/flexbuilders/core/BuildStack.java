
package flexbuilders.core;

/**
 *
 * @author Enrique Urra C.
 */
public interface BuildStack
{
    void push(NestedBuilder buildable);
    void pop();
    boolean isInvalidated();
    String getTextStack();
}
