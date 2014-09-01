
package flexbuilders.graph;

/**
 *
 * @author Enrique Urra C.
 */
public enum NodePriority
{
    MANUALLY_SET(-2),
    AUTOLOADED(-1),
    HIGH(0),
    NORMAL(1),
    LOW(2),
    DEFAULT_VALUE(3);

    private final int priority;

    private NodePriority(int priority)
    {
        this.priority = priority;
    }

    public int getPriority()
    {
        return priority;
    }
}
