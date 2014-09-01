
package flexbuilders.graph;

import flexbuilders.core.BuildException;

/**
 *
 * @author Enrique Urra C.
 */
public interface SubGraphLoader
{
    void loadSubGraph(MutableGraph graph) throws BuildException;
}
