
package flexbuilders.tree;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;

/**
 *
 * @author Enrique Urra C.
 */
public interface TreeHandler
{
    BranchBuilder branch(String id) throws BuildException;
    BranchBuilder branch() throws BuildException;
    BranchBuilder root() throws BuildException;
    BranchBuilderSet branchSet() throws BuildException;
    Buildable ref(String destId) throws BuildException;
}
