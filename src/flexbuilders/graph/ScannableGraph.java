
package flexbuilders.graph;

import flexbuilders.core.BuildException;

/**
 *
 * @author Enrique Urra C.
 */
public interface ScannableGraph extends NestableGraph
{
    void findNodes(String input) throws BuildException;
    void findNodes(String input, ClassLoader classLoader) throws BuildException;
}
