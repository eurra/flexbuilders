
package flexbuilders.tree;

import flexbuilders.core.BuildException;

/**
 *
 * @author Enrique Urra C.
 */
public interface BranchBuilderSet
{
    void addBranch(BranchBuilder branch);
    BranchBuilderSet open() throws BuildException;
    BranchBuilderSet readOnly() throws BuildException;
    BranchBuilderSet seal() throws BuildException;
}
