
package flexbuilders.tree;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;

/**
 *
 * @author Enrique Urra C.
 */
public interface BranchBuilder extends Builder, Buildable
{
    BranchBuilder setBuildable(Buildable source) throws BuildException;
    Buildable getBuildable() throws BuildException;
    <T extends Buildable> T getBuildableAs(Class<T> buildableType) throws BuildException;
    BranchBuilder open() throws BuildException;
    BranchBuilder readOnly() throws BuildException;
    BranchBuilder seal() throws BuildException;
    BranchBuilder addTo(BranchBuilderSet set) throws BuildException;
    boolean isRoot();
    boolean isWritable();
    boolean isReadable();
    boolean isFrozen();
}
